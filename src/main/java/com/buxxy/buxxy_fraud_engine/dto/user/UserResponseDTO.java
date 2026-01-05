package com.buxxy.buxxy_fraud_engine.dto.user;

import com.buxxy.buxxy_fraud_engine.enums.Role;
import com.buxxy.buxxy_fraud_engine.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private Long userId;
    private String userName;
    private String userMail;
    private Role userRole;

    public UserResponseDTO(User user) {
        this.userId=user.getUserId();
        this.userName=user.getUserName();
        this.userMail=user.getUserMail();
        this.userRole=user.getUserRole();
    }
}
