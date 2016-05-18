package FileSolve;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

public class ReadWod {
	public static String readWord(String fileName)throws Exception {
		String text = null;
		if(fileName.endsWith(".doc")){
		FileInputStream fis = new FileInputStream(fileName);
		HWPFDocument doc = new HWPFDocument(fis);
		Range rang = doc.getRange();
		text  = rang.text();
		fis.close();
		}else if(fileName.endsWith(".docx")){
			OPCPackage opcPackage = POIXMLDocument.openPackage(fileName);
			POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
			text = extractor.getText();
			extractor.close();
		}
		return text;
	}
	
	public static File[] findFile(String path){
		File file = new File(path);
		File[] files = file.listFiles();
		return files;
	}
	
	public static void run(String path) throws Exception{
		File[] files = findFile(path);
		File sumFile = new File(path+"\\"+"SUM.txt");
		FileOutputStream fos = new FileOutputStream(sumFile,true);
		String text;
		for(File item :files){
			text =readWord(item.getAbsolutePath());
			if(text!=null){
				byte[] content = text.getBytes();
				fos.write(content, 0, content.length);
				fos.write('\n');
			}
		}	
		fos.close();
		return ;
	}
	
}
