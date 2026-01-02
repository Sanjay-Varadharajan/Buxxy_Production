    package com.buxxy.buxxy_fraud_engine.model;

    import com.buxxy.buxxy_fraud_engine.enums.Role;
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
    import java.util.ArrayList;
    import java.util.List;

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
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long userId;

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


        @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
        private List<Transaction> transactionList=new ArrayList<>();

        @PrePersist
        public void prePersist() {
            if (userCreatedOn == null) {
                this.userCreatedOn = LocalDateTime.now();
            }
        }
    }
