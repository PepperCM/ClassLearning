package MD5encode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Check {
	public static void srcMd5(File[] files) {
		File filea = new File("A.txt");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filea);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for(File item:files){
			try {
				fos.write( WorkSection.getMd5ByFile(item));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void encodeMd5(File[] files) {
		File filea = new File("B.txt");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filea);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for(File item:files){
			try {
				fos.write( WorkSection.getMd5ByFile(item));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean run() {
		String aMd5 = null,bMd5 = null;
		try {
			aMd5 = new String(WorkSection.getMd5ByFile(new File("A.txt")));
			bMd5 = new String(WorkSection.getMd5ByFile(new File("B.txt")));
		} catch (FileNotFoundException e) {
			//产生文件不存在异常，忽略掉，在界面上的表示是无响应。
		}
		
		return aMd5.equals(bMd5);
		
	}
}
