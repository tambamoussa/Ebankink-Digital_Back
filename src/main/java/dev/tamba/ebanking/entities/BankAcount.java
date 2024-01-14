package dev.tamba.ebanking.entities;

import dev.tamba.ebanking.Enums.AcountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data @NoArgsConstructor @AllArgsConstructor
public abstract class BankAcount {
    @Id
    private  String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AcountStatus status;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAcount", fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperations;
}
