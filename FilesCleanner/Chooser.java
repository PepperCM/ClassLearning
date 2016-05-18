package FilesCleanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * 创建文件目录选择器，对其进行一系列的设置
 * 设置为：只能选择文件夹
 * 		不允许多重选择
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
		chooser.showDialog(null, "目录");
		setLayout(null);
	}
	
	//这个方法纯粹是为了为界面显示路径设置的
	public String getPath(){
		return chooser.getSelectedFile().getAbsolutePath();
	}
	
	
}
