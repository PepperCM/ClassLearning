package FilesCleanner;

import java.util.EventListener;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;


/**
 * �Ԏ���<code>DefaultTableModel</code> ������ʾboolean��Ҳ�Ǿ�checkbox��������Ҫ�Լ�����TableModel
 * �����Լ����һ��TableModel̫��ʱ�����Ҵ󲿷ֵķ���������DefaultTableModel�о��Ѿ�ʵ�ֵĺ�����
 * ���Բ��ô���ģʽ�������ж���һ��DefaultTableModel �����ڲ���Ҫ�ı�ķ�����ֱ�ӵ���DefaultTableModel�ķ���
 * �ı�ķ�����getColumnClass,isCellEditable,getColumnName;
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
