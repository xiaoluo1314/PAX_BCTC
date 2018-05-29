package cn.com.pax.tree;

import cn.com.pax.Constant;
import cn.com.pax.entity.TreeLeaf;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * 改变叶子节点的状态图标
 *
 * @author luohl
 * @create 2017-12-06-20:09
 */
public class MyCellRenderer extends DefaultTreeCellRenderer {
    private Icon icon;
    public MyCellRenderer() {
        super();
    }
    //先留着
    @Override
    public void setLeafIcon(Icon icon) {
       //this.icon = icon;
       super.setLeafIcon(icon);
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value,sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
       if ((node.getUserObject() instanceof TreeLeaf)){
          TreeLeaf treeLeaf =(TreeLeaf)node.getUserObject();
//           if(leaf){
//               if (treeLeaf.getState() == 0){
//                   setIcon(UIUtils.createImageIcon(Constant.UNTEST));
//               }else if(treeLeaf.getState() == 1){
//                   setIcon(UIUtils.createImageIcon(Constant.PASS));
//               }else{
//                   setIcon(UIUtils.createImageIcon(Constant.FAIL));
//               }
//           }
        }
        return this;
    }
}
    