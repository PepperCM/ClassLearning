package randomInteger;

/**
* randomInteger/RandomIntegerHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从TheMdule.idl
* 2016年3月19日 星期六 下午09时13分02秒 CST
*/


// ?H???[
public final class RandomIntegerHolder implements org.omg.CORBA.portable.Streamable
{
  public randomInteger.RandomInteger value = null;

  public RandomIntegerHolder ()
  {
  }

  public RandomIntegerHolder (randomInteger.RandomInteger initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = randomInteger.RandomIntegerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    randomInteger.RandomIntegerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return randomInteger.RandomIntegerHelper.type ();
  }

}
