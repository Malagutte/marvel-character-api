package com.growin.marvel.api;

import com.growin.marvel.api.service.ICharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@RequiredArgsConstructor
public class MarvelApiApplication {
	private final ICharacterService service;

	public static void main(String[] args) {
		SpringApplication.run(MarvelApiApplication.class, args);
	}

	@PostConstruct
	@Profile("!test")
	private void getCharacterIds() throws NoSuchAlgorithmException {
		service.saveAllIdsInMemory();
	}


}
