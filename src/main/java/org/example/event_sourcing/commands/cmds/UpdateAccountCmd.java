package org.example.event_sourcing.commands.cmds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.example.event_sourcing.enums.AccountStatus;


@Getter
@AllArgsConstructor
public class UpdateAccountCmd {
    @TargetAggregateIdentifier
    private String id;
    private AccountStatus status;
}
