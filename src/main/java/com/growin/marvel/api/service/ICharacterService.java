package com.growin.marvel.api.service;

import com.growin.marvel.api.dto.response.CharacterDataResponse;

import java.security.NoSuchAlgorithmException;

public interface ICharacterService {

    Iterable<Integer> getAllIds();

    CharacterDataResponse getCharacterById(Integer id, String language) throws Exception;

    void saveAllIdsInMemory() throws NoSuchAlgorithmException;

}