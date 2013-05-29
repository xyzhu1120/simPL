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
	
	private HashMap <String, SimPLSymbol> curenv = null;
	
	private Vector<HashMap<String, SimPLSymbol>> stack = null;
	
	public SimPLEnv(){
		
		stack = new Vector<HashMap<String, SimPLSymbol>>();
		curenv = null;
	}
	
	public int EnterBlock(){
		curenv = new HashMap<String, SimPLSymbol>();
		stack.add(curenv);
		return stack.size();
	}
	public int LeaveBlock(){
		stack.remove(stack.size()-1);
		return stack.size();
	}
	
	public boolean LocalExist(String name){
		return curenv.containsKey(name);
	}
	
	public int LocalSetSymbol(String name, SimPLSymbol symbol){
		if(curenv.containsKey(name)){
			curenv.put(name, symbol);
			return 0;
		}else {
			curenv.put(name, symbol);
			return 1;
		}
	}
	
	public boolean GlobalExist(String name){
		for(int i = 0; i < stack.size(); i++){
			if(stack.get(stack.size()-1-i).containsKey(name))
				return true;
		}
		return false;
	}
	
	public SimPLSymbol GlobalGetSymbol(String name){
		for(int i = 0; i < stack.size(); i++){
			if(stack.get(stack.size()-1-i).containsKey(name))
				return stack.get(stack.size()-1-i).get(name);
		}
		return null;
	}
	
	public int GlobalSetSymbol(String name, SimPLSymbol newValue){
		for(int i = 0; i < stack.size(); i++){
			if(stack.get(stack.size()-1-i).containsKey(name)){
				stack.get(stack.size()-1-i).put(name,newValue);
				return 1;
			}
		}
		return 0;
	}
	
	
	public void PrintStack(){
		for(int i = 0; i < stack.size();i++){
			Iterator<String> itKey = stack.get(i).keySet().iterator();
			Iterator<SimPLSymbol> itValue = stack.get(i).values().iterator();
			while(itKey.hasNext()){
				for(int j = 0; j < i; j++)
					System.out.print("\t");
				String name = itKey.next();
				SimPLSymbol value = itValue.next();
				System.out.print(name+":"+value.type+"\n");
			}
		}
	}
	

	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimPLEnv env = new SimPLEnv();
		env.EnterBlock();
		if(!env.LocalExist("a"))
			env.LocalSetSymbol("a", new SimPLSymbol(1));
		if(!env.LocalExist("a"))
			env.LocalSetSymbol("a", new SimPLSymbol(2));
		if(!env.LocalExist("c"))
			env.LocalSetSymbol("c", new SimPLSymbol(3));
		if(!env.LocalExist("b"))
			env.LocalSetSymbol("b", new SimPLSymbol(4));
		env.EnterBlock();
		if(!env.LocalExist("a"))
			env.LocalSetSymbol("a", new SimPLSymbol(22));
		if(!env.LocalExist("b"))
			env.LocalSetSymbol("b", new SimPLSymbol(33));
		env.PrintStack();
	}

}
