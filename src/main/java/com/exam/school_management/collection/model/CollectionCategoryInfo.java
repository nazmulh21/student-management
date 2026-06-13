package com.exam.school_management.collection.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Table(name = "collection_category_info")
@Entity
@Data
public class CollectionCategoryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "category-fees")
    private BigDecimal categoryFees;

    public CollectionCategoryInfo() {
    }

    public CollectionCategoryInfo(Long id) {
        this.id = id;
    }
}
