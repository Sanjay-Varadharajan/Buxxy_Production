package com.buxxy.buxxy_fraud_engine.controller.transaction;


import com.buxxy.buxxy_fraud_engine.dto.transaction.TransactionResponseDTO;
import com.buxxy.buxxy_fraud_engine.service.transaction.UserLevelTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserLevelTransactionController {

    private final UserLevelTransactionService userLevelTransactionService;



    @GetMapping("/view/transactions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<TransactionResponseDTO>> viewTransactions(Principal principal,
                                                                         @PageableDefault(
                                                                                 page = 0,
                                                                                 size = 10,
                                                                                 sort = "transactionOn",
                                                                                 direction = Sort.Direction.DESC
                                                                         ) Pageable pageable){
       Page<TransactionResponseDTO> response= userLevelTransactionService
                .viewTransactions(principal,pageable);

        return ResponseEntity.ok(response);

    }

}
