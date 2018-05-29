package cn.com.pax.tree;

import cn.com.pax.Constant;
import cn.com.pax.entity.TreeLeaf;
import cn.com.pax.utils.UIUtils;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class CheckBoxTreeCellRenderer extends JPanel implements TreeCellRenderer
{
	protected JCheckBox check;
	protected CheckBoxTreeLabel label;

	public CheckBoxTreeCellRenderer()
	{
		setLayout(null);
		add(check = new JCheckBox());
		add(label = new CheckBoxTreeLabel());
		check.setBackground(UIManager.getColor("Tree.textBackground"));
		label.setForeground(UIManager.getColor("Tree.textForeground"));
	}
	/**
	 * 返回的是一个<code>JPanel</code>对象，该对象中包含一个<code>JCheckBox</code>对象
	 * 和一个<code>JLabel</code>对象。并且根据每个结点是否被选中来决定<code>JCheckBox</code>
	 * 是否被选中。
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		//String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		//System.out.println("stringValue的值是：---"+ stringValue + "，当前方法=getTreeCellRendererComponent.CheckBoxTreeCellRenderer()");
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		setEnabled(tree.isEnabled());
		//System.out.println("tree.isEnabled()的值是：---"+ tree.isEnabled()+ "，当前方法=getTreeCellRendererComponent.CheckBoxTreeCellRenderer()");
		check.setSelected(((CheckBoxTreeNode)value).isSelected());
		label.setFont(tree.getFont());
		label.setSelected(selected);
		label.setFocus(hasFocus);

		TreeLeaf treeLeaf = (TreeLeaf) node.getUserObject();
		label.setText(treeLeaf.getLeafName());
		if(leaf){
			if (treeLeaf.getState() == 4){
				label.setIcon(UIUtils.createImageIcon(Constant.UNTEST));
			}else if(treeLeaf.getState() == 1){
				label.setIcon(UIUtils.createImageIcon(Constant.PASS));
			}else{
				label.setIcon(UIUtils.createImageIcon(Constant.FAIL));
			}
		}
		else if(expanded)
			label.setIcon(UIManager.getIcon("Tree.openIcon"));
		else
			label.setIcon(UIManager.getIcon("Tree.closedIcon"));
			
		return this;
	}

	@Override
	public Dimension getPreferredSize()
	{
		Dimension dCheck = check.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		//System.out.println("dCheck的值是：---"+ dCheck + "，当前方法=getPreferredSize.CheckBoxTreeCellRenderer()");
		return new Dimension(dCheck.width + dLabel.width, dCheck.height < dLabel.height ? dLabel.height: dCheck.height);
	}
	
	@Override
	public void doLayout()
	{
		Dimension dCheck = check.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
//		System.out.println("dCheck.width的值是：---"+ dCheck.width + "，当前方法=doLayout.CheckBoxTreeCellRenderer()");
//		System.out.println("dCheck.height的值是：---"+ dCheck.height + "，当前方法=doLayout.CheckBoxTreeCellRenderer()");
		int yCheck = 0;
		int yLabel = 0;

		if(dCheck.height < dLabel.height)
			yCheck = (dLabel.height - dCheck.height) / 2;
		else
			yLabel = (dCheck.height - dLabel.height) / 2;

		//System.out.println("yCheck的值是：---"+ yCheck+ "，当前方法=doLayout.CheckBoxTreeCellRenderer()");
		check.setLocation(0, yCheck);
		check.setBounds(0, yCheck, dCheck.width, dCheck.height-2);
		label.setLocation(dCheck.width, yLabel);
		label.setBounds(dCheck.width, yLabel, dLabel.width, dLabel.height);
	}
	
	@Override
	public void setBackground(Color color)
	{
		if(color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}
}
