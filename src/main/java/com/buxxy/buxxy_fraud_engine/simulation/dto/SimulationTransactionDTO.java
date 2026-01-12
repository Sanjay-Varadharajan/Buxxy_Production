package com.buxxy.buxxy_fraud_engine.simulation.dto;

import com.buxxy.buxxy_fraud_engine.enums.Decision;
import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.simulation.enums.SimulationScenario;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimulationTransactionDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    private Long transactionId;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal transactionAmount;

    @NotBlank(message = "Transaction location is required")
    private String transactionLocation;

    private LocalDateTime transactionOn;

    private TransactionStatus transactionStatus;

    private String channel;

    private String message;

    private String deviceId;

    private Decision transactionDecision;

    private boolean simulated = true;

    private SimulationScenario scenario;
}
