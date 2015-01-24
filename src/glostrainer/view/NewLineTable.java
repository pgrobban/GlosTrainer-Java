package glostrainer.view;

import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * A table that keeps an extra empty line. We use it in the application
 * as an extra way of adding a new entry to the table.
 * @author Peter Lang
 * http://stackoverflow.com/a/2321411/147262
 */
public class NewLineTable extends JTable {

    @Override
    public int getRowCount() {
        // fake an additional row
        return super.getRowCount() + 1;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if(row < super.getRowCount()) {
            return super.getValueAt(row, column);
        }
        return ""; // value to display in new line
    }

    @Override
    public int convertRowIndexToModel(int viewRowIndex) {
        if(viewRowIndex < super.getRowCount()) {
            return super.convertRowIndexToModel(viewRowIndex);
        }
        return super.getRowCount(); // can't convert our faked row
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if(row < super.getRowCount()) {
            super.setValueAt(aValue, row, column);
        }
        else {
            Object[] rowData = new Object[getColumnCount()];
            Arrays.fill(rowData, "");
            rowData[convertColumnIndexToModel(column)] = aValue;
            // That's where we insert the new row.
            // Change this to work with your model.
            ((DefaultTableModel)getModel()).addRow(rowData);
        }
    }
}