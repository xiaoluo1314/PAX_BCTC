package cn.com.pax.tree;


import cn.com.pax.entity.TreeLeaf;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class CustTreeModel extends DefaultTreeModel
{
    private int status;
    private CheckBoxTreeNode mRoot;

    public CustTreeModel(CheckBoxTreeNode root)
    {
        super(root);
        mRoot= root;
    }

    public Object getChild(Object parent, int index)
    {
        CheckBoxTreeNode node= (CheckBoxTreeNode) parent;

        int pos= 0;
        for (int i= 0, cnt= 0; i< node.getChildCount(); i++) {
            TreeLeaf leaf = (TreeLeaf) ((CheckBoxTreeNode) node.getChildAt(i)).getUserObject();
            if (leaf.getLeafName().endsWith(".txt"))
            {
                TreeLeaf info = (TreeLeaf) (((CheckBoxTreeNode) node.getChildAt(i)).getUserObject());
                if(getResult(status).contains(convertStr(info.getState()))) {
                    if(cnt++ == index) {
                        pos = i;
                        break;
                    }
                }
            } else {
                pos = index;
            }
        }
        return node.getChildAt(pos);
    }

    public int getChildCount(Object parent)
    {
        CheckBoxTreeNode node=
                (CheckBoxTreeNode) parent;

        int childCount= 0;
        for(int i=0; i<node.getChildCount(); i++) {
            CheckBoxTreeNode child = (CheckBoxTreeNode)node.getChildAt(i);
            TreeLeaf leaf = (TreeLeaf) (child.getUserObject());
            if (leaf.getLeafName().endsWith(".txt")) {
                TreeLeaf info = (TreeLeaf)(child.getUserObject());
                if(getResult(status).contains(convertStr(info.getState()))) {
                    childCount++;
                }
            } else {
                childCount++;
            }
        }
        return childCount;
    }

    public static int convertInt(String status){
        if (status == null){
            return 4;
        }else{
            if ("UNTEST".equals(status)){
                return 4;
            }else if("FAIL".equals(status)){
                return 2;
            }else{
                return 1;
            }
        }
    }


    public static String convertStr(int status){
        if (status == 4) {
            return "UNTEST";
        }else if (status == 2) {
            return "FAIL";
        }else{
            return "PASS";
        }
    }
    public static String getResult(int status) {
        String result = "";
        if(getResult(status, 0x4))
            result += "UNTEST";
        if(getResult(status, 0x2))
            result += "FAIL";
        if(getResult(status, 0x1))
            result += "PASS";
        return result;
    }

    private static boolean getResult(int s1, int t1) {
        if((s1 & t1) == t1 ) return true;
        return false;
    }

    public int getShowStatus() {
        return status;
    }

    public void setShowStatus(int mstatus)
    {
        if(status != mstatus) {
            status = mstatus;
            Object[] path = {mRoot};
            int[] childIndices = new int[root.getChildCount()];
            Object[] children = new Object[root.getChildCount()];
            for (int i = 0; i < root.getChildCount(); i++) {
                childIndices[i] = i;
                children[i] = root.getChildAt(i);
            }
            fireTreeStructureChanged(this, path, childIndices, children);
        }
    }
}


    