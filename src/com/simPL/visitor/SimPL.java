package com.simPL.visitor;

import com.simPL.compiler.SIMPL;
import com.simPL.compiler.SimpleNode;

public class SimPL {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	    System.out.println("Reading from standard input...");
	    System.out.print("Enter an expression like \"1+(2+3)*var;\" :");
	    new SIMPL(System.in);
	    try
	    {
	      SimpleNode n = SIMPL.Start();
	      Object result = n.jjtAccept(new SIMPLVisitorImpl(), null);
	      SimPLSymbol r = (SimPLSymbol)result;
	      //if(r.type == ValueType.INTEGER)
	    	 System.out.println((String)r.value);
	      if(r.type == ValueType.EXCEPTION)
		    	 System.out.println((String)r.value);
	      //n.dump("");
	    }
	    catch (Exception e)
	    {
	      System.out.println("Oops.");
	      System.out.println(e.getMessage());
	    }
	}

}
