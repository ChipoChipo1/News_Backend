package com.news.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "keywords")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;         // 키워드 고유 ID

    private String email;    // 사용자 이메일
    private String keyword;  // 키워드 값

    private Long createdAt;  // 키워드 생성 시간
    private Long updatedAt;  // 키워드 수정 시간
    // 생성자 추가
    public Keyword(String email, String keyword, Long createdAt, Long updatedAt) {
        this.email = email;
        this.keyword = keyword;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now().toEpochMilli();
    }
}
