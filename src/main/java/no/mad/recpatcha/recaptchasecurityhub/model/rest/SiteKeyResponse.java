package no.mad.recpatcha.recaptchasecurityhub.model.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class SiteKeyResponse {

    String application;
    String siteKey;
}