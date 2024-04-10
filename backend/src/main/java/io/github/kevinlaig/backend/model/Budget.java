package io.github.kevinlaig.backend.model;

import jakarta.persistence.*;
import java.util.Set;

import lombok.*;

/**
 * Budget entity.
 */
@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"budgetLimits"}) // Preventing recursion
@ToString(exclude = {"budgetLimits"})
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "budget", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BudgetLimit> budgetLimits;

}