package com.growin.marvel.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growin.marvel.api.dto.response.ServiceResponse;
import com.growin.marvel.api.dto.response.TranslateResponse;
import com.growin.marvel.api.model.MarvelCharacter;
import com.growin.marvel.api.repositoy.ICharacterIdRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CharacterServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ICharacterIdRepository repository;

    @Autowired
    private ICharacterService service;


    private static InputStream readJson(String fileName) throws IOException {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(fileName);
    }

    @BeforeAll
    public void setUp() throws IOException {
        var body = readJson("mock/characters-success.json");
        when(restTemplate.getForObject(anyString(), eq(ServiceResponse.class)))
                .thenReturn(new ObjectMapper().readValue(body, ServiceResponse.class));
    }

    @Test
    void shouldReturnListOfIds() throws IOException {

        var list = new ArrayList<MarvelCharacter>();
        list.add(new MarvelCharacter(100));
        when(repository.findAll())
                .thenReturn(list);

        assertTrue(service.getAllIds().iterator().hasNext());
    }

    @Test
    void shouldReturnEmptyListIfNotExists() throws IOException {

        var list = new ArrayList<MarvelCharacter>();
        when(repository.findAll())
                .thenReturn(list);

        assertFalse(service.getAllIds().iterator().hasNext());
    }


    @Test
    void shouldReturnDetailCharacterValues() throws IOException {

        var body = readJson("mock/characters-success.json");
        when(restTemplate.getForObject(anyString(), eq(ServiceResponse.class)))
                .thenReturn(new ObjectMapper().readValue(body, ServiceResponse.class));

        assertDoesNotThrow(() -> service.getCharacterById(1, ""));
    }

    @Test
    void shouldReturnDetailCharacterValuesWithTranslatedDescription() throws IOException {
        var body = readJson("mock/characters-success.json");
        var bodyTranslate = readJson("mock/translate-success.json");
        when(restTemplate.getForObject(anyString(), eq(ServiceResponse.class)))
                .thenReturn(new ObjectMapper().readValue(body, ServiceResponse.class));

        when(restTemplate.getForObject(anyString(), eq(TranslateResponse.class)))
                .thenReturn(new ObjectMapper().readValue(bodyTranslate, TranslateResponse.class));

        assertDoesNotThrow(() -> service.getCharacterById(1, "pt"));
    }

    @Test
    void shouldThrowNotFoundExceptionIfResponseIsNull() throws IOException {
        when(restTemplate.getForObject(anyString(), eq(ServiceResponse.class)))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.getCharacterById(1, ""));
    }


    @Test
    void shouldNotThrowErrorWhenIsSavingIds() throws IOException {
        var body = readJson("mock/characters-success.json");
        when(restTemplate.getForObject(anyString(), eq(ServiceResponse.class)))
                .thenReturn(new ObjectMapper().readValue(body, ServiceResponse.class));
        when(repository.findAll())
                .thenReturn(new ArrayList<MarvelCharacter>());

        assertDoesNotThrow(() -> service.saveAllIdsInMemory());
    }

}
