package cn.com.pax.table;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * CheckBox渲染
 *
 * @author luohl
 * @create 2017-12-06-18:53
 */
public class MyCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

    public MyCheckBoxRenderer() {
        this.setBorderPainted(false);
        this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        return this;

    }
}
    