package com.growin.marvel.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "characters")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarvelCharacter {
    @Id
    @Column
    private Integer id;
}
