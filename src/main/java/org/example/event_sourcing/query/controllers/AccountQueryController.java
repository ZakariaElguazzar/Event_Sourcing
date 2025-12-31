package org.example.event_sourcing.query.controllers;


import lombok.AllArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.example.event_sourcing.query.dtos.AccountStatementResponseDTO;
import org.example.event_sourcing.query.entities.Account;
import org.example.event_sourcing.query.entities.AccountOperation;
import org.example.event_sourcing.query.queries.GetAccountStatementQuery;
import org.example.event_sourcing.query.queries.GetAllAccountQuery;
import org.example.event_sourcing.query.queries.WatchEventQuery;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/accounts")
@AllArgsConstructor
public class AccountQueryController {
    private QueryGateway queryGateway;

    @GetMapping("/all")
    public CompletableFuture<List<Account>> getAllAccounts(){
        return queryGateway.query(new GetAllAccountQuery(), ResponseTypes.multipleInstancesOf(Account.class));
    }

    @GetMapping("/accountsstatement/{accountid}")
    public CompletableFuture<AccountStatementResponseDTO> getAccountStatement(@PathVariable String accountid){
        return queryGateway.query(new GetAccountStatementQuery(accountid), ResponseTypes.instanceOf(AccountStatementResponseDTO.class));

    }

    @GetMapping(value = "/watch/{accountId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountOperation> watch(@PathVariable String accountId){
        SubscriptionQueryResult<AccountOperation, AccountOperation> result = queryGateway.subscriptionQuery(new WatchEventQuery(accountId),
                ResponseTypes.instanceOf(AccountOperation.class),
                ResponseTypes.instanceOf(AccountOperation.class)
        );
        return result.initialResult().concatWith(result.updates());
    }




}
