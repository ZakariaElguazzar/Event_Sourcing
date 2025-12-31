package org.example.event_sourcing.commands.dto;



public record CreateAccountRequest (
        String accountHolderName,
        double initialBalance,
        String currency
) {
}
