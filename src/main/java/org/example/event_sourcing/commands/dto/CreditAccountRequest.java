package org.example.event_sourcing.commands.dto;

public record CreditAccountRequest(
        String accountId,
        double amount
) {
}
