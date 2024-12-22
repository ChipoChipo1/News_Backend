package com.news.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scraps")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Scrap {

    @Id
    private String email;

    private String newsId;
}
