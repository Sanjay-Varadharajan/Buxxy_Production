package com.buxxy.buxxy_fraud_engine.simulation.service;

import com.buxxy.buxxy_fraud_engine.enums.Decision;
import com.buxxy.buxxy_fraud_engine.enums.TransactionStatus;
import com.buxxy.buxxy_fraud_engine.model.Transaction;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.otp.EmailService;
import com.buxxy.buxxy_fraud_engine.otp.OtpService;
import com.buxxy.buxxy_fraud_engine.repositories.TransactionRepository;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import com.buxxy.buxxy_fraud_engine.service.engine.fruadcontrol.FraudControlService;
import com.buxxy.buxxy_fraud_engine.simulation.dto.SimulationTransactionDTO;
import com.buxxy.buxxy_fraud_engine.simulation.enums.SimulationScenario;
import com.buxxy.buxxy_fraud_engine.simulation.utils.SimulationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class SimulationTransactionService {

    private final TransactionRepository transactionRepository;
    private final FraudControlService fraudControlService;
    private final UserRepository userRepository;
    private final SimulationUtils simulationUtils;
    private final OtpService otpService;
    private final EmailService emailService;

    private final Random random = new Random();


    public SimulationTransactionDTO transaction(SimulationTransactionDTO transactionDTO,
                                                Principal principal) {

        User loggedInUser = userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName() + " not found, login and try"));

        SimulationScenario scenario = transactionDTO.getScenario() != null
                ? transactionDTO.getScenario()
                : SimulationScenario.NORMAL;

        BigDecimal amount = transactionDTO.getTransactionAmount() != null
                ? transactionDTO.getTransactionAmount()
                : generateAmount(scenario);

        String location = transactionDTO.getTransactionLocation() != null
                ? transactionDTO.getTransactionLocation()
                : generateRandomLocation(scenario);

        String channel = transactionDTO.getChannel() != null
                ? transactionDTO.getChannel()
                : SimulationUtils.generateChannel();

        String deviceId = transactionDTO.getDeviceId() != null
                ? transactionDTO.getDeviceId()
                : SimulationUtils.generateDeviceId();


        transactionDTO.setTransactionAmount(amount);
        transactionDTO.setTransactionLocation(location);
        transactionDTO.setChannel(channel);
        transactionDTO.setDeviceId(deviceId);
        transactionDTO.setSimulated(true);

        Transaction transaction = new Transaction();
        transaction.setUser(loggedInUser);
        transaction.setTransactionAmount(amount);
        transaction.setTransactionLocation(location);
        transaction.setTransactionOn(transactionDTO.getTransactionOn() != null
                ? transactionDTO.getTransactionOn()
                : java.time.LocalDateTime.now());

        transaction.setTransactionStatus(TransactionStatus.PENDING);

        var decision=fraudControlService.fraudControl(transaction);
        transactionDTO.setTransactionDecision(decision);

        if (decision== Decision.BLOCK){
            transaction.setTransactionStatus(TransactionStatus.BLOCKED);
            transaction.setTransactionAmount(null);
            transaction.setTransactionLocation(null);
            transactionRepository.save(transaction);
            transactionDTO.setTransactionId(transaction.getTransactionId());
            transactionDTO.setTransactionStatus(TransactionStatus.BLOCKED);
            transactionDTO.setMessage("Transaction blocked due to high risk.");

            return transactionDTO;
        }

        else if (decision==Decision.ALLOW){
            transaction.setTransactionStatus(TransactionStatus.APPROVED);
            transactionRepository.save(transaction);

            transactionDTO.setTransactionId(transaction.getTransactionId());
            transactionDTO.setTransactionStatus(TransactionStatus.APPROVED);
            transactionDTO.setMessage("Transaction approved successfully.");
            return transactionDTO;

        }
        else if(decision==Decision.STEP_UP){
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            transactionRepository.save(transaction);

            String otpValue = otpService.generateAndSaveOtp(transaction, loggedInUser);
            emailService.sendOtp(loggedInUser.getUserMail(), otpValue);

            transactionDTO.setTransactionId(transaction.getTransactionId());
            transactionDTO.setTransactionStatus(TransactionStatus.PENDING);
            transactionDTO.setTransactionDecision(Decision.STEP_UP);
            transactionDTO.setMessage("OTP sent to email. Verify to complete transaction.");
            return transactionDTO;
        }

        return transactionDTO;
    }

    private BigDecimal generateAmount(SimulationScenario scenario) {
        double amt;
        switch (scenario) {
            case HIGH_AMOUNT:
                amt = 5000 + random.nextDouble() * 15000;
                break;
            case RAPID_FIRE:
                amt = 10 + random.nextDouble() * 100;
                break;
            case OUTLIER:
                amt = 20000 + random.nextDouble() * 50000;
                break;
            case NORMAL:
            default:
                amt = 50 + random.nextDouble() * 450;
        }
        return BigDecimal.valueOf(Math.round(amt * 100) / 100.0);
    }

    private String generateRandomLocation(SimulationScenario scenario) {
        switch (scenario) {
            case OUTLIER:
                return simulationUtils.pickRandomFrom(List.of("New York", "Tokyo", "London"));
            case HIGH_AMOUNT:
                return simulationUtils.pickRandomFrom(List.of("Dubai", "Singapore", "London"));
            case RAPID_FIRE:
            case NORMAL:
            default:
                return simulationUtils.pickRandomFrom(List.of("Chennai", "Bangalore", "Mumbai"));
        }
    }


}