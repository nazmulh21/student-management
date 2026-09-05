package com.exam.school_management.dashboard;

import com.exam.school_management.expense.expense_vouchar.model.ExpenseInfo;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.routine.main_routine.model.SubstituteInfo;
import com.exam.school_management.students.model.StudentInfo;
import lombok.Data;

import java.util.List;

@Data
public class DashboardDTO {
    private List<LeaveRequestInfo> sentBackRequests;
    private List<LeaveRequestInfo> leaveRequests;
    private List<PersonnelInfo> personnelList;
    private List<StudentInfo> allActiveStudents;
    private List<ExpenseInfo> allPendingExpenses;
    private List<SubstituteInfo> allPendingGapClass;


}
