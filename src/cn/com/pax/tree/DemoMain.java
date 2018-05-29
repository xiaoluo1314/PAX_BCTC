package cn.com.pax.tree;

import cn.com.pax.Constant;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class DemoMain extends JFrame
{
	private JTree tree;
	public DemoMain(){
		super("CheckBoxTreeDemo");
		UIUtils.ChangeWindowDisplay();
		setLayout(new BorderLayout(5,5));
		setBounds(200, 200, 400, 400);
		tree = new JTree();
	/*	CheckBoxTreeNode rootNode = new CheckBoxTreeNode("root");
		CheckBoxTreeNode node1 = new CheckBoxTreeNode("node_1");
		CheckBoxTreeNode node1_1 = new CheckBoxTreeNode("node_1_1");
		CheckBoxTreeNode node1_2 = new CheckBoxTreeNode("node_1_2");
		CheckBoxTreeNode node1_3 = new CheckBoxTreeNode("node_1_3");
		node1.add(node1_1);
		node1.add(node1_2);
		node1.add(node1_3);
		CheckBoxTreeNode node2 = new CheckBoxTreeNode("node_2");
		CheckBoxTreeNode node2_1 = new CheckBoxTreeNode("node_2_1");
		CheckBoxTreeNode node2_2 = new CheckBoxTreeNode("node_2_2");
		node2.add(node2_1);
		node2.add(node2_2);
		rootNode.add(node1);
		rootNode.add(node2);*/
		JScrollPane scroll = new JScrollPane(tree);
		scroll.setBounds(0, 0, 300, 320);
		add(scroll);
		CheckBoxTreeNode rootNode= TreeOperation.traverseFolder(Constant.TESTCRIPT);
		DefaultTreeModel model = new DefaultTreeModel(rootNode);
		//tree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
		tree.setModel(model);
		tree.setCellRenderer(new CheckBoxTreeCellRenderer());

	}
	public static void main(String[] args)
	{
		DemoMain frame = new DemoMain();
		System.out.println("frame.getContentPane()的值是：---"+  frame.getContentPane()+ "，当前方法=main.DemoMain()");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
