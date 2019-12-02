package no.mad.recpatcha.recaptchasecurityhub.controller;

import java.net.URI;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import no.mad.recpatcha.recaptchasecurityhub.dao.RecaptchaRepository;
import no.mad.recpatcha.recaptchasecurityhub.model.ErrorCode;
import no.mad.recpatcha.recaptchasecurityhub.model.Recaptcha;
import no.mad.recpatcha.recaptchasecurityhub.model.rest.RecaptchaRequest;
import no.mad.recpatcha.recaptchasecurityhub.model.rest.RecaptchaResponse;
import no.mad.recpatcha.recaptchasecurityhub.model.rest.SiteKeyResponse;
import no.mad.recpatcha.recaptchasecurityhub.model.rest.VerifyTokenResponse;

@RestController
@RequestMapping("/rest")
public class RecaptchaRestController {

    @Value("${recaptcha.url}")
    private String RECAPTCHA_URL;

    @Autowired
    RecaptchaRepository recaptchaRepository;

    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(RecaptchaRestController.class);

    @GetMapping("/sitekey/{application}")
    public SiteKeyResponse getSiteKey(@PathVariable String application) {
        SiteKeyResponse siteKeyResponse = new SiteKeyResponse();
        siteKeyResponse.setApplication(application);
        siteKeyResponse.setSiteKey(recaptchaRepository.findSiteKeyByApplication(application));
        return siteKeyResponse;
    }

    @GetMapping("/token/verify")
    public VerifyTokenResponse verifyToken(@ModelAttribute @Valid RecaptchaRequest recaptchaRequest) throws Exception {
        Recaptcha recaptcha = recaptchaRepository.findByApplication(recaptchaRequest.getApplication());

        String secretKey = recaptcha.getSecretKey();
        Double threshold = recaptcha.getThreshold();

        URI verifyUri = URI.create(String.format(RECAPTCHA_URL + "?secret=%s&response=%s",
          secretKey, recaptchaRequest.getToken()) + (!StringUtils.isEmpty(recaptchaRequest.getClientIP()) 
            ? String.format("&remoteip=%s", recaptchaRequest.getClientIP()) : ""));
 

        RecaptchaResponse recaptchaResponse = restTemplate.getForObject(verifyUri, RecaptchaResponse.class);

        if (recaptchaResponse == null) {
            throw new Exception("Google Recaptcha is Unavailable");
        }
        logger.info("Recaptcha Response: " + recaptchaResponse.toString());

        Boolean isHuman = (recaptchaResponse.getScore() != null && recaptchaResponse.getScore() >= threshold);
        Boolean isVerified = (!recaptchaResponse.isSuccess() || !isHuman) ? Boolean.FALSE : Boolean.TRUE;

        if (recaptchaResponse.getErrorCodes() == null && !isHuman) {
            recaptchaResponse.setErrorCodes(new ErrorCode[]{ErrorCode.ThresholdFailed});
        }

        return new VerifyTokenResponse(recaptchaRequest.getApplication(), recaptchaRequest.getToken(), 
            isVerified, recaptchaResponse.getErrorCodes());
    }
}