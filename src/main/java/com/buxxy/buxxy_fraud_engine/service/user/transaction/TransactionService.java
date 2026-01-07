package com.buxxy.buxxy_fraud_engine.service.user.transaction;

import com.buxxy.buxxy_fraud_engine.dto.transaction.TransactionResponseDTO;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    public Page<TransactionResponseDTO> viewTransactions(Principal principal,
                                                         Pageable pageable) {

        User LoggedInUser=userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(()->new UsernameNotFoundException(principal.getName()+" Not Found,login and try"));

        Set<String> allowed = Set.of("transactionOn", "transactionAmount", "transactionStatus");

        pageable.getSort().forEach(order -> {
            if (!allowed.contains(order.getProperty())) {
                throw new IllegalArgumentException("Invalid sort field: " + order.getProperty());
            }
        });

        Page<Transaction> transactionPage=transactionRepository.findByUser_UserMail(
                principal.getName(),pageable
        );
        return transactionPage.map(TransactionResponseDTO::new);
    }


}
