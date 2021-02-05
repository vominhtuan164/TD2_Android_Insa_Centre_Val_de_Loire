package andro.jf.androclassloader;

import java.io.Serializable;

public class CreditCard implements Serializable {

	// This is very sensitive !
	private int number = 0;
	
	public CreditCard(int i) {
		number = i;
	}
	
	public int getNumber()
	{
		return number;
	}
}

