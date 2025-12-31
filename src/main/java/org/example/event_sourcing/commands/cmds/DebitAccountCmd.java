package org.example.event_sourcing.commands.cmds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Getter
@AllArgsConstructor
public class DebitAccountCmd {
    @TargetAggregateIdentifier
    private String id;
    private double amount;
}
