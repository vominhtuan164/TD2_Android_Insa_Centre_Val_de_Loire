package andro.jf.androclassloader;

import java.io.Serializable;

public class Profil implements Cloneable, Serializable {

	public String name;
	public int age;
	private CreditCard cc;

	public Profil(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public void setCreditCard(CreditCard c)
	{
		cc = c;
	}

	public CreditCard getCreditCard()
	{
		return cc;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}

