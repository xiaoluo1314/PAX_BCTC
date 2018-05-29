package cn.com.pax.entity;

/**
 * 填充表格数据
 *
 * @author luohl
 * @create 2017-12-07-17:01
 */
public class TableData {
    private boolean isSelect = false;
    private String leafName ;

    private int state ;

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getLeafName() {
        return leafName;
    }

    public void setLeafName(String leafName) {
        this.leafName = leafName;
    }


    /**
     * 如果对象类型是TableData 的话 则返回true 去比较hashCode值
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(this == obj) return true;
        if(obj instanceof TableData){
            TableData data =(TableData)obj;
            // leafName 一致时才返回true 之后再去比较 hashCode
            if(data.leafName.equals(this.leafName)) return true;
        }
        return false;
    }

    /**
     * 重写hashcode 方法，返回的hashCode 不一样才认定为不同的对象
     */
    @Override
    public int hashCode() {
 // 只比较leafName，leafName一样就不添加进集合
        return  leafName.hashCode();
    }

    @Override
    public String toString() {
        return "TableData{" +
                "isSelect=" + isSelect +
                ", leafName='" + leafName + '\'' +
                ", state=" + state +
                '}';
    }
}
    