package com.buxxy.buxxy_fraud_engine.admin.dto;

import com.buxxy.buxxy_fraud_engine.security.role.Role;
import com.buxxy.buxxy_fraud_engine.admin.model.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminResponseDTO {
    private Long userId;
    private String userName;
    private String userMail;
    private Role userRole;



    public AdminResponseDTO(Admin admin) {
        this.userId=admin.getUserId();
        this.userMail=admin.getUserMail();
        this.userName=admin.getUserName();
        this.userRole=admin.getUserRole();
    }
}
