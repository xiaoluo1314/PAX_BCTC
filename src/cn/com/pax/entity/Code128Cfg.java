package cn.com.pax.entity;

/**
 * Code128实体类
 *
 * @author luohl
 * @create 2018-01-09-9:58
 */
public class Code128Cfg {
    private String precision;
    private String data;
    private String bgcolor;
    private String fgcolor;
    private String height; //默认16

    private String fixationWidth;
    private String unitLength;
    private String brightValue;   //亮度

    public String getBrightValue() {
        return brightValue;
    }

    public void setBrightValue(String brightValue) {
        this.brightValue = brightValue;
    }

    public String getFixationWidth() {
        return fixationWidth;
    }

    public void setFixationWidth(String fixationWidth) {
        this.fixationWidth = fixationWidth;
    }

    public String getUnitLength() {
        return unitLength;
    }

    public void setUnitLength(String unitLength) {
        this.unitLength = unitLength;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public String getFgcolor() {
        return fgcolor;
    }

    public void setFgcolor(String fgcolor) {
        this.fgcolor = fgcolor;
    }

    @Override
    public String toString() {
        return "Code128Cfg{" +
                "precision='" + precision + '\'' +
                ", data='" + data + '\'' +
                ", bgcolor='" + bgcolor + '\'' +
                ", fgcolor='" + fgcolor + '\'' +
                ", height='" + height + '\'' +
                ", fixationWidth='" + fixationWidth + '\'' +
                ", unitLength='" + unitLength + '\'' +
                ", brightValue='" + brightValue + '\'' +
                '}';
    }
}
    