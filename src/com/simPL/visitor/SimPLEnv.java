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
	public SimPLEnv Duplicate(){
		SimPLEnv result = new SimPLEnv();
		for(int i = 0; i < stack.size();i++){
			HashMap<String, SimPLSymbol> frame = new HashMap<String, SimPLSymbol>();
			result.stack.add(frame);
			
			Iterator<String> itKey = stack.get(i).keySet().iterator();
			Iterator<SimPLSymbol> itValue = stack.get(i).values().iterator();
			while(itKey.hasNext()){
				String name = itKey.next();
				SimPLSymbol value = itValue.next();
				frame.put(name, value);
			}
		}
		return result;
	}
	public int EnterBlock(){
		stack.add(new HashMap<String, SimPLSymbol>());
		curenv = stack.get(stack.size()-1);
		return stack.size();
	}
	public int LeaveBlock(){
		stack.remove(stack.size()-1);
		curenv = null;
		return stack.size();
	}
	public int GetDepth(){
		return stack.size();
	}
	public int PopStackToDepth(int depth){
		if(depth >= GetDepth())
			return 0;
		int cnt = 0;
		for(int i = stack.size()-1; i >= depth; i--,cnt++)
			stack.remove(i);
		return cnt;
	}
	public boolean LocalExist(String name){
		curenv = stack.get(stack.size()-1);
		return curenv.containsKey(name);
	}
	
	public int LocalSetSymbol(String name, SimPLSymbol symbol){
		SimPLSymbol value = symbol.Duplicate();
		curenv = stack.get(stack.size()-1);
		if(curenv.containsKey(name)){
			curenv.put(name, value);
			return 0;
		}else {
			curenv.put(name, value);
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
		SimPLSymbol value = new SimPLSymbol(newValue.type,newValue.value);
		int i = 0;
		for(; i < stack.size(); i++){
			if(stack.get(stack.size()-1-i).containsKey(name)){
				stack.get(stack.size()-1-i).put(name,value);
				return i;
			}
		}
		return -1;
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
			env.LocalSetSymbol("a", new SimPLSymbol(ValueType.INTEGER));
		if(!env.LocalExist("a"))
			env.LocalSetSymbol("a", new SimPLSymbol(ValueType.INTEGER));
		if(!env.LocalExist("c"))
			env.LocalSetSymbol("c", new SimPLSymbol(ValueType.INTEGER));
		if(!env.LocalExist("b"))
			env.LocalSetSymbol("b", new SimPLSymbol(ValueType.INTEGER));
		env.EnterBlock();
		if(!env.LocalExist("a"))
			env.LocalSetSymbol("a", new SimPLSymbol(ValueType.INTEGER));
		if(!env.LocalExist("b"))
			env.LocalSetSymbol("b", new SimPLSymbol(ValueType.INTEGER));
		env.PrintStack();
	}
}
