package com.buxxy.buxxy_fraud_engine.idempotency.model;

import com.buxxy.buxxy_fraud_engine.idempotency.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "idempotency_records",
uniqueConstraints = @UniqueConstraint(columnNames = "idempotencyKey"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdempotencyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idempotencyId;

    private String idempotencyKey;

    private String requestHash;

    @Lob //used log so if suppose response does not fit in varchar it will be handled well in db
    private String response;

    @Enumerated(EnumType.STRING)
    private IdempotencyStatus status;
}
