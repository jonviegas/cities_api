package com.jonservices.citiesapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "pais")
public class Country extends RepresentationModel<Country> {

    @Id
    private Long id;

    @Column(name = "nome")
    private String name;

    @Column(name = "sigla")
    private String code;

    @Column(name = "bacen")
    private Integer bacen;

}
