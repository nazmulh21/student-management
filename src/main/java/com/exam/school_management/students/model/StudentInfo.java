package com.exam.school_management.students.model;

import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.classes.model.ClassInfo;
import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.group.model.GroupInfo;
import com.exam.school_management.scholarship.model.ScholarshipInfo;
import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.union.model.UnionInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "student_info")
@Entity
@Data
public class StudentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stu_unique_id")
    private String stuUniqueId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "stu_name")
    private String studentName;

    @Column(name = "dob")
    private Date stuDOB;

    @Column(name = "father")
    private String father;

    @Column(name = "f_nid")
    private String fatherNID;


    @Column(name = "mother")
    private String mother;

    @Column(name = "m_nid")
    private String motherNID;

    @Column(name = "mobile")
    private String mobile;


    @Column(name = "roll_no")
    private Long roll;

    @Column(name = "academic_year")
    private Long  academicYear;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "class_id")
    private ClassInfo classInfo;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "blood_id")
    private BloodInfo bloodInfo;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "group_id")
    private GroupInfo groupInfo;


    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "district_id")
    private DistrictInfo districtInfo;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "thana_id")
    private ThanaInfo thanaInfo;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "union_id")
    private UnionInfo unionInfo;

    @ManyToOne(fetch = FetchType.EAGER) // <-- Changed to EAGER
    @JoinColumn(name = "scholarship_id")
    private ScholarshipInfo scholarshipInfo;



    @Column(name = "village")
    private String village;

    @Column(name = "board_reg_no", length = 30)
    private String boardRegNo;

    @Column(name = "birth_reg_no", length = 30)
    private String birthRegNo;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "guardian_mobile")
    private String guardianMobile;

    @Column(name = "guardian_address")
    private String guardianAddress;

    @Column(name = "tuition_fees_facilaties")
    private BigDecimal tuitionFeesFacilities;

    @Column(name = "ins_by")
    private String insBy;

    @Column(name = "ins_date")
    private Date insDate;

    @Column(name = "is_active")
    private Boolean isActive;


    public StudentInfo() {
    }

    public StudentInfo(Long id) {
        this.id = id;
    }
}
