package com.rajasreeit.backend.entities.crmEmployeeEntities;

import com.rajasreeit.backend.entities.CrmEmployee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "employee_leaves")
public class CrmLeaves {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private CrmEmployee crmEmployee;

    private Date startDate;

    private Date endDate;

    private String reason;

    private String leaveDay;

    private String leaveType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeavesEnum leavesEnum = LeavesEnum.PENDING;
}
