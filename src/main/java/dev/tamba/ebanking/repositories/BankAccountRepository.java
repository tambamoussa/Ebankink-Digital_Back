package dev.tamba.ebanking.repositories;

import dev.tamba.ebanking.entities.BankAcount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAcount, String> {
}
