package Server;

import java.util.Random;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;


import randomInteger.RandomInteger;
import randomInteger.RandomIntegerHelper;

import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NameComponent;

/**
 * 
 *Module:          HelloServer.java
 *Description:    ��������˵ķ���
 *Company:       
 *Version:          1.0.0
 *Author:           pantp
 *Date:              Jul 8, 2012
 */
public class RandomIntegerServer {

    //����ORB�Լ��ȴ�Զ�̿ͻ����ĵ��õĴ���
    public static void main(String args[]) throws Exception {
        // -ORBInitialPort 1050
        args = new String[2];
        args[0] = "-ORBInitialPort";
        args[1] = "1050";//�˿�

        // ����һ��ORBʵ��
        ORB orb = ORB.init(args, null);
        System.out.println("server--->������ORBʵ��");

        // �õ�һ��RootPOA�����ã�������POAManager
        org.omg.CORBA.Object obj=orb.resolve_initial_references("RootPOA");
        POA rootpoa = POAHelper.narrow(obj);
        rootpoa.the_POAManager().activate();

        System.out.println("server--->������POAManager");

        // ����һ��Implʵ��
        RandomIntegerImpl randomIntegerImpl = new RandomIntegerImpl();
        
        System.out.println("server--->������Implʵ��");

        // �ӷ����еõ����������
        org.omg.CORBA.Object ref = rootpoa.servant_to_reference(randomIntegerImpl);
        RandomInteger href = RandomIntegerHelper.narrow(ref);

        System.out.println("server--->��ȡ���������");

        // �õ�һ�������Ƶ�������
        org.omg.CORBA.Object objRef = orb
                .resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

        System.out.println("server--->�õ�������������");

        // �������������а��������
        String name = "RandomInteger";
        NameComponent path[] = ncRef.to_name(name);
        ncRef.rebind(path, href);

        System.out.println("server--->�󶨶��󣬵ȴ��ͻ��˵���");

        // �����̷߳��񣬵ȴ��ͻ��˵���
        orb.run();
    }
}
