package cn.com.pax.utils;

import javax.sound.midi.Soundbank;
import java.util.Calendar;

/**
 * 枚举类
 *
 * @author luohl
 * @create 2018-01-04-9:49
 */

public enum ColorUtils {
    RED(0),WHITE(1),BLACK(2),BLUE(4),OTHE(5);

    private ColorUtils(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    private int color;

    public static void main(String[] args) {
       // ColorUtils.values();
        System.out.println(ColorUtils.WHITE.getColor());
        ColorUtils colorUtils = ColorUtils.WHITE;
        switch (colorUtils){
            case RED:
                System.out.println("xxxx");
                break;
            case WHITE:
                System.out.println("11111");
                break;
            case BLUE:
                System.out.println("xxwwewe");
                break;
        }
    }
}
