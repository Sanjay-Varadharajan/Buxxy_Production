package com.buxxy.buxxy_fraud_engine.dto.user;

import com.buxxy.buxxy_fraud_engine.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleUpdateDTO {
    @NotNull
    private Long userId;

    @NotNull
    private Role role;
}
