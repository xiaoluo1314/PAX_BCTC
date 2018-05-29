package cn.com.pax.tree;

import cn.com.pax.Constant;
import cn.com.pax.display.MainFrame;
import cn.com.pax.entity.TableData;
import cn.com.pax.entity.TreeLeaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class CheckBoxTreeNodeSelectionListener extends MouseAdapter
{
	//private Set<TableData> tabelSetSelect = new LinkedHashSet<TableData>();
	public static Set<TableData> tabelSetSelect = new LinkedHashSet<TableData>();
	private JTable table ;
	int id = 0;
	private CheckBoxTreeNode node;

	public CheckBoxTreeNodeSelectionListener(JTable table){
		this.table = table;
	}
	@Override
	public void mouseClicked(MouseEvent event)
	{
		JTree tree = (JTree)event.getSource();
		int x = event.getX();
		int y = event.getY();
		int row = tree.getRowForLocation(x, y);
		TreePath path = tree.getPathForRow(row);

		if(path != null)
		{
			CheckBoxTreeNode node = (CheckBoxTreeNode)path.getLastPathComponent();
			if(node != null )
			{
				boolean isSelected = !node.isSelected();
				node.setSelected(isSelected);
				if (isSelected == true){
					tabelSetSelect = CheckBoxTreeNode.getTabelSet();
					//System.out.println("tabelSet.的值是：---"+ tabelSetSelect + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
					//System.out.println("tabelSet.size()的值是：---"+ tabelSetSelect.size() + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
					//TODO 这个地方遇到很多问题，以解决
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					int size = model.getRowCount();
					//System.out.println("size的值是：---"+ size + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
					if (size == 0){
						//全部添加
						//System.out.println("tabelSetSelect的值是：---"+ tabelSetSelect + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
						for (TableData tableData1 : tabelSetSelect) {
							if(CustTreeModel.getResult(MainFrame.state).contains(CustTreeModel.convertStr(tableData1.getState()))){
								model.addRow(new Object[]{tableData1.isSelect(), tableData1.getLeafName(), "UNTEST", ++id});
							}
						}
					}else{
						//部分添加
						for (TableData tableData : tabelSetSelect) {
							int j = 0;
							for (int i = 0; i < size; i++) {
								//System.out.println("!(model.getValueAt(i,1).toString()).equals(tableData.getLeafName())的值是：---"+ !(model.getValueAt(i,1).toString()).equals(tableData.getLeafName()) + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
								// System.out.println("model.getValueAt(i,1).toString()的值是：---"+ model.getValueAt(i,1).toString() + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
								if(!(model.getValueAt(i,1).toString()).equals(tableData.getLeafName())){
									++j;
									continue;

								}
							}

							if(j == size) {
								//System.out.println("的值是：---"+"xxxxx" + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
								if(CustTreeModel.getResult(MainFrame.state).contains(CustTreeModel.convertStr(tableData.getState()))){
									model.addRow(new Object[]{tableData.isSelect(), tableData.getLeafName(), "UNTEST", ++id});
								}
								//model.fireTableDataChanged();
							}
						}

					}
				}else{
					//暂时不需要
				}
				((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);
				TreeOperation.expandAll(tree, new TreePath(node.getRoot()), true);

				//System.out.println("node.getFirstLeaf()的值是：---"+ node.getFirstLeaf() + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
				//System.out.println("node.getLeafCount()的值是：---"+ node.getLeafCount() + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
				//System.out.println("node.getLastLeaf()的值是：---"+ node.getLastLeaf() + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
				//System.out.println("node.getLevel()的值是：---"+  node.getLevel()+ "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
				//TreeLeaf treeLeaf =(TreeLeaf)node.getUserObject();
				//System.out.println("node的值是：---"+ node + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
				/*TreeLeaf treeLeaf =(TreeLeaf)node.getUserObject();
				while (!node.isLeaf()){
					if("条码测试".equals(treeLeaf.getLeafName())){
						Enumeration enu= node.children();
						while (enu.hasMoreElements()) {

						}
					}
				}*/

				/*System.out.println("node.getLastChild()的值是：---"+  node.getLastChild()+ "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
				TableData tableData = new TableData();
				TreeLeaf treeLeaf = null;
				while (!node.isLeaf()){
					 treeLeaf =(TreeLeaf)node.getUserObject();
					if("条码测试".equals(treeLeaf.getLeafName())){
						node = (CheckBoxTreeNode)node.getNextNode();
						tableData.setLeafName(((TreeLeaf) node.getUserObject()).getLeafName().toString());
						tableData.setSelect(false);
						break;
					}
				}*/

			/*	if (i==1 && j == 0){
					//某一个文件夹
				}else if (i==0 && j ==0){
					// 叶子
					treeLeaf =(TreeLeaf)node.getUserObject();
					TableData tableData = new TableData();
					tableData.setSelect(false);
					tableData.setLeafName(treeLeaf.getLeafName().toString());
				}else{
					//全选
				}*/



				/*TreeLeaf treeLeaf =(TreeLeaf)node.getUserObject();
				String nodeName = treeLeaf.getLeafName().toString();


				//一层一层往下遍历
				System.out.println("node的值是：---"+ node + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
				if (!node.isLeaf()){
					node.getNextNode();
					System.out.println("node.getNextNode()的值是：---"+ node.getNextNode() + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
					Enumeration enu= node.children();
					while (enu.hasMoreElements()) {
						CheckBoxTreeNode n = (CheckBoxTreeNode) enu.nextElement();
						System.out.println("n的值是：---"+ n + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
						if(!n.isLeaf()){
							Enumeration enuc= n.children();
							while (enuc.hasMoreElements()) {
								CheckBoxTreeNode c = (CheckBoxTreeNode) enuc.nextElement();
								System.out.println("c的值是：---"+ c + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
								if (!c.isLeaf()) {
									Enumeration enuc1 = n.children();
									while (enuc1.hasMoreElements()) {
										CheckBoxTreeNode c1 = (CheckBoxTreeNode) enuc.nextElement();
										System.out.println("c1的值是：---" + c1 + "，当前方法=mouseClicked.CheckBoxTreeNodeSelectionListener()");
									}
								}
							}
						}
					}
				}*/
				
			}//end node
		}
	}
	public  void  RecursionNode(CheckBoxTreeNode node){
		//for (int i = model.getRowCount() - 1; i >= 0; i--) {
	//                    model.removeRow(i);
//                }
			if (node.isLeaf()){
				TreeLeaf treeLeaf =(TreeLeaf)node.getUserObject();
				TableData tableData = new TableData();
				tableData.setSelect(false);
				tableData.setLeafName(treeLeaf.getLeafName().toString());
				if (tabelSetSelect.add(tableData)){

					DefaultTableModel model = (DefaultTableModel) table.getModel();
					model.addRow(new Object[]{tableData.isSelect(),tableData.getLeafName(),"" ,++id});
					System.out.println("id的值是：---"+ id + "，当前方法=valueChanged.MyTreeSelectionListener()");
				}

			}else {
				RecursionNode(node);
			}
	}
}
