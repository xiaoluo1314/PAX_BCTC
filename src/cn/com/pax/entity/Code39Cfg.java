package cn.com.pax.entity;

/**
 * Code39实体类
 *
 * @author luohl
 * @create 2018-01-08-18:02
 */
public class Code39Cfg{
   private String ratio;
   private String precision;
   private String data;
   private String bgcolor;
   private String fgcolor;
   private String height;
   private String fixationWidth;
   private String unitLength;
   private String brightValue;   //亮度

    public String getBrightValue() {
        return brightValue;
    }

    public void setBrightValue(String brightValue) {
        this.brightValue = brightValue;
    }

    public void setFixationWidth(String fixationWidth) {
        this.fixationWidth = fixationWidth;
    }

    public void setUnitLength(String unitLength) {
        this.unitLength = unitLength;
    }

    public String getFixationWidth() {
        return fixationWidth;
    }

    public String getUnitLength() {
        return unitLength;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Code39Cfg{" +
                "ratio='" + ratio + '\'' +
                ", precision='" + precision + '\'' +
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



    