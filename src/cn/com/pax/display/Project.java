package cn.com.pax.display;

import cn.com.pax.Constant;
import cn.com.pax.utils.GBC;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 开始启动一个工程
 *
 * @author luohl
 * @create 2017-12-05-19:40
 */
public class Project extends JFrame implements ActionListener{
    private JButton newProject;
    private  JButton openProject;
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(newProject)){
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            new NewProject().setVisible(true);
            Project.this.setVisible(false);
        }else if (e.getSource().equals(openProject)){
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            new OpenProject().setVisible(true);
            Project.this.setVisible(false);
        }
    }

    public Project(){
        super("工程项目");
        setIconImage(UIUtils.getLogo(this,Constant.LOGO));
        UIUtils.ChangeWindowDisplay();
        setLayout(new GridBagLayout());
        setSize(Constant.WIDTH1,Constant.HEIGHT1);
        //JPanel jPanel = new JPanel(new GridBagLayout());

        newProject = new JButton("新建项目");
        openProject  = new JButton( "打开项目");
        newProject.setFont( UIUtils.setFont(24));
        openProject.setFont( UIUtils.setFont(24));
        setLocationRelativeTo(null);

        newProject.addActionListener(this);
        openProject.addActionListener(this);

        add(newProject, new GBC(0, 0).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.CENTER));
        add(openProject, new GBC(0, 2).setInsets(5).
                setWeight(100, 100).setAnchor(GBC.CENTER));

    }


}
    