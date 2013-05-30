package com.simPL.visitor;

enum ValueType{
	INTEGER,BOOLEAN,LIST,UNIT,PAIR,VAR,FUN,EXCEPTION
}
public class SimPLSymbol {
	
	public ValueType type;
	public Object value;
	
	SimPLSymbol(ValueType theType){
		if(theType == ValueType.EXCEPTION)
			System.err.println("exception happens");
		type = theType;
	}
	SimPLSymbol(ValueType theType, Object theValue){
		if(theType == ValueType.EXCEPTION)
			System.err.println((String)theValue);
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
