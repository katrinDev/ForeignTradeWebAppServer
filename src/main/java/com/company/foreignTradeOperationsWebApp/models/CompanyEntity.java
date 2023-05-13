package com.company.foreignTradeOperationsWebApp.models;

import com.company.foreignTradeOperationsWebApp.models.enums.TradeTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "company", schema = "foreign-trade-operations")
@NoArgsConstructor
public class CompanyEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Basic
    @Column( nullable = false, length = 45)
    private String companyName;

    @Basic
    @Column(nullable = false, length = 45)
    private String country;

    @Basic
    @Column( nullable = false, length = 45)
    private String checkingAccount;

    @Basic
    @Column( nullable = true, length = 45)
    private String companyEmail;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="trade_type_id")
    private TradeTypeEntity companyType;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private transient List<TradeOperationEntity> operations;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyEntity company = (CompanyEntity) o;
        return Objects.equals(companyId, company.companyId) && Objects.equals(companyName, company.companyName) && Objects.equals(country, company.country) && Objects.equals(checkingAccount, company.checkingAccount) && Objects.equals(companyEmail, company.companyEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyId, companyName, country, checkingAccount, companyEmail);
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", country='" + country + '\'' +
                ", checkingAccount='" + checkingAccount + '\'' +
                ", companyEmail='" + companyEmail + '\'' +
                ", operations=" + operations +
                '}';
    }
}
