package com.exam.school_management.transaction_history.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "transaction_history_info")
@Data
public class TransactionHistoryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "collection_by")
    private String collectionBy;

    @Column(name = "receipt_id")
    private String receiptId;

    @Column(name = "collection_date")
    private Date collectionDate;

}
