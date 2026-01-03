package com.buxxy.buxxy_fraud_engine.userdetailservice;

import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userMail) throws UsernameNotFoundException {
        User user=userRepository.findByUserMailAndUserActiveTrue(userMail)
                .orElse(null);

        if(user!=null){
            return new CustomUserDetails(
                    user,
                    user.getUserMail(),
                    user.getUserPassword(),
                    user.isUserActive(),
                    List.of(new SimpleGrantedAuthority(user.getUserRole().name()))
            );
        }
        throw new UsernameNotFoundException("User not found with email: " + userMail);
    }
}
