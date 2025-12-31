package org.example.event_sourcing.query.repositories;

import org.example.event_sourcing.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account,String> {
}
