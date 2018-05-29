package cn.com.pax.report;

public class ResultInfo {
    private String caseName;
    private String caseResult;
    private String caseDetail;

    public ResultInfo(String caseName, String caseResult, String caseDetail) {
        this.caseName = caseName;
        this.caseResult = caseResult;
        this.caseDetail = caseDetail;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getCaseResult() {
        return caseResult;
    }

    public String getCaseDetail() {
        return caseDetail;
    }
}
