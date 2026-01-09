package com.buxxy.buxxy_fraud_engine.dto.transaction;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionCreateDTO {
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotBlank
    private String location;
}