package dev.tamba.ebanking.web;

import dev.tamba.ebanking.dtos.AccountOperationDTO;
import dev.tamba.ebanking.dtos.BankAccountDTO;
import dev.tamba.ebanking.exceptions.BankAccountNotFoundExeption;
import dev.tamba.ebanking.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestAPI {

  private BankAccountService bankAccountService;

  @GetMapping("/accounts{accountId}")
  public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundExeption {
      return bankAccountService.getBankAccount(accountId);
  }
@GetMapping("/accounts")
  public List<BankAccountDTO>listAccounts(){
      return bankAccountService.bankAcountList();
  }
/*
  @GetMapping("/accounts/{accountId}/operations")
  public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
  return bankAccountService.accountHistory(accountId);
  }*/
}
