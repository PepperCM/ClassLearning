package FilesCleanner;

import java.util.EventListener;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;


/**
 * 自帶的<code>DefaultTableModel</code> 不能显示boolean，也是就checkbox框，所以需要自己设置TableModel
 * 但是自己设计一个TableModel太费时，而且大部分的方法功能在DefaultTableModel中就已经实现的很完善
 * 所以采用代理模式，在类中定义一个DefaultTableModel 对象，在不需要改变的方法中直接调用DefaultTableModel的方法
 * 改变的方法：getColumnClass,isCellEditable,getColumnName;
 * 
 * @author Pepper
 *
 */

@SuppressWarnings("serial")
public class MyTableModel extends AbstractTableModel {
	private DefaultTableModel dtm;
	private Object[] model = { false, new String("name"), new String("path"), new String("size/B") };

	public MyTableModel() {
		dtm = new DefaultTableModel(model, 0);
	}

	public void addRow(Object[] vaObjects) {
		dtm.addRow(vaObjects);
	}

	public void removeRow(int index) {
		dtm.removeRow(index);
	}

	@Override
	public int getRowCount() {
		return dtm.getRowCount();
	}

	@Override
	public int getColumnCount() {
		return dtm.getColumnCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return dtm.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public String getColumnName(int column) {
		if (column < 1)
			return "";
		return dtm.getColumnName(column);
	}

	@Override
	public int findColumn(String columnName) {
		return dtm.findColumn(columnName);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		if (columnIndex < 1)
			return Boolean.class;
		if(columnIndex == 3)
			return Double.class;
		return dtm.getColumnClass(columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex < 1)
			return true;
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		dtm.setValueAt(aValue, rowIndex, columnIndex);
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		dtm.addTableModelListener(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		dtm.removeTableModelListener(l);
	}

	@Override
	public TableModelListener[] getTableModelListeners() {
		return dtm.getTableModelListeners();
	}

	@Override
	public void fireTableDataChanged() {
		dtm.fireTableDataChanged();
	}

	@Override
	public void fireTableStructureChanged() {
		dtm.fireTableStructureChanged();
	}

	@Override
	public void fireTableRowsInserted(int firstRow, int lastRow) {
		dtm.fireTableRowsInserted(firstRow, lastRow);
	}

	@Override
	public void fireTableRowsUpdated(int firstRow, int lastRow) {
		dtm.fireTableRowsUpdated(firstRow, lastRow);
	}

	@Override
	public void fireTableRowsDeleted(int firstRow, int lastRow) {
		dtm.fireTableRowsDeleted(firstRow, lastRow);
	}

	@Override
	public void fireTableCellUpdated(int row, int column) {
		dtm.fireTableCellUpdated(row, column);
	}

	@Override
	public void fireTableChanged(TableModelEvent e) {
		dtm.fireTableChanged(e);
	}

	@Override
	public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
		return dtm.getListeners(listenerType);
	}

}
