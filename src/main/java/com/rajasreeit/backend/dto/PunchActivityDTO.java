package com.rajasreeit.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PunchActivityDTO {

    private String date;
    private boolean punchInImagePresent;
    private boolean punchOutImagePresent;
    private String remainderDate;
    private String workReport;
    private String punchInTime;
    private String punchOutTime;

    public PunchActivityDTO(String date, boolean punchInImagePresent, boolean punchOutImagePresent, String remainderDate, String workReport, String punchInTime, String punchOutTime) {
        this.date = date;
        this.punchInImagePresent = punchInImagePresent;
        this.punchOutImagePresent = punchOutImagePresent;
        this.remainderDate = remainderDate;
        this.workReport = workReport;
        this.punchInTime = punchInTime;
        this.punchOutTime = punchOutTime;
    }

}