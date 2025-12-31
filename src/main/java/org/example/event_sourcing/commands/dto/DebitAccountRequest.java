package org.example.event_sourcing.commands.dto;

public record DebitAccountRequest(
        String accountId,
        double amount
) {
}
