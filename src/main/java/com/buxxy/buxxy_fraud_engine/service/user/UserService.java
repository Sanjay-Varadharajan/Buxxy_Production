package com.buxxy.buxxy_fraud_engine.service.user;

import com.buxxy.buxxy_fraud_engine.dto.user.UserResponseDTO;
import com.buxxy.buxxy_fraud_engine.dto.user.UserUpdateDto;
import com.buxxy.buxxy_fraud_engine.exceptions.UserNotFoundException;
import com.buxxy.buxxy_fraud_engine.model.User;
import com.buxxy.buxxy_fraud_engine.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    public UserResponseDTO viewProfile(Principal principal) {
        User loggedInUser = userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName() + " not found,Login and try"));

        return new UserResponseDTO(loggedInUser);
    }

    public UserUpdateDto updateProfile(
                                                      UserUpdateDto userUpdateDto,
                                                       Principal principal) {

        User loggedInUser=userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(()->new UsernameNotFoundException(principal.getName()+" not Found,Login and try"));

        loggedInUser.setUserName(userUpdateDto.getUserName());
        User updatedUser= userRepository.save(loggedInUser);
        return new UserUpdateDto(updatedUser);
    }

    public void deActivate(Principal principal) {
        User user=userRepository
                .findByUserMailAndUserActiveTrue(principal.getName())
                .orElseThrow(()-> new UserNotFoundException(
                        principal.getName()+" Not Found,Login and try")
                );

        user.setUserActive(false);
        userRepository.save(user);

    }
}
