package io.github.kevinlaig.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Expense entity.
 */
@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Min(value = 0, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(length = 500)
    private String notes;

    /**
     * Constructor for creating an expense without specifying an ID.
     *
     * @param user The user that the expense belongs to.
     * @param amount The amount of the expense.
     * @param category The category of the expense.
     * @param dateTime The date and time of the expense.
     * @param notes The notes of the expense.
     */
    public Expense(User user, BigDecimal amount, Category category, LocalDateTime dateTime, String notes) {
        this.user = user;
        this.amount = amount;
        this.category = category;
        this.dateTime = dateTime;
        this.notes = notes;
    }
}
