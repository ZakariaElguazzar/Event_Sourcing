package org.example.event_sourcing.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountCreditedEvent {
    private String accountId;
    private double amount;
}
