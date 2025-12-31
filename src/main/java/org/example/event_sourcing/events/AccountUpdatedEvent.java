package org.example.event_sourcing.events;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.event_sourcing.enums.AccountStatus;

@Getter
@AllArgsConstructor
public class AccountUpdatedEvent{
    private String accountId;
    private AccountStatus previousStatus;
    private AccountStatus currentStatus;
}
