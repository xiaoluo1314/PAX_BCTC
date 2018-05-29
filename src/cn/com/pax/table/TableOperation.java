package cn.com.pax.table;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * 表格属性操作类
 *
 * @author luohl
 * @create 2017-12-06-19:23
 */
public class TableOperation {
    public  static  void setTable(JTable table){
        table.getTableHeader().setReorderingAllowed( false );
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setFillsViewportHeight(true);

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getTableHeader().setDefaultRenderer(headerRenderer);

       // DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        //增加对齐指令
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus,int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
                String resultString = (String)table.getModel().getValueAt(row, 2);
                if("PASS".equalsIgnoreCase(resultString))
                    cell.setBackground(PreColor.passedColor);
                else if("FAIL".equalsIgnoreCase(resultString))
                    cell.setBackground(PreColor.failedColor);
                else if(!isSelected)
                    setBackground(Color.white);
                return cell;
            }
        };
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, cellRenderer);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(5);
    }
}
    