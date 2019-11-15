package no.mad.recpatcha.recaptchasecurityhub.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import no.mad.recpatcha.recaptchasecurityhub.model.Recaptcha;

public interface RecaptchaRepository extends CrudRepository<Recaptcha, Integer> {
    Recaptcha findByApplication(String application);

    @Query("SELECT r.siteKey FROM Recaptcha r where r.application = :application")
    String findSiteKeyByApplication(String application);

    @Query("SELECT r.secretKey FROM Recaptcha r where r.application = :application")
    String findSecretKeyByApplication(String application);
}