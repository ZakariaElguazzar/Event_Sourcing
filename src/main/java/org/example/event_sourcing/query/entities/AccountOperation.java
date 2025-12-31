package org.example.event_sourcing.query.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.event_sourcing.enums.OperationType;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountOperation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant operationDate;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    private String currency;
    @ManyToOne
    private Account account;
}
