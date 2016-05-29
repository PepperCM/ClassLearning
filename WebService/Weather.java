
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
 * 类作用调用webservice得到天气预报服务
 * 
 * @author qsw-Myonlystar 2010-1-13上午09:59:45
 */
public class Weather {
	/**
	 * 获取soap请求头，并替换其中的标志符号为用户的输入符号
	 * 
	 * @param city
	 *            用户输入城市名
	 * @return 用户将要发送给服务器的soap请求
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
	 * 用户把SOAP请求发送给服务器端，并返回服务器点返回的输入流
	 * 
	 * @param city
	 *            用户输入的城市名称
	 * @return 服务器端返回的输入流，供客户端读取
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
	 * 测试函数
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		Weather w = new Weather();
		InputStream is = w.getSupportCitySoapInputStream("陕西");
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
		
		is = w.getWeatherSoapInputStream("西安");
		r = new InputStreamReader(is, "utf-8");
		flag = 0;
		System.out.println("西安天气：");
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