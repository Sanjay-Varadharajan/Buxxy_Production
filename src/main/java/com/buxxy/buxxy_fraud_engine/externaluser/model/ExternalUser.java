package com.buxxy.buxxy_fraud_engine.externaluser.model;

import com.buxxy.buxxy_fraud_engine.transaction.model.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "external_users",
        indexes = {
                @Index(name = "idx_ext_user_id", columnList = "externalUserId"),
                @Index(name = "idx_ext_user_upi", columnList = "upiId")
        }
)

public class ExternalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eUserId;

    @Column(nullable = false)
    @NotEmpty(message = "external user id required")
    private String externalUserId;

    @Column(nullable = false)
    private String upiId;

    @Column(nullable = false, unique = true)
    private String eUserMail;

    @CreatedDate
    private LocalDateTime firstSeenAt;

    private LocalDateTime lastSeenAt;

    @OneToMany(mappedBy = "externalUser",cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    private List<Transaction> transactionList=new ArrayList<>();

}
