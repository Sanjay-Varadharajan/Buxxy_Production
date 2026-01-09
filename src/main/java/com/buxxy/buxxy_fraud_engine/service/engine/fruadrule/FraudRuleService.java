package com.buxxy.buxxy_fraud_engine.service.engine.fruadrule;


import com.buxxy.buxxy_fraud_engine.dto.fraudrules.FraudRuleCreateDTO;
import com.buxxy.buxxy_fraud_engine.dto.fraudrules.FraudRuleResponseDTO;
import com.buxxy.buxxy_fraud_engine.model.FraudRules;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.FraudRuleRepository;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class FraudRuleService {

    private final FraudRuleRepository fraudRuleRepository;

    private final UserRepository userRepository;




    public FraudRuleCreateDTO createRule(Principal principal, @Valid FraudRuleCreateDTO fraudRule) {

        User loggedInAdmin=userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(()->
                        new UsernameNotFoundException(principal.getName()+" Not Found,Login and Try"));

        FraudRules fraudRules=new FraudRules();
        fraudRules.setRuleDescription(fraudRule.getRuleDescription());
        fraudRules.setThreshold(fraudRule.getRuleThreshold());
        fraudRules.setRuleType(fraudRule.getRuleType());
        fraudRules.setRuleUpdatedOn(LocalDateTime.now());

        fraudRuleRepository.save(fraudRules);

        return new FraudRuleCreateDTO(fraudRules);
    }

    @Transactional(readOnly = true)
    public Page<FraudRuleResponseDTO> viewRules(Principal principal, Pageable pageable) {

        User loggedInAdmin=userRepository.findByUserMailAndUserActiveTrue(
                principal.getName()
        ).orElseThrow(()->new UsernameNotFoundException(principal.getName()+" Not Found,Login and Try"));

        Set<String> allowed=Set.of("ruleAddedOn");

        pageable.getSort().forEach(order ->{
                if(!allowed.contains(order.getProperty())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid sort field: " + order.getProperty());
            }
        });

        Page<FraudRules> fraudRules=fraudRuleRepository.findAll(pageable);

        return fraudRules.map(FraudRuleResponseDTO::new);
    }


    public void deActivateRule(Principal principal, long ruleId) {
        User loggedInAdmin=userRepository.findByUserMailAndUserActiveTrue(
                principal.getName()
        ).orElseThrow(()->new UsernameNotFoundException(principal.getName()+" Not Found,Login and Try"));

        FraudRules fraudRules=fraudRuleRepository
                .findById(ruleId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,ruleId+" does not Exists"));

        fraudRules.setActive(false);
        fraudRules.setRuleUpdatedOn(LocalDateTime.now());

        fraudRuleRepository.save(fraudRules);
    }

    public FraudRuleResponseDTO activateRule(Principal principal, long ruleId) {
        User loggedInAdmin = userRepository.findByUserMailAndUserActiveTrue(
                principal.getName()
        ).orElseThrow(() -> new UsernameNotFoundException(principal.getName() + " Not Found,Login and Try"));

        FraudRules fraudRules = fraudRuleRepository
                .findById(ruleId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ruleId + " does not Exists"));

        fraudRules.setActive(true);
        fraudRules.setRuleUpdatedOn(LocalDateTime.now());

     FraudRules rules=fraudRuleRepository.save(fraudRules);
     return new FraudRuleResponseDTO(rules);
    }
}
