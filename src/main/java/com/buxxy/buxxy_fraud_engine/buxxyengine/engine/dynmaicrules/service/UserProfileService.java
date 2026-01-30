    package com.buxxy.buxxy_fraud_engine.buxxyengine.engine.dynmaicrules.service;


    import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.dynmaicrules.model.UserRuleProfile;
    import com.buxxy.buxxy_fraud_engine.buxxyengine.engine.dynmaicrules.repository.UserRuleProfileRepository;
    import com.buxxy.buxxy_fraud_engine.dto.fraudrules.FraudRuleDtoForEngine;
    import com.buxxy.buxxy_fraud_engine.model.FraudRules;
    import com.buxxy.buxxy_fraud_engine.model.User;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.ArrayList;


    @Service
    @RequiredArgsConstructor
    public class UserProfileService {

        private final UserRuleProfileRepository userRuleProfileRepository;

        public UserRuleProfile getOrCreateProfile(User user, FraudRuleDtoForEngine fraudRules) {
            return userRuleProfileRepository.findByUserIdAndRuleId(user.getUserId(), fraudRules.getRuleId())
                    .orElseGet(() -> {
                        UserRuleProfile userRuleProfile = new UserRuleProfile();
                        userRuleProfile.setUserId(user.getUserId());
                        userRuleProfile.setRuleId(fraudRules.getRuleId());
                        userRuleProfile.setAvgAmount(BigDecimal.ZERO);
                        userRuleProfile.setTxCountPerHour(0);
                        userRuleProfile.setLastTxTime(LocalDateTime.now());
                        userRuleProfile.setMultiplier(BigDecimal.valueOf(3));
                        userRuleProfile.setDynamicThreshold(0);
                        return userRuleProfileRepository.save(userRuleProfile);
                    });
        }


        public void updatedAverageAmount(UserRuleProfile userRuleProfile,BigDecimal txAmount,int windowSize){
            BigDecimal preAvg=userRuleProfile.getAvgAmount();
            int n=windowSize;
            BigDecimal newAvg=(preAvg.multiply(BigDecimal.valueOf(n-1))
                    .add(txAmount)
                    .divide(BigDecimal.valueOf(n),2,BigDecimal.ROUND_HALF_UP));
            userRuleProfile.setAvgAmount(newAvg);
            userRuleProfile.setDynamicThreshold(newAvg.multiply(userRuleProfile.getMultiplier()).intValue());
            userRuleProfile.setUpdatedOn(LocalDateTime.now());
            userRuleProfileRepository.save(userRuleProfile);
        }


        public void updatedTxCount(UserRuleProfile userRuleProfile,int count){
            userRuleProfile.setTxCountPerHour(count);
            int dynamicLimit = userRuleProfile.getTxCountPerHour() * userRuleProfile.getMultiplier().intValue();
            userRuleProfile.setDynamicThreshold(dynamicLimit);
            userRuleProfile.setUpdatedOn(LocalDateTime.now());
            userRuleProfileRepository.save(userRuleProfile);
        }

        public void updateUsual(UserRuleProfile profile,String country,String city){
            if (profile.getUsualTransactionCountries()==null){
                profile.setUsualTransactionCountries(new ArrayList<>());
            }
            if (profile.getUsualTransactionCities()==null){
                profile.setUsualTransactionCities(new ArrayList<>());
            }

            if(!profile.getUsualTransactionCountries().stream()
                    .anyMatch(c->c.equalsIgnoreCase(country))){
                profile.getUsualTransactionCountries().add(country);
            }
            if (!profile.getUsualTransactionCities().stream()
                    .anyMatch(c -> c.equalsIgnoreCase(city))) {
                profile.getUsualTransactionCities().add(city);
            }

            profile.setUpdatedOn(LocalDateTime.now());
            userRuleProfileRepository.save(profile);
        }
    }
