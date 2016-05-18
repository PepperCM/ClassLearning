package FilesCleanner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

class Handler implements InvocationHandler{

	TableModel t = new DefaultTableModel();
	MyTableModel mt = new MyTableModel();
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.getName().equals("getColumnClass")){
			return  method.invoke(mt, args);
		}
		else {
			return  method.invoke(t, args);
		}
	}
	
}

public class ProxyTableModel{
	public static void main(String[] args) {
		TableModel p = (TableModel)Proxy.newProxyInstance(ProxyTableModel.class.getClassLoader(), new Class[]{TableModel.class}, new Handler());
		System.out.println(p.getRowCount());
	}
}
