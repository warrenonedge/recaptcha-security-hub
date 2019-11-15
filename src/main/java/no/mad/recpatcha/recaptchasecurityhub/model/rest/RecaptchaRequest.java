package no.mad.recpatcha.recaptchasecurityhub.model.rest;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class RecaptchaRequest {

    @NotBlank(message = "Application must not be blank")
    String application;

    String token;

    String clientIP;
}