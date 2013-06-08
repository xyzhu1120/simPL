package com.simPL.visitor;

import com.simPL.compiler.SimpleNode;

public class MyFunc {

	/**
	 * @param args
	 */
	public SimPLSymbol param; //var
	
	public int level; //
	public SimPLSymbol body;
	public SimpleNode node;
	public ValueType paramType;
	public ValueType returnType;
	public MyFunc(SimPLSymbol theParam, int theLevel, SimPLSymbol theBody, SimpleNode theNode,ValueType theParamType, ValueType theReturn){
		param = theParam;
		level = theLevel;
		body = theBody;
		node = theNode;
		paramType = theParamType;
		returnType = theReturn;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
