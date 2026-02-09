package com.buxxy.buxxy_fraud_engine.engine.device.extractor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class DeviceContextExtractor {


    public String getUserAgent(HttpServletRequest httpServletRequest){
        String userAgent=httpServletRequest.getHeader("User-Agent");
        return userAgent;
    }

    public String getTimeZone(HttpServletRequest httpServletRequest){
        String timeZone=httpServletRequest.getHeader("Time-Zone");
        return timeZone;
    }

    public String getLanguage(HttpServletRequest httpServletRequest){
        String language=httpServletRequest.getHeader("Accept-Language");
        return language;
    }
}
