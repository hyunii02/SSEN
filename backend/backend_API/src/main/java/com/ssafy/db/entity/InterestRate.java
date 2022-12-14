package com.ssafy.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * 금리
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "interest_rate")
public class InterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid", columnDefinition = "int unsigned")
    private Long uid;
    @Column(name = "rate", nullable = false)
    @ColumnDefault("0.00")
    private double rate;
    @Column(name = "regdate", nullable = false)
    private LocalDate regdate;

    // currency_categories
    @OneToOne
    @JoinColumn(nullable = false)
    private CurrencyCategory currencyCategory;

}
