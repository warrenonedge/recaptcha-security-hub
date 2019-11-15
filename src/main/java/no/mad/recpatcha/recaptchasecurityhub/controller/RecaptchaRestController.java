package no.mad.recpatcha.recaptchasecurityhub.controller;

import java.net.URI;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import no.mad.recpatcha.recaptchasecurityhub.dao.RecaptchaRepository;
import no.mad.recpatcha.recaptchasecurityhub.model.rest.RecaptchaRequest;
import no.mad.recpatcha.recaptchasecurityhub.model.rest.RecaptchaResponse;
import no.mad.recpatcha.recaptchasecurityhub.model.rest.SiteKeyResponse;

@RestController
@RequestMapping("/rest")
public class RecaptchaRestController {

    @Value("${recaptcha.url}")
    private String RECAPTCHA_URL;

    @Autowired
    RecaptchaRepository recaptchaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/sitekey/{application}")
    public SiteKeyResponse getSiteKey(@PathVariable String application) {
        SiteKeyResponse siteKeyResponse = new SiteKeyResponse();
        siteKeyResponse.setApplication(application);
        siteKeyResponse.setSiteKey(recaptchaRepository.findSiteKeyByApplication(application));
        return siteKeyResponse;
    }

    @GetMapping("/token/verify")
    public RecaptchaResponse verifyToken(@ModelAttribute @Valid RecaptchaRequest recaptchaRequest) {
        String secretKey = recaptchaRepository.findSecretKeyByApplication(recaptchaRequest.getApplication());

        URI verifyUri = URI.create(String.format(RECAPTCHA_URL + "?secret=%s&response=%s",
          secretKey, recaptchaRequest.getToken()) + (!StringUtils.isEmpty(recaptchaRequest.getClientIP()) 
            ? String.format("&remoteip=%s", recaptchaRequest.getClientIP()) : ""));
 
        return restTemplate.getForObject(verifyUri, RecaptchaResponse.class);
    }
}