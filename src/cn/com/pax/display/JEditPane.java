package cn.com.pax.display;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * ceshi
 *
 * @author luohl
 * @create 2018-01-05-15:01
 */

public class JEditPane {
        public static void main(String[] args) {
            JEditorPane editPane  = new JEditorPane();//构造一个空的JEditorPane
//            try {
//                File file = new File("src/SDCS_BCTC_Code39-2-2018-01-04-20-10-44.html");
//                String str = file.getAbsolutePath();//取得文件位置的绝对路径
//                str = "file:" + str;//将绝对路径合成一完整的输入字符串
//                editPane.setPage(str);
//            } catch (IOException ioe) {
//                ioe.printStackTrace(System.err);
//                System.exit(0);
//            }
            //setup HTMLEditorKit
            editPane.setContentType("text/html");
            HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
            editPane.setEditorKit(htmlEditorKit);
            Document doc = editPane.getDocument();
            //htmlEditorKit = (HTMLEditorKit) editPane.getEditorKit();
            String htmlText =" <p style=\"color:808080\">特征:C4</p>";
            try {
                    doc.remove(0,doc.getLength());
                    htmlEditorKit.insertHTML((HTMLDocument) doc, doc.getLength(),"<p style=\"color:000000\">[==============    Test Start    ==============]</p>",0,0,null);
                    htmlEditorKit.insertHTML((HTMLDocument) doc, doc.getLength(), htmlText, 0, 0, null);
                    htmlEditorKit.insertHTML((HTMLDocument) doc, doc.getLength(),"<p style=\"color:808080\">数据:1366247825630124983</p>",0,0,null);
                    htmlEditorKit.insertHTML((HTMLDocument) doc, doc.getLength()," <p style=\"color:FF0000\">扫码验证失败:扫码成功，但返回数据错误</p>",0,0,null);
                    //editPane.setText(""); //都可以
                    // doc.remove(0,doc.getLength());
                    htmlEditorKit.insertHTML((HTMLDocument) doc, doc.getLength()," <p style=\"color:FF0000\">扫码验证失败:扫码成功，但返回数据错误</p>",0,0,null);
                    // doc = editPane.getDocument();
                    htmlEditorKit.insertHTML((HTMLDocument) doc, doc.getLength(),"<p style=\"color:000000\">[==============    Test End    ==============]</p>",0,0,null);


            } catch (BadLocationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // editPane.setText(" <p style=\"color:808080\">特征:C4</p>");
           // System.out.println(editPane.getText().trim());

            //builder.append("<p style=\"color:808080\">数据:1366247825630124983</p>");
           // editPane.setText(builder.toString());

            editPane.setEditable(false);
            JFrame f = new JFrame("JEditorPaneTest");
            f.setContentPane(new JScrollPane(editPane));
            f.setSize(600, 600);
            f.show();
            f.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        }
    }



//public class JEditPane {
//    public JComponent makeUI() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<html><body>aaaaaaaaaaaaaaaaaaaaaa<br />");
//        sb.append("<img width='144' height='120' src='https://lh5.googleusercontent.com/_9Z4BYR88imo/TQTOK8UtUUI/AAAAAAAAAb8/yiME-hTTlWA/s800/HyperlinkListener.png' />");
//        sb.append("</body></html>");
//        JEditorPane editorPane = new JEditorPane();
//        editorPane.setEditable(false);
//        editorPane.setContentType("text/html");
//        editorPane.setText(sb.toString());
//        JPanel p = new JPanel(new BorderLayout());
//        p.add(new JScrollPane(editorPane));
//        return p;
//    }
//    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            @Override public void run() {
//                createAndShowGUI();
//            }
//        });
//    }
//    public static void createAndShowGUI() {
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        f.getContentPane().add(new JEditPane().makeUI());
//        f.setSize(320, 240);
//        f.setLocationRelativeTo(null);
//        f.setVisible(true);
//    }
//}
    