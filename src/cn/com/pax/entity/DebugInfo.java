package cn.com.pax.entity;

/**
 * 调试相关的信息
 *
 * @author luohl
 * @create 2017-12-26-9:50
 */
public class DebugInfo {
    private  String moveNum ;
    private  String moveSpeed;
    private  String rotateType;
    private  String rotateAngle;
    private  String rotate360;

    public String getMoveNum() {
        return moveNum;
    }

    public void setMoveNum(String moveNum) {
        this.moveNum = moveNum;
    }

    public String getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(String moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public String getRotateType() {
        return rotateType;
    }

    public void setRotateType(String rotateType) {
        this.rotateType = rotateType;
    }

    public String getRotateAngle() {
        return rotateAngle;
    }

    public void setRotateAngle(String rotateAngle) {
        this.rotateAngle = rotateAngle;
    }

    public String getRotate360() {
        return rotate360;
    }

    public void setRotate360(String rotate360) {
        this.rotate360 = rotate360;
    }

    @Override
    public String toString() {
        return "DebugInfo{" +
                "moveNum='" + moveNum + '\'' +
                ", moveSpeed='" + moveSpeed + '\'' +
                ", rotateType='" + rotateType + '\'' +
                ", rotateAngle='" + rotateAngle + '\'' +
                ", rotate360='" + rotate360 + '\'' +
                '}';
    }
}
    