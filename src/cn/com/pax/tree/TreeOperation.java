package cn.com.pax.tree;

import cn.com.pax.Constant;
import cn.com.pax.entity.TreeLeaf;
import cn.com.pax.utils.UIUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 树操作的类
 *
 * @author luohl
 * @create 2017-12-06-20:27
 */
public class TreeOperation {
  //  private static DefaultMutableTreeNode temp;
    //public static List<String>saveName = new LinkedList<>();
    public static List<CheckBoxTreeNode>saveList = new ArrayList<CheckBoxTreeNode>();
    private static CheckBoxTreeNode temp;

    public static Map<String,String> dirPath = new LinkedHashMap<>();

    public static List<CheckBoxTreeNode> getSaveList() {
        return saveList;
    }
    /*@Nullable
    public static DefaultMutableTreeNode traverseFolder(String path) {
        //DefaultMutableTreeNode  parentNode = new DefaultMutableTreeNode(new File(path).getName());
        DefaultMutableTreeNode  parentNode = new DefaultMutableTreeNode(new TreeLeaf(new File(path).getName(),
                UIUtils.createImageIcon(Constant.DIRICON)));
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                if(file.isDirectory()) {//如果是空文件夹
                    //DefaultMutableTreeNode dn=new DefaultMutableTreeNode(file.getName(), false);
                    DefaultMutableTreeNode dn = new DefaultMutableTreeNode(new TreeLeaf(file.getName(),
                            UIUtils.createImageIcon(Constant.DIRICON)),false);
                    return dn;
                }
            }else{
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        //是目录的话，生成节点，并添加里面的节点
                       parentNode.add(traverseFolder(file2.getAbsolutePath()));
                    }else{
                        //是文件的话直接生成节点，并把该节点加到对应父节点上
                       // temp = new DefaultMutableTreeNode(file2.getName());//其实这是核心，叶子节点 是否需要存起来？
                        temp = new DefaultMutableTreeNode(new TreeLeaf(file2.getName(), UIUtils.createImageIcon(Constant.UNTEST)));//其实这是核心，叶子节点 是否需要存起来？
                        parentNode.add(temp);
                    }
                }
            }
        } else {//文件不存在
            return null;
        }
        return parentNode;
    }*/

    @Nullable
    public static CheckBoxTreeNode traverseFolder(String path) {

        CheckBoxTreeNode parentNode = new CheckBoxTreeNode(new TreeLeaf(new File(path).getName(),
               null,4));

        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                if(file.isDirectory()) {//如果是空文件夹
                    //DefaultMutableTreeNode dn=new DefaultMutableTreeNode(file.getName(), false);//UIUtils.createImageIcon(Constant.DIRICON)
                    CheckBoxTreeNode dn = new CheckBoxTreeNode(new TreeLeaf(file.getName(),
                          null ,4 ),false);
                    return dn;
                }
            }else{
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        //是目录的话，生成节点，并添加里面的节点
                        parentNode.add(traverseFolder(file2.getAbsolutePath()));
//                        if (file2.getParentFile().getName().contains("条码")){
//                            saveName.add(file2.getName());
//                        }
                    }else{
                        //是文件的话直接生成节点，并把该节点加到对应父节点上
                        // temp = new DefaultMutableTreeNode(file2.getName());//其实这是核心，叶子节点 是否需要存起来？
                        temp = new CheckBoxTreeNode(new TreeLeaf(file2.getName(), UIUtils.createImageIcon(Constant.UNTEST),4));//其实这是核心，叶子节点是否需要存起来？
                        //System.out.println("#####"+file2.getParentFile().getParent());
                        //System.out.println(file2.getAbsolutePath());
                        //dirPath.add(file2.getParentFile().getParentFile().getName());
                        dirPath.put(file2.getName(),file2.getAbsolutePath());
                        saveList.add(temp);
                        parentNode.add(temp);
                    }
                }
            }
        } else {//文件不存在
            return null;
        }
       //System.out.println(dirPath.size()+"@@@@"+dirPath);
        return parentNode;
    }

    public static void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }


}
    