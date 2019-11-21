package no.mad.recpatcha.recaptchasecurityhub.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ErrorCode {
    MissingSecret,     InvalidSecret,
    MissingResponse,   InvalidResponse;

    private static Map<String, ErrorCode> errorsMap = new HashMap<String, ErrorCode>(4);

    static {
        errorsMap.put("missing-input-secret",   MissingSecret);
        errorsMap.put("invalid-input-secret",   InvalidSecret);
        errorsMap.put("missing-input-response", MissingResponse);
        errorsMap.put("invalid-input-response", InvalidResponse);
    }

    @JsonCreator
    public static ErrorCode forValue(String value) {
        return errorsMap.get(value.toLowerCase());
    }
}