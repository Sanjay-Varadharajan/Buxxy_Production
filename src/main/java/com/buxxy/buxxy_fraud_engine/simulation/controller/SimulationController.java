package com.buxxy.buxxy_fraud_engine.simulation.controller;

import com.buxxy.buxxy_fraud_engine.simulation.dto.SimulationTransactionDTO;
import com.buxxy.buxxy_fraud_engine.simulation.service.SimulationTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/simulation")
public class SimulationController {


    private final SimulationTransactionService simulationTransactionService;



    @PostMapping("/transaction")
    @PreAuthorize("hasRole('TESTER')")
    public ResponseEntity<SimulationTransactionDTO> transaction(@RequestBody
                                                                    @Valid SimulationTransactionDTO transactionDTO,
                                                                Principal principal){
        SimulationTransactionDTO simulationTransaction=
                simulationTransactionService.transaction(transactionDTO,principal);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(simulationTransaction);
    }
}

