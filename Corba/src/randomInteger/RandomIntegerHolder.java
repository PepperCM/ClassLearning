package randomInteger;

/**
* randomInteger/RandomIntegerHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��TheMdule.idl
* 2016��3��19�� ������ ����09ʱ13��02�� CST
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
