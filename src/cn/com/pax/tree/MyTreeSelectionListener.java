package cn.com.pax.tree;

import cn.com.pax.entity.TableData;
import cn.com.pax.entity.TreeLeaf;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashSet;
import java.util.Set;

/**
 * 树选择监听事件
 *
 * @author luohl
 * @create 2017-12-07-16:33
 */
public class MyTreeSelectionListener implements TreeSelectionListener {
     private Set<TableData> tabelSet = new HashSet<TableData>();
     private JTable table ;
     int id = 0;
     public  MyTreeSelectionListener(JTable table){
         this.table = table;
     }

    @Override
        public void valueChanged(TreeSelectionEvent e) {
        JTree tree =(JTree) e.getSource();
        DefaultMutableTreeNode selectionNode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
        //防止意外，多加一层判断
        if(selectionNode != null && selectionNode.getUserObject() instanceof TreeLeaf){
            TreeLeaf treeLeaf =(TreeLeaf)selectionNode.getUserObject();
            String nodeName = treeLeaf.getLeafName().toString();
            if(selectionNode.isLeaf()) {//如果被选中的节点是叶子节点，则更新表格数据，否则，不更新。
                //System.getProperty("user.dir")取得目前的工作路径
                //System.getProperty("file.separator")取得当前系统默认的文件分隔符
                TableData tableData = new TableData();
                tableData.setSelect(false);
                tableData.setLeafName(nodeName);
                //Todo 这么写会出意外，一旦单击叶子，表格就会从新更新数据，如果测试了，那么状态就变了。
//                DefaultTableModel model = (DefaultTableModel) table.getModel();
//                for (int i = model.getRowCount() - 1; i >= 0; i--) {
//                    model.removeRow(i);
//                }
//                for (TableData tableData1 : tabelSet) {
//                    model.addRow(new Object[]{tableData1.isSelect(),tableData1.getLeafName(),"" ,++id});
//                }
                // Todo 这样保证
                if (tabelSet.add(tableData)){
                   DefaultTableModel model = (DefaultTableModel) table.getModel();
                    model.addRow(new Object[]{tableData.isSelect(),tableData.getLeafName(),"" ,++id});
                    System.out.println("id的值是：---"+ id + "，当前方法=valueChanged.MyTreeSelectionListener()");
                }
                //System.out.println("tabelSet：---"+ tabelSet + "，当前方法=valueChanged.myTreeSelectionListener()");
            }
        }

    }
}
    