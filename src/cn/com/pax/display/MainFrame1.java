package cn.com.pax.display;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * UI显示类
 *
 * @author luohl
 * @create 2017-12-04-16:30
 */
public class MainFrame1 extends JFrame {
    public MainFrame1(){
        setSize(1000,800);
        //JPanel panel = new JPanel();
        String[]s1 = {"青菜","大蒜","大葱"};
        String []s2 = {"苹果", "梨子","香蕉"};
        String[]s3 = {"馒头","包子","饺子","混沌","面条"};
        Hashtable hashtable1 = new Hashtable();
        Hashtable hashtable2 = new Hashtable();
        hashtable1.put("蔬菜",s1);
        hashtable1.put("水果",s2);
        hashtable1.put("点心",hashtable2);
        hashtable2.put("中心",s3);
        Font font = new Font("Dialog",Font.PLAIN,12);
        Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
        while (keys.hasMoreElements()){
            Object key = keys.nextElement();
            if (UIManager.get(key) instanceof  Font){
                UIManager.put(key,font);
            }
        }
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        JTree tree = new JTree(hashtable1);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(tree);
        add(scrollPane);


    }
}
