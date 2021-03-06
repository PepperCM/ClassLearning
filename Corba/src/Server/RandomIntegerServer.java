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
 *Description:    启动服务端的服务
 *Company:       
 *Version:          1.0.0
 *Author:           pantp
 *Date:              Jul 8, 2012
 */
public class RandomIntegerServer {

    //启动ORB以及等待远程客户机的调用的代码
    public static void main(String args[]) throws Exception {
        // -ORBInitialPort 1050
        args = new String[2];
        args[0] = "-ORBInitialPort";
        args[1] = "1050";//端口

        // 创建一个ORB实例
        ORB orb = ORB.init(args, null);
        System.out.println("server--->创建了ORB实例");

        // 得到一个RootPOA的引用，并激活POAManager
        org.omg.CORBA.Object obj=orb.resolve_initial_references("RootPOA");
        POA rootpoa = POAHelper.narrow(obj);
        rootpoa.the_POAManager().activate();

        System.out.println("server--->激活了POAManager");

        // 创建一个Impl实例
        RandomIntegerImpl randomIntegerImpl = new RandomIntegerImpl();
        
        System.out.println("server--->创建了Impl实例");

        // 从服务中得到对象的引用
        org.omg.CORBA.Object ref = rootpoa.servant_to_reference(randomIntegerImpl);
        RandomInteger href = RandomIntegerHelper.narrow(ref);

        System.out.println("server--->获取对象的引用");

        // 得到一个根名称的上下文
        org.omg.CORBA.Object objRef = orb
                .resolve_initial_references("NameService");
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

        System.out.println("server--->得到跟名称上下文");

        // 在命名上下文中绑定这个对象
        String name = "RandomInteger";
        NameComponent path[] = ncRef.to_name(name);
        ncRef.rebind(path, href);

        System.out.println("server--->绑定对象，等待客户端调用");

        // 启动线程服务，等待客户端调用
        orb.run();
    }
}
