package com.simPL.visitor;

enum ValueType{
	INTEGER,BOOLEAN,LIST,UNIT,PAIR,VAR
}
public class SimPLSymbol {
	
	public ValueType type;
	public Object value;
	
	SimPLSymbol(ValueType theType){
		type = theType;
	}
	SimPLSymbol(ValueType theType, Object theValue){
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
