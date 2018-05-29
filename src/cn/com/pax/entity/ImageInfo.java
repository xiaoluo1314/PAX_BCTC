package cn.com.pax.entity;

import java.util.Map;

/**
 * 图片信息类
 *
 * @author luohl
 * @create 2017-12-23-16:21
 */
public class ImageInfo {
    private String imgName ;
    private String content;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgName() {
        return imgName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "imgName='" + imgName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
    