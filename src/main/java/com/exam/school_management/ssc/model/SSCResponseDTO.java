package com.exam.school_management.ssc.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class SSCResponseDTO {
    String studentName;
    Date dob;
    String fathersName;
    String mothersName;
    Long groupId;
    String sscYear;

}
