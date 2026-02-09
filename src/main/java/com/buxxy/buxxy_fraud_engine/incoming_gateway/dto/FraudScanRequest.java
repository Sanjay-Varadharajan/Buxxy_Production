package com.buxxy.buxxy_fraud_engine.incoming_gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FraudScanRequest {

    private String externalUserId;

    private String upiId;

    private String email;

    private BigDecimal amount;

    private String homeCountry;
    private String homeCity;

    private String awayCountry;
    private String awayCity;

    private String ipAddress;
}
