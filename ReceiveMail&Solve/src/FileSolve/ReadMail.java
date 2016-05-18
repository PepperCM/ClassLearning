package FileSolve;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import sun.misc.BASE64Decoder;

/**
 * 实现从新浪邮箱下载附件功能
 * 
 * @author Pepper
 *
 */
public class ReadMail {
	static String path;

	public static void readMail(String spath, String musername, String mpassword) throws IOException, MessagingException   {
		path = spath;
		String host = "pop.sina.com";
		String username = musername;
		String password = mpassword;

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Store store = session.getStore("pop3");

		store.connect(host, username, password);

		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);

		Message message[] = folder.getMessages();

		for (Message item : message) {
			solveMessage(item);
		}
		folder.close(true);
		store.close();

	}

	private static void solveMessage(Message item) throws IOException, MessagingException  {

		String disposition;
		BodyPart part;
		Multipart mp = (Multipart) item.getContent();

		int mpCount = mp.getCount();
		for (int m = 0; m < mpCount; m++) {
			part = mp.getBodyPart(m);
			disposition = part.getDisposition();

			if (disposition != null && disposition.equals(Part.ATTACHMENT)) {
				saveAttach(part);
			}
		}

	}

	private static void saveAttach(BodyPart part) throws MessagingException, IOException  {
		String temp = part.getFileName();
		String s = temp.substring(8, temp.indexOf("?="));
		String fileName = base64Decoder(s);

		if (fileName.contains("报告")) {
			System.out.println("附件:" + fileName);
			createExcel(part, fileName);
		}
	}

	private static void createExcel(BodyPart part, String fileName) throws IOException, MessagingException {

		InputStream in = part.getInputStream();
		FileOutputStream fos = new FileOutputStream(new File(path + "\\" + fileName));
		byte[] content = new byte[255];
		while ((in.read(content)) != -1) {
			fos.write(content);
		}
		fos.close();
		in.close();
	}

	private static String base64Decoder(String s) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(s);
		return (new String(b));
	}

}
