package org.example.urlshortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "urls")
@Setter
@Getter
public class Url implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String shortUrl;

    @Column(nullable = false)
    private String longUrl;

}
