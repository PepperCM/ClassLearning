package FileSolve;


import javax.swing.JFileChooser;

public class ChoosePath {
	public static String getPath(){
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jFileChooser.setApproveButtonText("È·¶¨");
		jFileChooser.showOpenDialog(null);
		String path =jFileChooser.getSelectedFile().getAbsolutePath();
		return path;
		
	}
}
