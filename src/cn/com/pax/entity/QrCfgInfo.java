package cn.com.pax.entity;

/**
 * QR配置信息
 *
 * @author luohl
 * @create 2017-12-23-16:19
 */
public class QrCfgInfo {

    private String version;        //版本:2   		 //第六行 版本
    private String errLevel;       //容错:L
    private String precision;      //精度:                （xxx mil）
    private String generateData;   //数据:
    //////////////////////以下可选//////////////////////////////////
    private String unitLength;
    private String charSet;        //charSet字符集
    private String foreground;     //前景色:
    private String background;     //背景色:
    private String brightValue;   //亮度

    public String getBrightValue() {
        return brightValue;
    }

    public void setBrightValue(String brightValue) {
        this.brightValue = brightValue;
    }

    public String getUnitLength() {
        return unitLength;
    }

    public void setUnitLength(String unitLength) {
        this.unitLength = unitLength;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getErrLevel() {
        return errLevel;
    }

    public void setErrLevel(String errLevel) {
        this.errLevel = errLevel;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public String getGenerateData() {
        return generateData;
    }

    public void setGenerateData(String generateData) {
        this.generateData = generateData;
    }
    /////////////////////////////////////////////////////////////////////////

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getForeground() {
        return foreground;
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public String toString() {
        return "QrCfgInfo{" +
                "version='" + version + '\'' +
                ", errLevel='" + errLevel + '\'' +
                ", precision='" + precision + '\'' +
                ", generateData='" + generateData + '\'' +
                ", unitLength='" + unitLength + '\'' +
                ", charSet='" + charSet + '\'' +
                ", foreground='" + foreground + '\'' +
                ", background='" + background + '\'' +
                ", brightValue='" + brightValue + '\'' +
                '}';
    }
}

    