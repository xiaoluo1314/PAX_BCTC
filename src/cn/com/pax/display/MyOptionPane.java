package cn.com.pax.display;

import javax.swing.*;
import java.awt.*;

/**
 * 提示信息框
 *
 * @author luohl
 * @create 2017-12-12-10:53
 */
public class MyOptionPane {
    public static void showMessage(Component parentComponent , String message, String title){
        JOptionPane.showMessageDialog(parentComponent,message,title,JOptionPane.WARNING_MESSAGE);
    }
}
    