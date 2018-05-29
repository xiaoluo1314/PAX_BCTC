package cn.com.pax.table;

import javax.swing.table.DefaultTableModel;

/**
 * 表格模型
 *
 * @author luohl
 * @create 2017-12-06-18:52
 */
public class MyTableModel extends DefaultTableModel {

    private int col;
    public MyTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
        col = -1;
        //row = -1;
    }

    public void setCellEditable(int row, int column) {
        //this.row = row;
        this.col = column;
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        // TODO Auto-generated method stub
        if(column==col)
            return true;
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // TODO Auto-generated method stub
        if(this.getRowCount() <= 0 )
            return null;
        Object value = getValueAt(0, columnIndex);

        if (value != null)
            return value.getClass();
        else
            return super.getClass();
    }


}
    