package com.buxxy.buxxy_fraud_engine.service.transaction;

import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.geolocation.GeoLocationService;
import com.buxxy.buxxy_fraud_engine.controller.transaction.TransactionService;
import com.buxxy.buxxy_fraud_engine.dto.transaction.TransactionCreateDTO;
import com.buxxy.buxxy_fraud_engine.dto.transaction.TransactionResponseDTO;
import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SystemLevelTransactionService implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final HttpServletRequest httpServletRequest;

    private final UserRepository userRepository;

    private final GeoLocationService geoLocationService;

    @Override
    public TransactionResponseDTO executeTransaction(TransactionCreateDTO transactionCreateDTO) {
        User user = userRepository.findByUserMailAndUserActiveTrue("SYSTEM").orElseThrow(() -> new UsernameNotFoundException("System Not Found"));
        String ipAddress=httpServletRequest.getRemoteAddr();
        String homeCountry=geoLocationService.getCountry(ipAddress);
        String homeCity=geoLocationService.getCity(ipAddress);

        Transaction transaction=new Transaction();
        transaction.setUser(user);
        transaction.setTransactionAmount(transactionCreateDTO.getTransactionAmount());
        transaction.setIpAddress(ipAddress);
        transaction.setTransactionHomeCountry(homeCountry);
        transaction.setTransactionHomeCity(homeCity);
        transaction.setTransactionAwayCountry(transactionCreateDTO.getTransactionAwayCountry());
        transaction.setTransactionAwayCity(transactionCreateDTO.getTransactionAwayCity());
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);
        return new TransactionResponseDTO(transaction);
    }

    @Override
    public Transaction findTransactionById(long transactionId)  {
        Transaction transaction=transactionRepository.findById(transactionId).orElseThrow(()->new RuntimeException("Transaction Not Found"));
        return transaction;
    }
}
