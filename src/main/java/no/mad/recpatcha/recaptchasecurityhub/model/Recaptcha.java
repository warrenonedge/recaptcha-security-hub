package no.mad.recpatcha.recaptchasecurityhub.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor
public class Recaptcha {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    @NotBlank(message = "Application Name must not be blank")
    private String application;
    
    @NotBlank(message = "Site Key must not be blank")
    private String siteKey;

    @NotBlank(message = "Secret Key must not be blank")
    private String secretKey;

    @NotNull(message = "Threshold must not be blank")
    @DecimalMin(value = "0.0", message = "Threshold must be a Decimal value between 0.0 - 1.0")
    @DecimalMax(value = "1.0", message = "Threshold must be a Decimal value between 0.0 - 1.0")
    private Double threshold;
}