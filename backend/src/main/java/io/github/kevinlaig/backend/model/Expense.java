package io.github.kevinlaig.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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

    @Column(nullable = false)
    @NotEmpty(message = "Category is required")
    private String category; // This could be an enum or a separate entity

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(length = 500)
    private String notes;

}
