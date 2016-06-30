package org.cyberpwn.phantom.lang;

public class GTriset<A, B, C>
{
	private A a;
	private B b;
	private C c;
	
	public GTriset(A a, B b, C c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public A getA()
	{
		return a;
	}
	
	public void setA(A a)
	{
		this.a = a;
	}
	
	public B getB()
	{
		return b;
	}
	
	public void setB(B b)
	{
		this.b = b;
	}
	
	public C getC()
	{
		return c;
	}
	
	public void setC(C c)
	{
		this.c = c;
	}
}
