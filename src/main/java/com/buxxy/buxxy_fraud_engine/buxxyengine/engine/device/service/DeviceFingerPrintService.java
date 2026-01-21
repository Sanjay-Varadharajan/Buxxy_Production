package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.device.service;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class DeviceFingerPrintService {

    public String sha256(String input) {
        return DigestUtils.sha256Hex(input);
    }

    public String fingerprint(String userAgent, String timeZone, String acceptedLanguage) {
        return sha256(userAgent + "|" + timeZone + "|" + acceptedLanguage);
    }
    public String generateFromRequest(HttpServletRequest request){
        String userAgent=request.getHeader("User-Agent");
        String acceptedLanguage=request.getHeader("Accept-Language");

        String ip=request.getRemoteAddr();
        String rawInput=userAgent+ "|" +acceptedLanguage+ "|" +ip;

        return sha256(rawInput);
    }

}
