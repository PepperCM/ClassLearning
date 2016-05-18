package Client;

import java.util.Date;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import randomInteger.RandomInteger;
import randomInteger.RandomIntegerHelper;

/**
 * 
 *Module:          HelloClient.java
 *Description:    �ͻ��˵ĳ�ʼ���Լ����õĴ���
 *Company:       
 *Version:          1.0.0
 *Author:           pantp
 *Date:              Jul 8, 2012
 */
public class RandomIntegerClient {

    static RandomInteger impl;

    static {
        System.out.println("�ͻ��˵ĳ�ʼ�����ÿ�ʼ......." + new Date());

        // -ORBInitialHost 127.0.0.1 -ORBInitialPort 1050
        String args[] = new String[4];
        args[0] = "-ORBInitialHost";
        args[1] = "127.0.0.1";// ����˵�IP��ַ
        args[2] = "-ORBInitialPort";
        args[3] = "1050";// ����˵Ķ˿�

        // ����һ��ORBʵ��
        ORB orb = ORB.init(args, null);

        // ��ȡ������������
        org.omg.CORBA.Object objRef = null;
        try {
            objRef = orb.resolve_initial_references("NameService");
        } catch (InvalidName e) {
            e.printStackTrace();
        }
        NamingContextExt neRef = NamingContextExtHelper.narrow(objRef);

        String name = "RandomInteger";
        try {
            impl = RandomIntegerHelper.narrow(neRef.resolve_str(name));
        } catch (NotFound e) {
            e.printStackTrace();
        } catch (CannotProceed e) {
            e.printStackTrace();
        } catch (org.omg.CosNaming.NamingContextPackage.InvalidName e) {
            e.printStackTrace();
        }

        System.out.println("�ͻ��˵ĳ�ʼ�����ý���......." + new Date());
    }
    
    public static void main(String args[]) throws Exception {
        sayHello();
    }
    // ����corba����ķ���
    public static void sayHello() {
        Integer u = impl.next();
        System.out.println(u);
    }
}