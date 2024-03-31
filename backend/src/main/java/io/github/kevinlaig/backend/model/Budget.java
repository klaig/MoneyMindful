package io.github.kevinlaig.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

// Represents the monthly budget set by the user, including fields for total budget amount
// and tracking of the budget categories.
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "budget_limits", joinColumns = @JoinColumn(name = "budget_id"))
    @MapKeyColumn(name = "category")
    @Column(name = "limit")
    private Map<String, BigDecimal> categoryLimits;

}