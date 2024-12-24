package com.rajasreeit.backend.entities.crmEmployeeEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LeaveWithEmployeeName {

    private int id;
    private Date startDate;
    private Date endDate;
    private String reason;
    private LeavesEnum leavesEnum;
    private String leaveDay;
    private String leaveType;
    private String employeeName;

}
