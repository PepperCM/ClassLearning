package FilesCleanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * �����ļ�Ŀ¼ѡ�������������һϵ�е�����
 * ����Ϊ��ֻ��ѡ���ļ���
 * 		���������ѡ��
 * @author Pepper
 *
 */
@SuppressWarnings("serial")
public class Chooser extends JFrame {
	private JFileChooser chooser;
	public Chooser() {
		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setMultiSelectionEnabled(false);
		chooser.showDialog(null, "Ŀ¼");
		setLayout(null);
	}
	
	//�������������Ϊ��Ϊ������ʾ·�����õ�
	public String getPath(){
		return chooser.getSelectedFile().getAbsolutePath();
	}
	
	
}
