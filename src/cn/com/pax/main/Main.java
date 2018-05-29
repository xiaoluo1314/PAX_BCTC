package cn.com.pax.main;

import cn.com.pax.display.MainFrame;
import cn.com.pax.display.Project;

import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 *
 * @author luohl
 * @create 2017-12-04-16:26
 */
public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
           // JFrame frame = new MainFrame();
            JFrame frame = new Project();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //frame.pack();
        });
    }
}
    