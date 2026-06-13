package com.exam.school_management.others_bill.model;

import com.exam.school_management.collection.model.CollectionCategoryInfo;
import com.exam.school_management.students.model.StudentInfo;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "others_bill_info")
@Entity
@Data
public class OthersBillInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "stu_id")
    private StudentInfo studentInfo;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "collection_id")
    private CollectionCategoryInfo collectionCategoryInfo;

    @Column(name="others_bill")
    private BigDecimal othersBill;

    @Column(name="paid_bill")
    private BigDecimal paidBill;

    @Column(name="create_date")
    private Date ceateDate;

    @Column(name="create_by")
    private Long createBy;

}
