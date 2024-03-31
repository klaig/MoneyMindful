package io.github.kevinlaig.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

// Represents the monthly budget set by the user, including fields for total budget amount
// and tracking of the budget categories.
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection
    private Map<String, BigDecimal> categoryLimits;

}