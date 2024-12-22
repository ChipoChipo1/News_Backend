package com.news.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "news")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    private String id;  // 뉴스 ID

    private String title;  // 뉴스 제목
    private String content;  // 뉴스 내용
    private String url;     // 뉴스 URL
}
