package FileSolve;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class Control extends Frame {

	// ������ʾ��
	private TextArea textArea = new TextArea();
	private TextField username = new TextField();
	private TextField password = new TextField();
	private Button button = new Button("confirm");
	private String musername, mpassword;
	private static Control c;

	private void launchFrame() {
		// ��ʼ������
		{
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setLocation(500, 500);
			setSize(500, 500);
			add(textArea, BorderLayout.NORTH);
			textArea.setEditable(false);
			add(new Label("�û���"));
			add(username);
			add(new Label("����"));
			add(password);
			add(button);
			pack();
		}

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// ��ť�����£��͵���run����
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == button) {
					musername = username.getText();
					mpassword = password.getText();
					System.out.println(musername);
					System.out.println(mpassword);
					try {
						run();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		setVisible(true);
	}

	public void run() throws Exception {
		String path = System.getProperty("user.dir") + new Date().getTime() + "�����ļ��A";
		
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		c.textArea.append("ѡ��·����" + path + "\n��ʼ����\n");
		try {
			ReadMail.readMail(path, musername, mpassword);
		} catch (Exception e) {
			e.printStackTrace();
			c.textArea.append("����ʧ�ܣ�����û������������\n");
			return;
		}
		c.textArea.append("���ӳɹ�\n");
		c.textArea.append("��ȡExcel\n");
		ReadExcel.run(path);
		c.textArea.append("���");
		
	}

	public static void main(String[] args) throws Exception {
		c = new Control();
		c.launchFrame();
	}

}
