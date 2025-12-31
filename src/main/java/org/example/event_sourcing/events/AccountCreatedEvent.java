package org.example.event_sourcing.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.event_sourcing.enums.AccountStatus;

@AllArgsConstructor @Getter
public class AccountCreatedEvent {
    private String accountId;

    private double initialBalance;
    private String accountHolderName;
    private String currency;
    private AccountStatus status;
}
