package org.example.event_sourcing.commands.cmds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Getter
@AllArgsConstructor
public class AddAccountCmd {
    @TargetAggregateIdentifier
    private String id;
    private double initialBalance;
    private String currency;
}
