package com.exam.school_management.personnel.model;

import com.exam.school_management.blood_group.model.BloodInfo;
import com.exam.school_management.designation.model.DesignationInfo;
import com.exam.school_management.district.model.DistrictInfo;
import com.exam.school_management.gender.model.GenderInfo;
import com.exam.school_management.job_status.model.JobStatusInfo;
import com.exam.school_management.subjects.model.SubjectInfo;
import com.exam.school_management.thana.model.ThanaInfo;
import com.exam.school_management.union.model.UnionInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Table(name = "personnel_info")
@Data
@Entity
public class PersonnelInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index")
    private String index;

    @Column(name = "pds_id")
    private Long pdsId;

    @Column(name = "name")
    private String name;

    @Column(name = "img_name")
    private String imageName;

    @Column(name = "nid")
    private String nid;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "father")
    private String father;

    @Column(name = "mother")
    private String mother;

    @Column(name = "signature")
    private String signatureName;

    @Column(name = "is_teacher")
    private Boolean isTeacher;


    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "join_date")
    private LocalDate joinDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column(name = "dob")
    private LocalDate dob;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "blood_id")
    private BloodInfo bloodInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gender_id")
    private GenderInfo genderInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "designation_id")
    private DesignationInfo designationInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private SubjectInfo subjectInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    private DistrictInfo districtInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "thana_id")
    private ThanaInfo thanaInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "union_id")
    private UnionInfo unionInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_status_id")
    private JobStatusInfo jobStatusInfo;

    @Column(name = "village")
    private String village;


    public PersonnelInfo() {
    }

    public PersonnelInfo(Long id) {
        this.id = id;
    }
}