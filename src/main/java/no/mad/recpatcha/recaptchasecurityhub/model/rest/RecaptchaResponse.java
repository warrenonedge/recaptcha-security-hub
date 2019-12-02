package no.mad.recpatcha.recaptchasecurityhub.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import no.mad.recpatcha.recaptchasecurityhub.model.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder({
        "success",
        "score",
        "action",
        "challenge_ts",
        "hostname",
        "error-codes"
    })
@Data @NoArgsConstructor @ToString
public class RecaptchaResponse {
     
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("score")
    private Double score;

    @JsonProperty("action")
    private String action;
        
    @JsonProperty("challenge_ts")
    private String challengeTs;
        
    @JsonProperty("hostname")
    private String hostname;
        
    @JsonProperty("error-codes")
    private ErrorCode[] errorCodes;
    
    @JsonIgnore
    public boolean hasClientError() {
        ErrorCode[] errors = getErrorCodes();
        if(errors == null) {
            return false;
        }
        for(ErrorCode error : errors) {
            switch(error) {
                case InvalidResponse:
                case MissingResponse:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}