package io.github.kevinlaig.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Category entity.
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Constructor for creating a category without specifying an ID.
     *
     * @param user The user that the category belongs to.
     * @param name The name of the category.
     */
    public Category(User user, String name) {
        this.user = user;
        this.name = name;
    }
}
