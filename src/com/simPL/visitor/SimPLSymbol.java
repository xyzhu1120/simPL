package com.simPL.visitor;

enum ValueType{
	INTEGER,BOOLEAN,LIST,UNIT,PAIR,VAR,FUN,FREE,EXCEPTION
}
public class SimPLSymbol {
	
	public ValueType type;
	public Object value;
	
	
	public void Print(){
		if(type == ValueType.INTEGER)
			System.out.println(value.toString());
		if(type == ValueType.BOOLEAN)
			System.out.println(value.toString());
		if(type == ValueType.LIST)
			System.out.println("list");
		if(type == ValueType.UNIT)
			System.out.println("unit");
		if(type == ValueType.PAIR){
			System.out.println("Pair:");
			System.out.println("(");
			SimPLSymbol first = ((MyPair)value).first;
			
			SimPLSymbol second = ((MyPair)value).second;
			first.Print();
			System.out.println(",");
			second.Print();
			System.out.println(")");
		}
		if(type == ValueType.VAR)
			System.out.println(value.toString());
		if(type == ValueType.FUN)
			System.out.println("Fun");
		if(type == ValueType.EXCEPTION)
			System.out.println(value.toString());
	}
	
	public SimPLSymbol(ValueType theType){
		//if(theType == ValueType.EXCEPTION)
		//	System.err.println("exception happens");
		type = theType;
	}
	public SimPLSymbol(ValueType theType, Object theValue){
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
