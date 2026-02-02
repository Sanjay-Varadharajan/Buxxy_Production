package com.buxxy.buxxy_fraud_engine.controller.transaction;


import com.buxxy.buxxy_fraud_engine.configurations.security.SystemApiKeyFilter;
import com.buxxy.buxxy_fraud_engine.dto.transaction.TransactionCreateDTO;
import com.buxxy.buxxy_fraud_engine.dto.transaction.TransactionResponseDTO;
import com.buxxy.buxxy_fraud_engine.idempotency.service.IdempotentService;
import com.buxxy.buxxy_fraud_engine.service.transaction.SystemLevelTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/system")
public class SystemLevelTransactionController {

    private final SystemLevelTransactionService systemLevelTransactionService;

    private final IdempotentService idempotentService;

    private static final Logger logger = LoggerFactory.getLogger(SystemLevelTransactionController.class);


    @PostMapping("/transaction/execute")
    public ResponseEntity<TransactionResponseDTO> executeTransaction(@RequestHeader("idempotency-key") String idempotencyKey,
                                                                     @Valid @RequestBody TransactionCreateDTO transactionCreateDTO
    ) {

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            TransactionResponseDTO response = idempotentService.executeIdempotent(
                    idempotencyKey,
                    transactionCreateDTO,
                    TransactionResponseDTO.class,
                    () -> systemLevelTransactionService.executeTransaction(transactionCreateDTO)
            );
            logger.info("System transaction executed successfully for idempotency-key: {}", idempotencyKey);
            return ResponseEntity.ok(response);
        }

        catch (Exception e) {
            logger.error("Error executing system transaction: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
