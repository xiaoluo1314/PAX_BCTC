package cn.com.pax.display;

import cn.com.pax.Constant;
import cn.com.pax.utils.GBC;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 软件信息
 *
 * @author luohl
 * @create 2017-12-20-16:42
 */
public class AboutSoft extends JDialog {
   public AboutSoft(){
       setTitle("软件信息");
       setLayout(new GridBagLayout());
       setModal(true);
       setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
       JLabel jpgLabel = new JLabel(UIUtils.createImageIcon(Constant.PAX));
       JLabel titleLabel = new JLabel(Constant.titleStr);
       JLabel verLabel = new JLabel("Version: " + Constant.versionStr);
       JLabel buildLabel = new JLabel("Build Id: " + Constant.BuildStr);
       titleLabel.setFont(UIUtils.setFont(14));
       verLabel.setFont(UIUtils.setFont(14));
       buildLabel.setFont(UIUtils.setFont(14));
       add(jpgLabel, new GBC(0, 0, 1, 3).setWeight(100, 100).setFill(GBC.BOTH));
       add(titleLabel, new GBC(1, 0, 1, 1).setWeight(100, 100).setAnchor(GBC.WEST));
       add(verLabel, new GBC(1, 1, 1, 1).setWeight(100, 100).setAnchor(GBC.WEST));
       add(buildLabel, new GBC(1, 2, 1, 1).setWeight(100, 100).setAnchor(GBC.WEST));
       setSize(Constant.SOFTWIDTH, Constant.SOFTHEIGHT);
       setLocationRelativeTo(null);
       setResizable(false);
       setVisible(true);
   }
}
    