package com.buxxy.buxxy_fraud_engine.simulation.controller;

import com.buxxy.buxxy_fraud_engine.dto.transaction.TransactionResponseDTO;
import com.buxxy.buxxy_fraud_engine.idempotency.service.IdempotentService;
import com.buxxy.buxxy_fraud_engine.simulation.dto.SimulationTransactionDTO;
import com.buxxy.buxxy_fraud_engine.simulation.service.SimulationTransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/simulation")
public class SimulationController {


    private final SimulationTransactionService simulationTransactionService;

    private final IdempotentService idempotentService;


    @PostMapping("/transaction")
    @PreAuthorize("hasRole('TESTER')")
    public ResponseEntity<SimulationTransactionDTO> transaction(
                                                                @RequestBody @Valid SimulationTransactionDTO transactionDTO,
                                                                Principal principal,
                                                                HttpServletRequest httpServletRequest,
                                                                @RequestHeader("Idempotency-Key")String idempotencyKey){
        SimulationTransactionDTO simulationTransaction=idempotentService.executeIdempotent(
                idempotencyKey,
                transactionDTO,
                SimulationTransactionDTO.class,
                ()->simulationTransactionService.transaction(transactionDTO,principal,httpServletRequest)
        );


        return ResponseEntity.status(HttpStatus.CREATED)
                .body(simulationTransaction);
    }
}

