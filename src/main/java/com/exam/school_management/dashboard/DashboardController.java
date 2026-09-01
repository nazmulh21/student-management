package com.exam.school_management.dashboard;

import com.exam.school_management.expense.expense_vouchar.model.ExpenseInfo;
import com.exam.school_management.expense.expense_vouchar.service.ExpenseService;
import com.exam.school_management.leave_management.model.LeaveRequestInfo;
import com.exam.school_management.leave_management.service.LeaveManagementService;
import com.exam.school_management.personnel.model.PersonnelInfo;
import com.exam.school_management.personnel.service.PersonnelService;
import com.exam.school_management.students.model.StudentInfo;
import com.exam.school_management.students.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final LeaveManagementService leaveManagementService;
    private final PersonnelService personnelService;
    private final StudentService studentService;
    private final ExpenseService expenseService;

    public DashboardController(LeaveManagementService leaveManagementService, PersonnelService personnelService, StudentService studentService, ExpenseService expenseService) {
        this.leaveManagementService = leaveManagementService;
        this.personnelService = personnelService;
        this.studentService = studentService;
        this.expenseService = expenseService;
    }


    @GetMapping("/summary/{personnelId}")
    public ResponseEntity<DashboardDTO>getDashboardSummary(@PathVariable Long personnelId){
        DashboardDTO dashboardDTO=new DashboardDTO();
        long year = Year.now().getValue();
        List<LeaveRequestInfo> sentBackList = leaveManagementService.getSentbackList(personnelId);
        List<LeaveRequestInfo> leaveRequests = leaveManagementService.getPendingLeaveRequest(personnelId);
        List<PersonnelInfo> personnelList=personnelService.getPersonnelList();
        List<StudentInfo> allStudent=studentService.getAllActiveStudent(year);
        List<ExpenseInfo> getAllPendingExpenseList=expenseService.getPendingList();
        dashboardDTO.setSentBackRequests(sentBackList);
        dashboardDTO.setLeaveRequests(leaveRequests);
        dashboardDTO.setPersonnelList(personnelList);
        dashboardDTO.setAllActiveStudents(allStudent);
        dashboardDTO.setAllPendingExpenses(getAllPendingExpenseList);
       return ResponseEntity.ok(dashboardDTO);

    }
}
