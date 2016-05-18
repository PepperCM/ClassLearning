package Server;


import java.util.Random;

import randomInteger.RandomIntegerPOA;

/**
 * 
 *Module:          HelloImpl.java
 *Description:    服务端实现sayHello()方法
 *Company:       
 *Version:          1.0.0
 *Author:           pantp
 *Date:              Jul 8, 2012
 */
public class RandomIntegerImpl extends RandomIntegerPOA {

	static Random r = new Random();
	@Override
	public int next() {
		return r.nextInt(100);
	}
}