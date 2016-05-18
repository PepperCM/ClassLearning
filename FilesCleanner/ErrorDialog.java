package FilesCleanner;

import java.awt.CardLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 错误提示界面
 * @author Pepper
 *
 */
public class ErrorDialog {
	JDialog j;

	public ErrorDialog(JFrame frame, String text) {
		j = new JDialog(frame, text, true);
		j.setLocation((int)(frame.getLocation().getX()+frame.getWidth()/2),(int) (frame.getLocation().getY()+frame.getHeight()/2));
		j.setLayout(new CardLayout());
		j.add(new JLabel(text));
		j.setSize(200, 100);
		j.setTitle("ERROR");
		j.setVisible(true);
	}
}
