package com.growin.marvel.api.repositoy;

import com.growin.marvel.api.model.MarvelCharacter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICharacterIdRepository extends CrudRepository<MarvelCharacter,Integer> {

}
