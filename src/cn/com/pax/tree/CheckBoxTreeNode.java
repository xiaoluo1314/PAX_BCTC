package cn.com.pax.tree;

import cn.com.pax.entity.TableData;
import cn.com.pax.entity.TreeLeaf;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

public class CheckBoxTreeNode extends DefaultMutableTreeNode
{
	private static Set<TableData> tabelSetSelect = new LinkedHashSet<TableData>();
	private static Set<TableData> tabelSetCancel  = new LinkedHashSet<TableData>();

	public static Set<TableData> getTabelSet() {
		return tabelSetSelect;
	}

	protected boolean isSelected;
	
	public CheckBoxTreeNode()
	{
		this(null);
	}
	
	public CheckBoxTreeNode(Object userObject)
	{
		this(userObject, true, false);
	}
	public CheckBoxTreeNode(Object userObject ,boolean allowsChildren)
	{
		this(userObject, true, false);
	}

	public CheckBoxTreeNode(Object userObject, boolean allowsChildren, boolean isSelected)
	{
		super(userObject, allowsChildren);
		this.isSelected = isSelected;
	}

	public boolean isSelected()
	{
		return isSelected;
	}
	
	public void setSelected(boolean _isSelected)
	{
		this.isSelected = _isSelected;

		if(_isSelected)
		{
			// 如果选中，则将其所有的子结点都选中
			if(children != null)
			{
				for(Object obj : children)
				{
					CheckBoxTreeNode node = (CheckBoxTreeNode)obj;
					//System.out.println("node的值是：---"+ node + "，当前方法=setSelected.CheckBoxTreeNode()");
					if(_isSelected != node.isSelected())
						node.setSelected(_isSelected);
					//System.out.println("xxx的值是：---"+ "1111" + "，当前方法=setSelected.CheckBoxTreeNode()");
					getData(node);
				}
			}else{
				if (this.isLeaf()){
					getData(this);
				}
			}
			// 向上检查，如果父结点的所有子结点都被选中，那么将父结点也选中
			CheckBoxTreeNode pNode = (CheckBoxTreeNode)parent;
			// 开始检查pNode的所有子节点是否都被选中
			//System.out.println("pNode的值是：---"+ pNode + "，当前方法=setSelected.CheckBoxTreeNode()");
			if(pNode != null)
			{
				int index = 0;
				for(; index < pNode.children.size(); ++ index)
				{
					CheckBoxTreeNode pChildNode = (CheckBoxTreeNode)pNode.children.get(index);
					//System.out.println("pChildNode的值是：---"+ pChildNode + "，当前方法=setSelected.CheckBoxTreeNode()");

					if(!pChildNode.isSelected())
						break;
				}
				/* 
				 * 表明pNode所有子结点都已经选中，则选中父结点，
				 * 该方法是一个递归方法，因此在此不需要进行迭代，因为
				 * 当选中父结点后，父结点本身会向上检查的。
				 */
				if(index == pNode.children.size())
				{
					if(pNode.isSelected() != _isSelected)
						pNode.setSelected(_isSelected);
				}

			}
		}
		else 
		{
			/*
			 * 如果是取消父结点导致子结点取消，那么此时所有的子结点都应该是选择上的；
			 * 否则就是子结点取消导致父结点取消，然后父结点取消导致需要取消子结点，但
			 * 是这时候是不需要取消子结点的。
			 */
			if(children != null)
			{
				int index = 0;
				for(; index < children.size(); ++ index)
				{
					CheckBoxTreeNode childNode = (CheckBoxTreeNode)children.get(index);
					if(!childNode.isSelected())
						break;
				}
				// 从上向下取消的时候
				if(index == children.size())
				{
					for(int i = 0; i < children.size(); ++ i)
					{
						CheckBoxTreeNode node = (CheckBoxTreeNode)children.get(i);
						if(node.isSelected() != _isSelected)
							node.setSelected(_isSelected);
					}
				}
			}
			
			// 向上取消，只要存在一个子节点不是选上的，那么父节点就不应该被选上。
			CheckBoxTreeNode pNode = (CheckBoxTreeNode)parent;
			if(pNode != null && pNode.isSelected() != _isSelected)
				pNode.setSelected(_isSelected);
		}
		//System.out.println("tabelSet的值是：---"+ tabelSet + "，当前方法=setSelected.CheckBoxTreeNode()");
		//System.out.println("tabelSet.size()的值是：---"+ tabelSet.size() + "，当前方法=setSelected.CheckBoxTreeNode()");
	}
	public static Set getData(CheckBoxTreeNode node){
		//System.out.println("node的值是：---"+ node + "，当前方法=getData.CheckBoxTreeNode()");
		if (node.isLeaf()){
			TreeLeaf treeLeaf =(TreeLeaf)node.getUserObject();
			TableData tableData = new TableData();
			tableData.setLeafName(treeLeaf.getLeafName());
			tableData.setSelect(false);
			tableData.setState(treeLeaf.getState());
			tabelSetSelect.add(tableData); //过滤掉界面不存在的值
		}
		return tabelSetSelect;
	}
	

}