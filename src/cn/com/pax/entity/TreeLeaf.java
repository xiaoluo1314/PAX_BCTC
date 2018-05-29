package cn.com.pax.entity;

import javax.swing.*;

/**
 * 封装图标与叶子名字类
 *
 * @author luohl
 * @create 2017-12-07-19:11
 */
public class TreeLeaf {
    private String leafName;
    private ImageIcon imageIcon;
    private int state = 0;
    public TreeLeaf(String leafName,ImageIcon imageIcon,int state){
        this.leafName = leafName;
        this.imageIcon = imageIcon;
        this.state = state;
    }
    public String getLeafName() {
        return leafName;
    }

    public void setLeafName(String leafName) {
        this.leafName = leafName;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "TreeLeaf{" +
                "leafName='" + leafName + '\'' +
                ", imageIcon=" + imageIcon +
                '}';
    }
}
    