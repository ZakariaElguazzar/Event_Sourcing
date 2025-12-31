package org.example.event_sourcing.query.handlers;


import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.example.event_sourcing.query.dtos.AccountStatementResponseDTO;
import org.example.event_sourcing.query.entities.Account;
import org.example.event_sourcing.query.entities.AccountOperation;
import org.example.event_sourcing.query.queries.GetAccountStatementQuery;
import org.example.event_sourcing.query.queries.GetAllAccountQuery;
import org.example.event_sourcing.query.queries.WatchEventQuery;
import org.example.event_sourcing.query.repositories.AccountOperationRepo;
import org.example.event_sourcing.query.repositories.AccountRepo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AccountQueryHandler {
    private AccountRepo accountRepo;
    private AccountOperationRepo accountOperationRepo;

    @QueryHandler
    public List<Account> on(GetAllAccountQuery query){
        return accountRepo.findAll();
    }

    @QueryHandler
    public AccountStatementResponseDTO on(GetAccountStatementQuery query){
        Account account = accountRepo.findById(query.getAccountId()).orElseThrow();
        List<AccountOperation> operations = accountOperationRepo.findByAccount_AccountId(account.getAccountId());
        return new AccountStatementResponseDTO(account,operations);
    }

    @QueryHandler
    public AccountOperation on(WatchEventQuery query){
        return AccountOperation.builder().build();
    }



}
