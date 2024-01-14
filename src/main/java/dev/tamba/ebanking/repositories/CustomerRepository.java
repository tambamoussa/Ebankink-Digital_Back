package dev.tamba.ebanking.repositories;

import dev.tamba.ebanking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
