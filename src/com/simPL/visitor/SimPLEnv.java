package com.simPL.visitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.simPL.compiler.SIMPLConstants;
import com.simPL.compiler.SIMPLTreeConstants;

public class SimPLEnv implements SIMPLTreeConstants, SIMPLConstants{

	/**
	 * @param args
	 */
	
	private HashMap <String, Integer> curenv = null;
	
	private Vector<HashMap<String, Integer>> stack = null;
	
	public SimPLEnv(){
		
		stack = new Vector<HashMap<String, Integer>>();
		curenv = null;
	}
	
	public int EnterBlock(){
		curenv = new HashMap<String, Integer>();
		stack.add(curenv);
		return stack.size();
	}
	public boolean Exist(String name){
		return curenv.containsKey(name);
	}
	
	public int InsertAndUpdate(String name, Integer Type){
		if(curenv.containsKey(name)){
			curenv.put(name, Type);
			return 1;
		}else {
			curenv.put(name, Type);
			return 0;
		}
	}
	public void PrintStack(){
		for(int i = 0; i < stack.size();i++){
			Iterator<String> itKey = stack.get(i).keySet().iterator();
			Iterator<Integer> itValue = stack.get(i).values().iterator();
			while(itKey.hasNext()){
				for(int j = 0; j < i; j++)
					System.out.print("\t");
				String name = itKey.next();
				Integer value = itValue.next();
				System.out.print(name+":"+value+"\n");
			}
		}
	}
	
	public int LeaveBlock(){
		stack.remove(stack.size()-1);
		return stack.size();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimPLEnv env = new SimPLEnv();
		env.EnterBlock();
		if(!env.Exist("a"))
			env.InsertAndUpdate("a", 1);
		if(!env.Exist("a"))
			env.InsertAndUpdate("a", 2);
		if(!env.Exist("c"))
			env.InsertAndUpdate("c", 4);
		if(!env.Exist("b"))
			env.InsertAndUpdate("b", 3);
		env.EnterBlock();
		if(!env.Exist("a"))
			env.InsertAndUpdate("a", 222);
		if(!env.Exist("b"))
			env.InsertAndUpdate("b", 333);
		env.PrintStack();
	}

}
