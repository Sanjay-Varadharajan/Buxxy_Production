package com.buxxy.buxxy_fraud_engine.dto.user;

import com.buxxy.buxxy_fraud_engine.model.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @NotBlank(message = "UserName is Needed")
    @Column(nullable = false)
    private String userName;

    public UserUpdateDto(User user) {
        this.userName=user.getUserName();
    }
}
