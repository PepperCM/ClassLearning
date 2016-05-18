package FilesCleanner;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Proxy;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * UI��ͼ����
 * ��Ҫ���еĹ����ǣ��ٴ�������
 * 			   �ڲ�������
 * 			   �۶԰�ť���ü�����
 * �ó������������в�������ͨ����ť������������״̬�ı䣬�����¼������߳������е�
 * 
 * @author Pepper
 *
 */

public class MainUI {
	private JFrame frame;
	private JTable table;
	private Chooser c;
	private MyTableModel mTableModel;
	// �ֱ���·���ı����������ؼ����ı�������չ���ı�������С�ı���
	private JTextField pathField, keywordField, extensionField, sizeField;
	private JButton scanButton, clearButton, browerButton, allSelectButton, disSelectButton;
	private LinkedList<File> files;

	public MainUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initPanel();
		initButton();
		initTable();
		frame.setVisible(true);
	}

	private void initButton() {

		browerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c = new Chooser();
				pathField.setText(c.getPath());
			}
		});

		/**
		 * ����ʱ�����ܻ�����ļ���С������쳣��������Ϊ���õ�size������long������Ĵ�С�Ѿ�ԶԶ���������ļ�ϵͳ���ܴ洢�����ֵ
		 * ���Բ��ú��Բ���������ʲô��û���ҳ�
		 * ������size����һ�����ֵ�ʱ����ʾ�������
		 */
		scanButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				for (int i = mTableModel.getRowCount() - 1; i >= 0; i--)
					mTableModel.removeRow(i);

				Thread t = new Thread(new Runnable() {
					public void run() {
						try {
							lock();
							String key = keywordField.getText();
							String extension = extensionField.getText();
							String path = pathField.getText();
							long size = 0;
							String s;
							if (!(s = sizeField.getText()).equals(""))
								size = Long.parseLong(s);
							files = Scan.getfiles(extension, key, size, path);
							if (files == null) {
								clear();
								return;
							}
							for (File item : files)
								mTableModel.addRow(
										new Object[] { false, item.getName(), item.getAbsolutePath(), item.length() });
						} catch (NumberFormatException e) {
							new ErrorDialog(frame, "input error,must be a llegal number");
						} catch (Exception e) {
							new ErrorDialog(frame, e.getMessage());
						} finally {
							clear();
						}
					}
				});

				t.start();
			}
		});

		// û�м�鵽�쳣�������������finally��ȷ����ť�ܱ���������״̬
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						lock();

						try {
							for (int i = table.getRowCount() - 1; i >= 0; i--) {
								boolean f = (boolean) mTableModel.getValueAt(i, 0);
								if (f == true) {
									// ���ɾ�����
									// ��Ϊ�����Sorter������ɾ�����󣬲���files��ɾ���������ҵ��ļ�·����ɾ��
									File f1 = new File((String) mTableModel.getValueAt(i, 2));
									f1.delete();
									
									mTableModel.removeRow(i);
								}
							}
						} catch (Exception e) {
							new ErrorDialog(frame, e.getMessage());
						} finally {
							clear();
						}
					}
				});
				t.start();
			}

		});

		allSelectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						lock();
						for (int i = table.getRowCount() - 1; i >= 0; i--) {
							mTableModel.setValueAt(true, i, 0);
						}
						clear();
					}
				});
				t.start();
			}
		});

		// ʵ�ֹ����Ƿ�ѡ
		disSelectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() {
					public void run() {
						lock();
						for (int i = table.getRowCount() - 1; i >= 0; i--) {
							boolean b = (boolean) mTableModel.getValueAt(i, 0);
							mTableModel.setValueAt(!b, i, 0);
						}
						clear();
					}
				});
				t.start();
			}
		});
	}

	// ��Ҫ�ĳ�ʼ��UI����Ĳ������������BorderLayout�Ĳ���
	// ��С�ķ�����ϸ�������Ĳ��֣�ȷ�����������
	// Java�Դ����������
	private void initPanel() {

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));

		JLabel lblPath = new JLabel("PATH");
		panel_2.add(lblPath);

		pathField = new JTextField();
		pathField.setEditable(false);
		panel_2.add(pathField);
		pathField.setColumns(10);

		browerButton = new JButton("Brower");
		panel_2.add(browerButton);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));

		JLabel lblMinsize = new JLabel("minSize");
		panel_3.add(lblMinsize);

		sizeField = new JTextField();
		panel_3.add(sizeField);
		sizeField.setColumns(10);

		JLabel jlbKb = new JLabel("B   ");
		panel_3.add(jlbKb);
		
		
		JLabel lblExtension = new JLabel("extension");
		panel_3.add(lblExtension);

		extensionField = new JTextField();
		panel_3.add(extensionField);
		extensionField.setColumns(10);

		JLabel lblKeyword = new JLabel("keyword");
		panel_3.add(lblKeyword);

		keywordField = new JTextField();
		panel_3.add(keywordField);
		keywordField.setColumns(10);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		scanButton = new JButton("Scan");
		panel_1.add(scanButton);

		clearButton = new JButton("Clear");
		panel_1.add(clearButton);

		table = new JTable();
		JScrollPane pane = new JScrollPane(table);
		frame.getContentPane().add(pane, BorderLayout.CENTER);

		JPanel panel_4 = new JPanel();
		frame.getContentPane().add(panel_4, BorderLayout.SOUTH);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));

		allSelectButton = new JButton("All Select");
		panel_4.add(allSelectButton);
		disSelectButton = new JButton("Dis Select");
		panel_4.add(disSelectButton);

	}

	private void initTable() {
		/********* init table ********/
		mTableModel = new MyTableModel();
		TableModel p = (TableModel)Proxy.newProxyInstance(ProxyTableModel.class.getClassLoader(), new Class[]{TableModel.class}, new Handler());
		TableRowSorter<TableModel> trs = new TableRowSorter<TableModel>(mTableModel);
		table.setModel(mTableModel);
		table.setRowSorter(trs);
		for (int i = 0; i < 4; i++)
			if (i == 0)
				table.getColumnModel().getColumn(i).setPreferredWidth(50);
			else {
				table.getColumnModel().getColumn(i).setPreferredWidth(200);
			}
		/******************************/
	}
	//�����а�ť������������ʾɨ����߳�����������
	private void lock() {
		scanButton.setEnabled(false);
		clearButton.setEnabled(false);
		browerButton.setEnabled(false);
		allSelectButton.setEnabled(false);
		disSelectButton.setEnabled(false);
	}
	//�����а�ť���лָ�
	private void clear() {
		scanButton.setEnabled(true);
		clearButton.setEnabled(true);
		browerButton.setEnabled(true);
		allSelectButton.setEnabled(true);
		disSelectButton.setEnabled(true);
	}

	public static void main(String[] args) {
		new MainUI();
	}

}
