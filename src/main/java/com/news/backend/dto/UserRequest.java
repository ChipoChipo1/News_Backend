package com.news.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "허용하지 않는 이메일 형식입니다.")
    private String email;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Pattern(
            regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "전화번호 형식은 000-0000-0000 이어야 합니다."
    )
    private String phoneNumber;
}