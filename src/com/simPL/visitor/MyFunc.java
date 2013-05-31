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
	public MyFunc(SimPLSymbol theParam, int theLevel, SimPLSymbol theBody, SimpleNode theNode){
		param = theParam;
		level = theLevel;
		body = theBody;
		node = theNode;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
