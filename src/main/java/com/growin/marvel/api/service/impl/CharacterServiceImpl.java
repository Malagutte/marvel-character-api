package com.growin.marvel.api.service.impl;

import com.growin.marvel.api.dto.response.CharacterDataResponse;
import com.growin.marvel.api.dto.response.ServiceResponse;
import com.growin.marvel.api.dto.response.TranslateResponse;
import com.growin.marvel.api.model.MarvelCharacter;
import com.growin.marvel.api.repositoy.ICharacterIdRepository;
import com.growin.marvel.api.service.ICharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.webjars.NotFoundException;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.join;

@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterServiceImpl implements ICharacterService {

    private final ICharacterIdRepository characterIdRepository;

    private final RestTemplate restTemplate;

    @Value("${marvel.api.key.public}")
    private String publicApiKey;

    @Value("${marvel.api.key.private}")
    private String privateApiKey;

    @Value("${marvel.api.url}")
    private String baseUrl;

    @Value("${translate.api.url}")
    private String translateBaseUrl;

    @Value("${marvel.api.limit}")
    private Integer resultLimit;


    @Override
    public Iterable<Integer> getAllIds() {
        var searchResult = characterIdRepository.findAll().spliterator();
        return StreamSupport.stream(searchResult, false)
                .map(MarvelCharacter::getId)
                .toList();
    }

    @Override
    public CharacterDataResponse getCharacterById(Integer id, String language) throws Exception {
        ServiceResponse response = getCharacterById(id);

        if (response == null || response.getData() == null) {
            throw new NotFoundException("response is empty");
        }

        var data = response.getData().getResults().get(0);

        if (isNotBlank(data.getDescription()) && isNotBlank(language)) {
            translateDescription(language, data);
        }

        return data;

    }

    private void translateDescription(String language, CharacterDataResponse data) {
        var uriTranslate = UriComponentsBuilder.fromUriString(translateBaseUrl)
                .pathSegment("/get")
                .queryParam("q", data.getDescription())
                .queryParam("langpair", join("en|", language))
                .build().toString();

        var translate = restTemplate.getForObject(uriTranslate, TranslateResponse.class);

        if (translate != null && translate.getResponseData() != null) {
            data.setDescription(translate.getResponseData().getTranslatedText());
        }
    }

    private ServiceResponse getCharacterById(Integer id) throws NoSuchAlgorithmException {
        var uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/v1/public/characters/")
                .pathSegment(id.toString())
                .queryParam("apikey", publicApiKey)
                .queryParam("hash", getHash(1))
                .queryParam("ts", 1)
                .build();

        return restTemplate.getForObject(uri.toString(), ServiceResponse.class);
    }

    @Override
    public void saveAllIdsInMemory() throws NoSuchAlgorithmException {
        var result = characterIdRepository.findAll();
        var existValues = result.iterator().hasNext();

        if (existValues) {
            log.warn("values already in memory!!!");
            return;
        }

        var timeStamp = 1;
        var hash = getHash(timeStamp);

        var page = 1;
        var count = 0;
        for (; ; page++) {
            var response = getCharactersFromApi(page, hash, timeStamp);

            if (response == null) {
                break;
            }

            var total = response.getData().getTotal();

            var ids = getResultData(response);

            count += ids.size();
            log.info("{}/{} missing: {}", count, total, total - count);

            if (!ids.isEmpty()) {
                log.debug("page: {}, total of ids: {}", page, ids.size());
                characterIdRepository.saveAll(ids);
            }

            if (count >= total) {
                break;
            }
        }


    }

    private List<MarvelCharacter> getResultData(ServiceResponse firstResponse) {
        return firstResponse.getData()
                .getResults()
                .stream()
                .map(data -> new MarvelCharacter(data.getId()))
                .toList();
    }

    private ServiceResponse getCharactersFromApi(Integer page, String hash, int timestamp) {
        var uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/v1/public/characters")
                .queryParam("apikey", publicApiKey)
                .queryParam("limit", resultLimit)
                .queryParam("offset", resultLimit * (page - 1))
                .queryParam("hash", hash)
                .queryParam("ts", timestamp)
                .build();


        log.debug("offset {} url: {}", page, uri);
        return restTemplate.getForObject(uri.toString(), ServiceResponse.class);
    }

    private String getHash(int timeStamp) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        var key = StringUtils.join(timeStamp, privateApiKey, publicApiKey);
        md.update(key.getBytes());
        return DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
    }
}
