package ch.santis.cryptosim.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;

@Entity
@Table(
        name = "portfolio",
        check = {
                @CheckConstraint(
                        name = "chk_portfolio_credit_positive",
                        constraint = "credit >= 0"
                )
        })
public class Portfolio {
    @Id
    @SequenceGenerator(
            name = "portfolio_seq",
            sequenceName = "portfolio_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "portfolio_seq"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal credit;

    public Portfolio() {}

    public Portfolio(String name, BigDecimal credit) {
        this.name = name;
        this.credit = credit;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }
}
