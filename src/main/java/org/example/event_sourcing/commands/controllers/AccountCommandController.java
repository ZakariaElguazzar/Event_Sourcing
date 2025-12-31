package org.example.event_sourcing.commands.controllers;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.example.event_sourcing.commands.cmds.AddAccountCmd;
import org.example.event_sourcing.commands.cmds.CreditAccountCmd;
import org.example.event_sourcing.commands.cmds.DebitAccountCmd;
import org.example.event_sourcing.commands.cmds.UpdateAccountCmd;
import org.example.event_sourcing.commands.dto.CreateAccountRequest;
import org.example.event_sourcing.commands.dto.CreditAccountRequest;
import org.example.event_sourcing.commands.dto.DebitAccountRequest;
import org.example.event_sourcing.commands.dto.UpdateAccountRequest;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/accounts")
@AllArgsConstructor
public class AccountCommandController {
    private CommandGateway commandGateway;
    private EventStore eventStore;
    // Command handling methods will be implemented here
    @PostMapping("/create")
    public CompletableFuture <String> createAccount(@RequestBody CreateAccountRequest request) {
        // Logic to handle account creation command
        AddAccountCmd cmd = new AddAccountCmd(
                java.util.UUID.randomUUID().toString(),
                request.accountHolderName(),
                request.initialBalance(),
                request.currency()
        );
        return commandGateway.send(cmd);
    }

    @PostMapping("/credit")
    public CompletableFuture <String> creditAccount(@RequestBody CreditAccountRequest request) {
        // Logic to handle account creation command
        CreditAccountCmd cmd = new CreditAccountCmd(
                request.accountId(),
                request.amount()
        );
        return commandGateway.send(cmd);
    }

    @PostMapping("/debit")
    public CompletableFuture <String> debitAccount(@RequestBody DebitAccountRequest request) {
        // Logic to handle account creation command
        DebitAccountCmd cmd = new DebitAccountCmd(
                request.accountId(),
                request.amount()
        );
        return commandGateway.send(cmd);
    }

    @PutMapping("/update")
    public CompletableFuture <String> update(@RequestBody UpdateAccountRequest request) {
        // Logic to handle account creation command
        UpdateAccountCmd cmd = new UpdateAccountCmd(
                request.accountId(),
                request.status()
        );
        return commandGateway.send(cmd);
    }



    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        return e.getMessage();
    }

    @GetMapping("/events/{accountId}")
    public Stream getAccountEvents(@PathVariable String accountId) {
        return eventStore.readEvents(accountId).asStream();
    }

}
