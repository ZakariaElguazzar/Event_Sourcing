package org.example.event_sourcing.commands.dto;

import org.example.event_sourcing.enums.AccountStatus;

public record UpdateAccountRequest(
        String accountId,
        AccountStatus status
) {
}
