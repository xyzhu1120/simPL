package com.simPL.visitor;

public class SimPLSymbol {

	public int type;
	public Object value;
	
	SimPLSymbol(int theType){
		type = theType;
	}
	SimPLSymbol(int theType, Object theValue){
		type = theType;
		value = theValue;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
