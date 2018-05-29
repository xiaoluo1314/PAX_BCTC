package cn.com.pax.table;

import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 * 隐藏列，后期可能需要
 *
 * @author luohl
 * @create 2017-12-07-20:06
 */
public class HidTableColumn {

    public static  void HiddenCell(JTable table, int column) {
        TableColumn tc = table.getTableHeader().getColumnModel().getColumn(column);
        tc.setMaxWidth(0);
        tc.setPreferredWidth(0);
        tc.setWidth(0);
        tc.setMinWidth(0);
        table.getTableHeader().getColumnModel().getColumn(column).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(column).setMinWidth(0);
    }
}
    