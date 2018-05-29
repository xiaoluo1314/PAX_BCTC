package cn.com.pax.table;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 鼠标点击事件
 *
 * @author luohl
 * @create 2017-12-07-20:16
 */
//目前没有什么用，预留
public class MyMouseListener extends MouseAdapter {
    private int row = -1;
    private int col = -1;
    private JTable table;
    public MyMouseListener(JTable table) {
        super();
        this.table = table;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        row = table.rowAtPoint(e.getPoint());
        col = table.columnAtPoint(e.getPoint());
        System.out.println("11111");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if(e.getButton() == MouseEvent.BUTTON1) {
            int row = table.rowAtPoint(e.getPoint());
            int col = table.columnAtPoint(e.getPoint());
            if(row >= table.getRowCount() || col >= table.getColumnCount())
                return;
            if(row != this.row || col != this.col)
                return;
            if(row < 0) return;
            Object obj = table.getValueAt(row, col);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }
}
    