package MD5encode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class WorkSection {

	static byte[] key = new byte[500];
	static File keyFile;

	public static byte[] getMD5byBytes(byte[] contents) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(contents);
		return md5.digest();
	}

	public static byte[] getMd5ByFile(File file) throws FileNotFoundException {
		FileInputStream in = new FileInputStream(file);
		try {
			MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			return md5.digest();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static void encodeFile(File file) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream encodefos = new FileOutputStream(
				file.getParentFile().getAbsolutePath() + "\\encode_" + file.getName());
		byte[] contents = new byte[1];

		// 得到密钥
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey sk = secretKeyFactory.generateSecret(desKeySpec);

		// 创建加密工具类
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, sk);
		while(fis.read(contents)!=-1){
//			System.out.println(contents[0]);
			String  s = Integer.toBinaryString(contents[0]);
			if(s.length()<32)
				s = String.format("%032d", Integer.parseInt(s));
			
			encodefos.write(cipher.doFinal(s.getBytes()));
		}
		
		//转换输出

		fis.close();
		encodefos.close();
	}

	public static void decodeFile(File file) throws InvalidKeySpecException, NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		if (!file.getName().startsWith("encode_"))
			return;
		FileOutputStream fos = new FileOutputStream(
				file.getParentFile().getAbsolutePath() + "\\" + file.getName().substring(7));
		FileInputStream decodefis = new FileInputStream(file);

		byte[] contents = new byte[32];

		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey sk = secretKeyFactory.generateSecret(desKeySpec);

		// 创建加密工具类
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, sk);

		//转换输出
		while(decodefis.read(contents)!=-1){
			byte[] s = new byte[1];
			BigInteger b = new BigInteger(new String(cipher.doFinal(contents)), 2);
			s[0] = (byte) b.intValue();
			fos.write(s);
		}
		
		
		fos.close();
		decodefis.close();

	}
	
	
	public static void setKey(String path){
		keyFile = new File(path);
		try {
			key = getMd5ByFile(keyFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void setKey(byte[] a){
		key = a;
	}
	
	public static File[] getFiles(String path){
		Queue<File> q = new LinkedList<>();
		LinkedList<File> ans = new LinkedList<>();
		File root = new File(path);
		q.add(root);
		File f ;
		while(!q.isEmpty()){
			f = q.poll();
			if(!f.isDirectory())
				ans.add(f);
			else {
				q.addAll( Arrays.asList(f.listFiles()));
			}
		}
		File[] a = new File[0];
		a = ans.toArray(a);
		
		return a;
		
	}
 }
