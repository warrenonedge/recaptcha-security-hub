package no.mad.recpatcha.recaptchasecurityhub.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.mad.recpatcha.recaptchasecurityhub.model.ErrorCode;

@Data @NoArgsConstructor @AllArgsConstructor
public class VerifyTokenResponse {

    String application;

    String token;

    Boolean isVerified;

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