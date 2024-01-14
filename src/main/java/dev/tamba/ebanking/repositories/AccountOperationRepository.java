package dev.tamba.ebanking.repositories;

import dev.tamba.ebanking.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;


public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {


       //  List<AccountOperation> findByBankAccountId( String accountId);

}
