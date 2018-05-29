package cn.com.pax.report;

import java.util.List;

public class TemplateCls {
    private int id;
    private String typeName;
    private String colName;
    private List<ResultInfo> childList;

    public TemplateCls(int id, String typeName, String colName, List<ResultInfo> childList) {
        this.id = id;
        this.typeName = typeName;
        this.colName = colName;
        this.childList = childList;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getColName() {
        return colName;
    }

    public List<ResultInfo> getChildList() {
        return childList;
    }
}
