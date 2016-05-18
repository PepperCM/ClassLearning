package randomInteger;


/**
* randomInteger/RandomIntegerHelper.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从TheMdule.idl
* 2016年3月19日 星期六 下午09时13分02秒 CST
*/


// ?H???[
abstract public class RandomIntegerHelper
{
  private static String  _id = "IDL:randomInteger/RandomInteger:1.0";

  public static void insert (org.omg.CORBA.Any a, randomInteger.RandomInteger that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static randomInteger.RandomInteger extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (randomInteger.RandomIntegerHelper.id (), "RandomInteger");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static randomInteger.RandomInteger read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_RandomIntegerStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, randomInteger.RandomInteger value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static randomInteger.RandomInteger narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof randomInteger.RandomInteger)
      return (randomInteger.RandomInteger)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      randomInteger._RandomIntegerStub stub = new randomInteger._RandomIntegerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static randomInteger.RandomInteger unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof randomInteger.RandomInteger)
      return (randomInteger.RandomInteger)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      randomInteger._RandomIntegerStub stub = new randomInteger._RandomIntegerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
