package org.example.event_sourcing.query.repositories;

import org.example.event_sourcing.query.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepo extends JpaRepository<AccountOperation,Long> {
    List<AccountOperation> findByAccount_AccountId(String accountId);
}
