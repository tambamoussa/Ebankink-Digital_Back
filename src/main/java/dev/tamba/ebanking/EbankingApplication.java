package dev.tamba.ebanking;

import dev.tamba.ebanking.Enums.AcountStatus;
import dev.tamba.ebanking.Enums.OperationType;
import dev.tamba.ebanking.dtos.BankAccountDTO;
import dev.tamba.ebanking.dtos.CurrentBankAcountDTO;
import dev.tamba.ebanking.dtos.CustomerDTO;
import dev.tamba.ebanking.dtos.SavingBankAcountDTO;
import dev.tamba.ebanking.entities.*;
import dev.tamba.ebanking.exceptions.BalanceNotSufficientException;
import dev.tamba.ebanking.exceptions.BankAccountNotFoundExeption;
import dev.tamba.ebanking.exceptions.CustomerNotFoundException;
import dev.tamba.ebanking.repositories.AccountOperationRepository;
import dev.tamba.ebanking.repositories.BankAccountRepository;
import dev.tamba.ebanking.repositories.CustomerRepository;
import dev.tamba.ebanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication

public class EbankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
		return args -> {
			Stream.of("Moussa","Fatou","Mohamed").forEach(name->{
				CustomerDTO customer=new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customer);
			});
			bankAccountService.listCustomers().forEach(customer->{
				try {
					bankAccountService.saveCurrentBankAcount(Math.random()*90000,9000,customer.getId());
					bankAccountService.saveSavingBankAcount(Math.random()*120000,5.5,customer.getId());

				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			});
			List<BankAccountDTO> bankAccounts = bankAccountService.bankAcountList();
			for (BankAccountDTO bankAccount:bankAccounts){
				for (int i = 0; i <10 ; i++) {
					String accountId;
					if(bankAccount instanceof SavingBankAcountDTO){
						accountId=((SavingBankAcountDTO) bankAccount).getId();
					} else{
						accountId=((CurrentBankAcountDTO) bankAccount).getId();
					}
					bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
					bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
				}
			}
		};
	}

	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository){

    return args -> {
		Stream.of("Moussa Tamba","Daouda Diop","Fatoumata Ndiaye").forEach(name->{
			Customer customer= new Customer();
			customer.setName(name);
			customer.setEmail(name+"@gmail.com");
			customerRepository.save(customer);
		});
		    customerRepository.findAll().forEach(cust->{
				CurrentAcount currentAcount = new CurrentAcount();
				currentAcount.setId(UUID.randomUUID().toString());
				currentAcount.setBalance(Math.random()*90000);
				currentAcount.setCreatedAt(new Date());
				currentAcount.setStatus(AcountStatus.CREATED);
				currentAcount.setCustomer(cust);
				currentAcount.setOverDraft(150000);
				bankAccountRepository.save(currentAcount);


			    SavingAcount savingAcount = new SavingAcount();
				savingAcount.setId(UUID.randomUUID().toString());
				savingAcount.setBalance(Math.random()*90000);
				savingAcount.setCreatedAt(new Date());
				savingAcount.setStatus(AcountStatus.CREATED);
				savingAcount.setCustomer(cust);
				savingAcount.setInterestRate(3.5);
				bankAccountRepository.save(savingAcount);
		});
			 bankAccountRepository.findAll().forEach(acc->{
				for (int i = 0; i < 10; i++) {
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setOperationDate(new Date());
					accountOperation.setAmount(Math.random()*120000);
					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT : OperationType.CREDIT);
					accountOperation.setBankAcount(acc);
					accountOperationRepository.save(accountOperation);

				}
			});

	  };
	}
}
