package org.example.event_sourcing.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountDebitedEvent {
    private String accountId;
    private double amount;
}
