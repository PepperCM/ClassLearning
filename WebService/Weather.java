
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
*/
/**
 * �����õ���webservice�õ�����Ԥ������
 * 
 * @author qsw-Myonlystar 2010-1-13����09:59:45
 */
public class Weather {
	/**
	 * ��ȡsoap����ͷ�����滻���еı�־����Ϊ�û����������
	 * 
	 * @param city
	 *            �û����������
	 * @return �û���Ҫ���͸���������soap����
	 */
	private  String getSupportCitySoapRequest(String city) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
				+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> "
				+ "<soap:Body><getSupportCity xmlns=\"http://WebXml.com.cn/\"><byProvinceName> "+city
				+ "</byProvinceName></getSupportCity></soap:Body></soap:Envelope>");
		return sb.toString();
	}
    private  String getWeatherSoapRequest(String city) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                        + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                        + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                        + "<soap:Body>    <getWeatherbyCityName xmlns=\"http://WebXml.com.cn/\">"
                        + "<theCityName>" + city
                        + "</theCityName>    </getWeatherbyCityName>"
                        + "</soap:Body></soap:Envelope>");
        return sb.toString();
    }
	/**
	 * �û���SOAP�����͸��������ˣ������ط������㷵�ص�������
	 * 
	 * @param city
	 *            �û�����ĳ�������
	 * @return �������˷��ص������������ͻ��˶�ȡ
	 * @throws Exception
	 */
	public  InputStream getSupportCitySoapInputStream(String city) throws Exception {
		try {
			String soap = getSupportCitySoapRequest(city);
			if (soap == null) {
				return null;
			}
			URL url = new URL("http://www.webxml.com.cn/WebServices/WeatherWebService.asmx");
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Length", Integer.toString(soap.length()));
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction", "http://WebXml.com.cn/getSupportCity");
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			osw.write(soap);
			osw.flush();
			osw.close();
			InputStream is = conn.getInputStream();
			// System.out.println(is.toString());
			return is;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    public  InputStream getWeatherSoapInputStream(String city) throws Exception {
        try {
            String soap = getWeatherSoapRequest(city);
            if (soap == null) {
                return null;
            }
            URL url = new URL(
                    "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx");
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Length", Integer.toString(soap
                    .length()));
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("SOAPAction",
                    "http://WebXml.com.cn/getWeatherbyCityName");
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
            osw.write(soap);
            osw.flush();
            osw.close();
            InputStream is = conn.getInputStream();
            //System.out.println(is.toString());
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
	
	/**
	 * ���Ժ���
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		Weather w = new Weather();
		InputStream is = w.getSupportCitySoapInputStream("����");
		DataInputStream dis = new DataInputStream(is);
		Reader r = new InputStreamReader(dis, "UTF-8");
		String s;
		int b;
		int flag = 0;
		while ((b = r.read()) != -1) {
			System.out.print((char) b);
			if (b == '/') {
				flag = 1;
			}
			if( b =='>'&&flag==1){
				System.out.println();
				flag = 0;
			}
		}
		
		is = w.getWeatherSoapInputStream("����");
		r = new InputStreamReader(is, "utf-8");
		flag = 0;
		System.out.println("����������");
		while ((b = r.read()) != -1) {
			System.out.print((char) b);
			if (b == '/') {
				flag = 1;
			}
			if( b =='>'&&flag==1){
				System.out.println();
				flag = 0;
			}
		}
		
	}
	
	
}