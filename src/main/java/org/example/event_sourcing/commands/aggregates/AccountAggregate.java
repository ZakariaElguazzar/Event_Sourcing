package org.example.event_sourcing.commands.aggregates;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.event_sourcing.commands.cmds.AddAccountCmd;
import org.example.event_sourcing.commands.cmds.CreditAccountCmd;
import org.example.event_sourcing.commands.cmds.DebitAccountCmd;
import org.example.event_sourcing.commands.cmds.UpdateAccountCmd;
import org.example.event_sourcing.enums.AccountStatus;
import org.example.event_sourcing.events.AccountCreatedEvent;
import org.example.event_sourcing.events.AccountCreditedEvent;
import org.example.event_sourcing.events.AccountDebitedEvent;
import org.example.event_sourcing.events.AccountUpdatedEvent;

@NoArgsConstructor
@Aggregate
@Slf4j
@Getter
@Setter
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private String accountHolderName;
    private double balance;
    private AccountStatus status;

    @CommandHandler
    public AccountAggregate(AddAccountCmd cmd) {
        log.info("###################### Received AddAccountCmd {} ######################", cmd);
        if (cmd.getInitialBalance() <= 0) { throw new IllegalArgumentException("Initial Balance must be greater than zero"); }
        AggregateLifecycle.apply(
                new AccountCreatedEvent(
                        cmd.getId(),
                        cmd.getInitialBalance(),
                        cmd.getAccountHolderName(),
                        cmd.getCurrency(),
                        AccountStatus.CREATED
                )
        );
    }

    @CommandHandler
    public void handleCommand(CreditAccountCmd cmd){
        log.info("CreditAccountCommand Command Received");
        if (!this.getStatus().equals(AccountStatus.ACTIVATED)) throw  new RuntimeException("This account can not be debited because of the account is not activated. The current status is "+status);
        AggregateLifecycle.apply(new AccountCreditedEvent(
                cmd.getId(),
                cmd.getAmount()
        ));
    }

    @CommandHandler
    public void handleCommand(DebitAccountCmd cmd){
        log.info("DebitAccountCommand Command Received");
        if (!this.getStatus().equals(AccountStatus.ACTIVATED)) throw  new RuntimeException("This account can not be debited because of the account is not activated. The current status is "+status);
        if (this.getBalance() < cmd.getAmount()) throw  new RuntimeException("Insufficient balance in the account");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                cmd.getId(),
                cmd.getAmount()
        ));
    }

    @CommandHandler
    public void handleCommand(UpdateAccountCmd cmd){
        log.info("UpdateAccountCommand Command Received");
        if (this.status.equals(cmd.getStatus())) throw new IllegalArgumentException("The account is already in status "+this.status);
        AggregateLifecycle.apply(
                new AccountUpdatedEvent(
                        cmd.getId(),
                        this.status,
                        cmd.getStatus()
                ));
    }

    @EventSourcingHandler
    public  void on(AccountCreatedEvent event) {
        log.info("###################### AccountCreatedEvent occurred {} ######################", event);
        this.accountId = event.getAccountId();
        this.accountHolderName=event.getAccountHolderName();
        this.balance = event.getInitialBalance();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        log.info("###################### AccountCreditedEvent occurred {} ######################", event);
        this.balance = this.balance + event.getAmount();
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        log.info("###################### AccountDebitedEvent occurred {} ######################", event);
        this.balance = this.balance - event.getAmount();
    }

    @EventSourcingHandler
    public void on(AccountUpdatedEvent event){
        log.info("###################### AccountUpdatedEvent occurred {} ######################", event);
        this.status=event.getCurrentStatus();
    }




}
