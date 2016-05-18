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

	// 输入显示区
	private TextArea textArea = new TextArea();
	private TextField username = new TextField();
	private TextField password = new TextField();
	private Button button = new Button("confirm");
	private String musername, mpassword;
	private static Control c;

	private void launchFrame() {
		// 初始化界面
		{
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setLocation(500, 500);
			setSize(500, 500);
			add(textArea, BorderLayout.NORTH);
			textArea.setEditable(false);
			add(new Label("用户名"));
			add(username);
			add(new Label("密码"));
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

		// 按钮被按下，就调用run方法
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
		String path = System.getProperty("user.dir") + new Date().getTime() + "接收文件夾";
		
		File f = new File(path);
		if (!f.exists())
			f.mkdirs();
		c.textArea.append("选择路径：" + path + "\n开始连接\n");
		try {
			ReadMail.readMail(path, musername, mpassword);
		} catch (Exception e) {
			e.printStackTrace();
			c.textArea.append("连接失败，检查用户名密码或网络\n");
			return;
		}
		c.textArea.append("连接成功\n");
		c.textArea.append("读取Excel\n");
		ReadExcel.run(path);
		c.textArea.append("完成");
		
	}

	public static void main(String[] args) throws Exception {
		c = new Control();
		c.launchFrame();
	}

}
