package com.exam.school_management.collection.model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "collection_info")
@Entity
@Data
public class CollectionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "trasaction_id")
    private String transactionId;



}
