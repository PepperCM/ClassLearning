package FilesCleanner;

import java.io.File;
import java.util.LinkedList;

/**
 * ���������ʵ��scan��ť��ɨ��������ҳ�Ŀ¼�����������������ļ�
 * ֻ�ܻ���ļ������ܻ���ļ���
 * ɾ����ʱ��Ҳ��ֻ��ɾ���ļ�������ɾ���ļ���
 * �ǵݹ����
 * @author Pepper
 *
 */

public class Scan {
	public  static LinkedList<File> getfiles(String extension,String keyword,long size,String path) {
		LinkedList<File> files = new LinkedList<>();
		LinkedList<File> dirs = new LinkedList<>();
		if(path==null||path.equals(""))
			return null;
		File fx = new File(path);
		File[] fs = fx.listFiles();
		for(File i :fs)
			System.out.println(i.getName());
		for (File item : fs)
			dirs.add(item);

		while (!dirs.isEmpty()) {
			File f = dirs.getFirst();
			dirs.removeFirst();
			if (f.isDirectory()) {
				fs = f.listFiles();
				for (File item : fs)
					dirs.add(item);
			} else {
				if((extension == null||f.getName().endsWith(extension))&&(keyword == null||f.getName().indexOf(keyword)!=-1)&&f.length()>=size)
				files.addLast(f);
			}
		}
		return files;
	}
}
