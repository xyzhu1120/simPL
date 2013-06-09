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
	    while(true){
		    try
		    {
		      SimpleNode n = SIMPL.Start();
		      Object result = n.jjtAccept(new SIMPLVisitorImpl(), null);
		      SimPLSymbol r = (SimPLSymbol)result;
		      //if(r.type == ValueType.INTEGER)
		     r.Print();
		      //if(r.type == ValueType.EXCEPTION)
			  //  	 System.out.println((String)r.value);
		      //n.dump("");
		    }
		    catch (Exception e)
		    {
		      System.out.println("Oops.");
		      System.out.println(e.getMessage());
		    }
		    catch (Error e)
		    {
			      System.out.println("Oops.");
			      System.out.println(e.getMessage());
		    }
		    System.out.println("Please input the expressions:");
		    SIMPL.ReInit(System.in);
	    }
	}

}
