package org.example.event_sourcing.query.dtos;

import org.example.event_sourcing.query.entities.Account;
import org.example.event_sourcing.query.entities.AccountOperation;

import java.util.List;

public record AccountStatementResponseDTO(
        Account account,
        List<AccountOperation> accountOperations
) {
}
