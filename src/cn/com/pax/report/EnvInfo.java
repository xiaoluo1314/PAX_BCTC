package cn.com.pax.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EnvInfo {
    private String reportDate;
    private String tester;
    private String place;
    private String env;
    private String oem;
    private String posType;
    private String remark;

    public EnvInfo(String tester, String place, String env, String oem, String posType, String remark) {
        this.tester = tester;
        this.place = place;
        this.env = env;
        this.oem = oem;
        this.posType = posType;
        this.remark = remark;
        this.reportDate = dateToString(new Date());
    }

    public static String dateToString(Date date) {
        String str = null;
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        str = format.format(date);
        return str;
    }

    public String getReportDate() {
        return reportDate;
    }

    public String getTester() {
        return tester;
    }

    public String getPlace() {
        return place;
    }

    public String getEnv() {
        return env;
    }

    public String getOem() {
        return oem;
    }

    public String getPosType() {
        return posType;
    }

    public String getRemark() {
        return remark;
    }
}
