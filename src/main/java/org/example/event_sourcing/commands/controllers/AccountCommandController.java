package org.example.event_sourcing.commands.controllers;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.example.event_sourcing.commands.dto.CreateAccountRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands/accounts")
@AllArgsConstructor
public class AccountCommandController {
    private CommandGateway commandGateway;
    // Command handling methods will be implemented here
    @PostMapping("/create")
    public String createAccount(@RequestBody CreateAccountRequest request) {
        // Logic to handle account creation command
        commandGateway.send();
        return "Account created";
    }

}
