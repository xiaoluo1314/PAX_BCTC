package cn.com.pax.tree;

import cn.com.pax.Constant;
import cn.com.pax.entity.TreeLeaf;
import cn.com.pax.tree.CheckBoxTreeNode;
import cn.com.pax.tree.TreeOperation;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.List;

/**
 * 更新测试结果图标node
 *
 * @author luohl
 * @create 2017-12-09-16:01
 */
public class UpdateTestResult {
    public static  void updateTestResult(JTree jTree ,String str, int state){  // 1UNTEST  2 FAIL  4 PASS
//        TreeNode node =(TreeNode)jTree.getModel().getRoot();
//        System.out.println("node的值是：---"+ node + "，当前方法=updateTestResult.UpdateTestResult()");
//        TreePath tp  =jTree.getPathForRow(3);
//       System.out.println(" tp.getPathCount()的值是：---"+  tp.getPathCount() + "，当前方法=updateTestResult.UpdateTestResult()");
//        CheckBoxTreeNode  checkBoxTreeNode =(CheckBoxTreeNode)tp.getLastPathComponent();
//
//        System.out.println("checkBoxTreeNode.getNextLeaf().getNextLeaf()的值是：---"+ checkBoxTreeNode.getNextLeaf().getNextLeaf() + "，当前方法=updateTestResult.UpdateTestResult()");
//        System.out.println("checkBoxTreeNode.getLeafCount()的值是：---"+ checkBoxTreeNode.getLeafCount() + "，当前方法=updateTestResult.UpdateTestResult()");
//        System.out.println("checkBoxTreeNode.getNextLeaf()的值是：---"+ checkBoxTreeNode.getNextLeaf() + "，当前方法=updateTestResult.UpdateTestResult()");
//        System.out.println("checkBoxTreeNode的值是：---"+ checkBoxTreeNode + "，当前方法=updateTestResult.UpdateTestResult()");
//        System.out.println("jTree.getRowCount()的值是：---"+ jTree.getRowCount() + "，当前方法=updateTestResult.UpdateTestResult()");
//        System.out.println("checkBoxTreeNode.getChildCount()的值是：---"+ checkBoxTreeNode.getChildCount() + "，当前方法=updateTestResult.UpdateTestResult()");
//        System.out.println("checkBoxTreeNode.getNextNode()的值是：---"+ checkBoxTreeNode.getNextNode() + "，当前方法=updateTestResult.UpdateTestResult()");
        List<CheckBoxTreeNode> checkBoxTreeNode1 = TreeOperation.getSaveList();
        for (CheckBoxTreeNode boxTreeNode : checkBoxTreeNode1) {
            TreeLeaf  treeLeaf =(TreeLeaf)boxTreeNode.getUserObject();
            if(treeLeaf.getLeafName().equals(str)) {
                ((TreeLeaf) boxTreeNode.getUserObject()).setState(state);
                ((DefaultTreeModel)jTree.getModel()).nodeStructureChanged(boxTreeNode);
                break;
            }
        }
    }
}
    