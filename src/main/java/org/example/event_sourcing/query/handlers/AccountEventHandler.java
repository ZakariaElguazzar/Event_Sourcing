package org.example.event_sourcing.query.handlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.example.event_sourcing.enums.OperationType;
import org.example.event_sourcing.events.AccountCreatedEvent;
import org.example.event_sourcing.events.AccountCreditedEvent;
import org.example.event_sourcing.events.AccountDebitedEvent;
import org.example.event_sourcing.events.AccountUpdatedEvent;
import org.example.event_sourcing.query.entities.Account;
import org.example.event_sourcing.query.entities.AccountOperation;
import org.example.event_sourcing.query.repositories.AccountOperationRepo;
import org.example.event_sourcing.query.repositories.AccountRepo;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class AccountEventHandler {
    private AccountRepo accountRepo;
    private AccountOperationRepo accountOperationRepo;
    private QueryUpdateEmitter queryUpdateEmitter;

    @EventHandler
    public void on(AccountCreatedEvent event, EventMessage eventMessage){
        log.info("=============== Query Side AccountCreatedEvent received ========================");
        Account account = Account.builder()
                .accountId(event.getAccountId())
                .accountHolderName(event.getAccountHolderName())
                .balance(event.getInitialBalance())
                .currency(event.getCurrency())
                .status(event.getStatus())
                .createdAt(eventMessage.getTimestamp())
                .build();
        accountRepo.save(account);
    }


    @EventHandler
    public void on(AccountUpdatedEvent event){
        log.info("=============== Query Side AccountUpdatedEvent received ========================");
        Account account = accountRepo.findById(event.getAccountId()).orElseThrow();
        account.setStatus(event.getCurrentStatus());
        accountRepo.save(account);

    }


    @EventHandler
    public void on(AccountCreditedEvent event, EventMessage eventMessage){
        log.info("=============== Query Side AccountCreditedEvent received ========================");
        Account account = accountRepo.findById(event.getAccountId()).orElseThrow();
        AccountOperation accountOperation = AccountOperation.builder()
                .operationType(OperationType.CREDIT)
                .amount(event.getAmount())
                .operationDate(eventMessage.getTimestamp())
                .currency(account.getCurrency())
                .account(account)
                .build();
        accountOperationRepo.save(accountOperation);
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepo.save(account);
        queryUpdateEmitter.emit(e->true,accountOperation);

    }

    @EventHandler
    public void on(AccountDebitedEvent event, EventMessage eventMessage){
        log.info("=============== Query Side AccountDebitedEvent received ========================");
        Account account = accountRepo.findById(event.getAccountId()).orElseThrow();
        AccountOperation accountOperation = AccountOperation.builder()
                .operationType(OperationType.DEBIT)
                .amount(event.getAmount())
                .operationDate(eventMessage.getTimestamp())
                .currency(account.getCurrency())
                .account(account)
                .build();
        accountOperationRepo.save(accountOperation);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepo.save(account);
        queryUpdateEmitter.emit(e->true,accountOperation);

    }
}
