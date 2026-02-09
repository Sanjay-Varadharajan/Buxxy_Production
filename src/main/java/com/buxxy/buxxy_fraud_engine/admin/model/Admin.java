    package com.buxxy.buxxy_fraud_engine.admin.model;

    import com.buxxy.buxxy_fraud_engine.security.role.Role;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotEmpty;
    import jakarta.validation.constraints.Size;
    import lombok.*;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;

    import java.time.LocalDateTime;

    @Table(
            name = "users",
            indexes = {
                    @Index(name = "idx_user_mail", columnList = "userMail"),
                    @Index(name = "idx_user_role",columnList = "userRole")
            }
    )
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EntityListeners(AuditingEntityListener.class)
    public class Admin {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long userId;

        @Column(nullable = false, unique = true, updatable = false)
        private String externalUserId;


        @NotBlank(message = "UserName is Needed")
        @Column(nullable = false)
        private String userName;

        @NotBlank(message = "UserMail is Needed")
        @Column(nullable = false,unique = true)
        @Email(message = "Invalid Email")
        private String userMail;

        @NotEmpty(message = "Password is Needed")
        @Size(min = 6,message = "Password Must have At least 6 characters")
        @Column(nullable = false)
        @JsonIgnore
        private String userPassword;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private Role userRole;

        @CreatedDate
        @Column(nullable = false)
        private LocalDateTime userCreatedOn;

        private boolean userActive=true;




        @PrePersist
        public void prePersist() {
            if (userCreatedOn == null) {
                this.userCreatedOn = LocalDateTime.now();
            }
        }
    }
