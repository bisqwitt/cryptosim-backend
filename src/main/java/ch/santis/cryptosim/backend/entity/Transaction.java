package ch.santis.cryptosim.backend.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "transaction",
    indexes = {
            @Index(name = "idx_transaction_portfolio_id", columnList = "portfolioId"),
            @Index(name = "idx_transaction_portfolio_crypto_id", columnList = "portfolioId, cryptoId")
        })
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_seq",
            sequenceName = "transaction_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_seq"
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portfolioId", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String cryptoId;

    @Column(nullable = false)
    private BigDecimal amountUsd;

    @Column(nullable = false)
    private BigDecimal amountCrypto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private LocalDate date;

    public Transaction() {}

    public Transaction(
            Portfolio portfolio,
            String cryptoId,
            BigDecimal amountUsd,
            BigDecimal amountCrypto,
            TransactionType type,
            LocalDate date) {
        this.portfolio = portfolio;
        this.cryptoId = cryptoId;
        this.amountUsd = amountUsd;
        this.amountCrypto = amountCrypto;
        this.type = type;
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getCryptoId() {
        return cryptoId;
    }

    public void setCryptoId(String cryptoId) {
        this.cryptoId = cryptoId;
    }

    public BigDecimal getAmountUsd() {
        return amountUsd;
    }

    public void setAmountUsd(BigDecimal amountUsd) {
        this.amountUsd = amountUsd;
    }

    public BigDecimal getAmountCrypto() {
        return amountCrypto;
    }

    public void setAmountCrypto(BigDecimal amountCrypto) {
        this.amountCrypto = amountCrypto;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
