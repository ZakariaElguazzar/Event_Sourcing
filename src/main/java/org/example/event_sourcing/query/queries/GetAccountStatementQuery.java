package org.example.event_sourcing.query.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class GetAccountStatementQuery {
    private String accountId;

}
