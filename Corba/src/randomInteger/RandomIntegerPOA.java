package randomInteger;


/**
* randomInteger/RandomIntegerPOA.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从TheMdule.idl
* 2016年3月19日 星期六 下午09时13分02秒 CST
*/


// ?H???[
public abstract class RandomIntegerPOA extends org.omg.PortableServer.Servant
 implements randomInteger.RandomIntegerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("next", new java.lang.Integer (0));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // randomInteger/RandomInteger/next
       {
         int $result = (int)0;
         $result = this.next ();
         out = $rh.createReply();
         out.write_long ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:randomInteger/RandomInteger:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public RandomInteger _this() 
  {
    return RandomIntegerHelper.narrow(
    super._this_object());
  }

  public RandomInteger _this(org.omg.CORBA.ORB orb) 
  {
    return RandomIntegerHelper.narrow(
    super._this_object(orb));
  }


} // class RandomIntegerPOA
