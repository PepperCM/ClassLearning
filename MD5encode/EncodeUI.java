package MD5encode;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.awt.event.ActionEvent;
public class EncodeUI {

	private JFrame frame;
	private JTextField pathfield;
	private JTextField picfield;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EncodeUI window = new EncodeUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EncodeUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);

		JLabel label = new JLabel("密钥生成文件路径");
		panel_2.add(label);

		picfield = new JTextField();
		picfield.setColumns(20);
		picfield.setEditable(false);
		panel_2.add(picfield);

		JButton selectPicButton = new JButton("选取文件");
		panel_2.add(selectPicButton);

		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);

		JButton colbutton = new JButton("计算");
		panel_5.add(colbutton);

		JLabel md5 = new JLabel("MD5:");
		panel_5.add(md5);

		JPanel panel_3 = new JPanel();
		panel.add(panel_3);

		JLabel lblNewLabel_2 = new JLabel("加密目录");
		panel_3.add(lblNewLabel_2);

		pathfield = new JTextField();
		panel_3.add(pathfield);
		pathfield.setEditable(false);
		pathfield.setColumns(20);

		JButton selectPath = new JButton("选取目录");
		panel_3.add(selectPath);

		JPanel panel_4 = new JPanel();
		panel.add(panel_4);

		JButton encodeButton = new JButton("加密");
		panel_4.add(encodeButton);

		JButton decodeButton = new JButton("解密");
		panel_4.add(decodeButton);

		JButton checkButton = new JButton("对比");
		panel_4.add(checkButton);
		
		
		checkButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Check.run()){
					JDialog j = new JDialog(frame, "对比信息", false);
					j.setSize(100, 50);
					j.add(new JPanel().add(new JLabel("true")));
					j.setVisible(true);
				}else{
					JDialog j = new JDialog(frame, "对比信息", false);
					j.setSize(100, 50);
					j.add(new JPanel().add(new JLabel("false")));	
					j.setVisible(true);
				}
				
			}
		});
		
		colbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String path = picfield.getText();
				if (path.equals(""))
					return;
				try {
					byte[] a = WorkSection.getMd5ByFile(new File(path));
					BigInteger bi = new BigInteger(1, a);
					WorkSection.setKey(a);
					md5.setText("MD5:" + bi.toString(16).toUpperCase());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		selectPicButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser j = new JFileChooser();
				j.showDialog(null, "确定");
				j.setFileSelectionMode(JFileChooser.FILES_ONLY);
				j.setMultiSelectionEnabled(false);
				j.setVisible(true);
				picfield.setText(j.getSelectedFile().getAbsolutePath());
			}
		});

		encodeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (pathfield.getText().equals("") || picfield.getText().equals(""))
					return;
				File[] files = WorkSection.getFiles(pathfield.getText());
				
				WorkSection.setKey(picfield.getText());
				Check.srcMd5(files);
				for (File i : files)
					try {
						WorkSection.encodeFile(i);
						i.delete();
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException
							| IOException e1) {
						e1.printStackTrace();
					}

			}
		});

		decodeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (pathfield.getText().equals("") || picfield.getText().equals(""))
					return;
				File[] files = WorkSection.getFiles(pathfield.getText());

				WorkSection.setKey(picfield.getText());
				for (File i : files)
					try {
						WorkSection.decodeFile(i);
						i.delete();
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException
							| IOException e1) {
						e1.printStackTrace();
					}
				files = WorkSection.getFiles(pathfield.getText());
				Check.encodeMd5(files);
			}
		});

		selectPath.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser j = new JFileChooser();

				j.setMultiSelectionEnabled(false);
				j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				j.setVisible(true);
				j.showDialog(null, "确定");
				pathfield.setText(j.getSelectedFile().getAbsolutePath());
			}
		});

	}

}
