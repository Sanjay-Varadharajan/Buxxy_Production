package com.buxxy.buxxy_fraud_engine.dto.user;

import com.buxxy.buxxy_fraud_engine.enums.Role;
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
    private String name;
    private String email;
    private Role role;
}
