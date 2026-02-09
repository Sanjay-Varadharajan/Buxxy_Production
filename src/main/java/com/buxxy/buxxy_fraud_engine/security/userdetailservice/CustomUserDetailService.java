package com.buxxy.buxxy_fraud_engine.security.userdetailservice;


import com.buxxy.buxxy_fraud_engine.admin.model.Admin;
import com.buxxy.buxxy_fraud_engine.admin.repository.AdminRepository;
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
    AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String userMail) throws UsernameNotFoundException {
        Admin user=adminRepository.findByUserMailAndUserActiveTrue(userMail)
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
