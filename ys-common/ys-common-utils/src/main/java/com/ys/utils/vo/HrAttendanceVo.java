package com.ys.utils.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class HrAttendanceVo {
    /** USER Name */
//    @Excel(name = "username")
//    private String nickName;
    /** CONTACT NUMBER */
    @Excel(name = "jobNumber")
    private String jobnumber;

    @Excel(name = "checkIn" ,format = "HH:mm:ss", importFormat = "HH:mm:ss")
    private Date checkIn;

    @Excel(name = "checkOut" ,format = "HH:mm:ss", importFormat = "HH:mm:ss")
    private Date checkOut;

    @Excel(name = "attendanceTime" ,format = "yyyy-MM-dd")
    private Date attendanceTime;
}
