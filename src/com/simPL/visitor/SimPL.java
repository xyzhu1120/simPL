package com.simPL.visitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.simPL.compiler.SIMPL;
import com.simPL.compiler.SimpleNode;

public class SimPL {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String banner = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader("banner"));
			String tmpstring = "";
			while((tmpstring = reader.readLine()) != null)
				banner = banner + tmpstring + '\n';
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("\n"+banner);
		boolean filemode = false;
		String filename = "";
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("-f")){
				filemode = true;
				filename = args[1];
			}
		}
		if(filemode){
			File file = new File(filename);
			try {
				new SIMPL(new InputStreamReader(new FileInputStream(file)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block	
				e.printStackTrace();
			}
		}else{
			 new SIMPL(System.in);
		}
	    System.out.println("Reading from standard input...");
	   
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
