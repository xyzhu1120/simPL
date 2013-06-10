package com.simPL.visitor;

import java.util.ArrayList;
import java.util.List;

enum ValueType{
	INTEGER,BOOLEAN,LIST,UNIT,PAIR,VAR,FUN,FREE,UNDEF,EXCEPTION
}
public class SimPLSymbol {
	
	public ValueType type;
	public Object value;
	
	
	public static boolean SameList(SimPLSymbol left, SimPLSymbol right){
		
		if((left.type == ValueType.LIST && right.type != ValueType.LIST)
				||(left.type != ValueType.LIST && right.type == ValueType.LIST))
			return false;
		
		if(right.type != left.type)
			return false;
		
		if(left.type != ValueType.LIST)
		{
			return Equal(left,right);
		}
		if(left.value == null || right.value == null){
			return left.value == null && right.value == null;
		}
		ArrayList<SimPLSymbol> leftlist = ((ArrayList<SimPLSymbol>)left.value);
		ArrayList<SimPLSymbol> rightlist = ((ArrayList<SimPLSymbol>)right.value);
		if(leftlist.size()!=rightlist.size())
			return false;
		for(int i = 0; i != leftlist.size(); i++){
			if(!Equal(leftlist.get(i),rightlist.get(i)))
					return false;
		}
		return true;
	}
	public static boolean Equal(SimPLSymbol left, SimPLSymbol right){
		if(left.type != right.type)
			return false;
		if(left.type == ValueType.INTEGER)
			return Integer.parseInt(left.value.toString())== Integer.parseInt(right.value.toString());
		if(left.type == ValueType.BOOLEAN)
			return left.value.toString() == right.value.toString();
		if(left.type == ValueType.LIST){
			return SameList(left,right);
		}
		if(left.type == ValueType.UNIT)
			return true;
		if(left.type == ValueType.PAIR)
			return ((MyPair)left.value).first==((MyPair)right.value).first && ((MyPair)left.value).second==((MyPair)right.value).second;
		if(left.type == ValueType.VAR)
			return left.value.toString() == right.value.toString();
		if(left.type == ValueType.FUN){
			return ((MyFunc)left.value).level == ((MyFunc)right.value).level;
		}
		if(left.type == ValueType.FREE)
			return true;
		if(left.type == ValueType.UNDEF)
			return true;
		if(left.type == ValueType.EXCEPTION)
			return true;
		return true;
	}
	public void Print(){
		//System.out.println("**PROGRAM OUTPUT**:");
		if(type == ValueType.INTEGER)
			System.out.print(value.toString());
		if(type == ValueType.BOOLEAN)
			System.out.print(value.toString());
		if(type == ValueType.LIST) {
			if(value == null) {
				System.out.println("nil list");
				return;
			}
			List<SimPLSymbol> list = (List<SimPLSymbol>)value;
			System.out.print('[');
			for(int i =0; i < list.size();i++){
				list.get(i).Print();
				if ( i < list.size() - 1 )
					System.out.print(", ");
			}
			System.out.print("]\n");
		}
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
		if(type == ValueType.UNDEF)
			System.out.println("undefine");
	}
	
	public SimPLSymbol(ValueType theType){
		//if(theType == ValueType.EXCEPTION)
			//System.err.println("exception happens");
		type = theType;
		if(type==ValueType.BOOLEAN)
			value = "true";
		if(type == ValueType.INTEGER)
			value = "1";
		if(type == ValueType.LIST)
			value = null;
	}
	public SimPLSymbol Duplicate(){
		SimPLSymbol result = new SimPLSymbol(type, value);
		if(type == ValueType.FUN){
			MyFunc origin = (MyFunc)value;
			MyFunc tmp = new MyFunc(origin.param.Duplicate(), origin.level, origin.body.Duplicate(), origin.node, origin.paramType, origin.paramType);
			result.value = tmp;
		}else if(type == ValueType.LIST)
		{
			if(value == null)
				return result;
			
			List<SimPLSymbol> origin = (List<SimPLSymbol>)value;
			List<SimPLSymbol> newlist = new ArrayList<SimPLSymbol>();
			for(int i = 0; i < origin.size(); i++){
				newlist.add(origin.get(i).Duplicate());
			}
			result.value = newlist;
		}else if(type == ValueType.PAIR)
		{
			MyPair origin = (MyPair)value;
			MyPair newpair = new MyPair(origin.first.Duplicate(),origin.second.Duplicate());
			result.value = newpair;
		}
		return result;
	}
	public SimPLSymbol(ValueType theType, Object theValue){
		//if(theType == ValueType.EXCEPTION)
		//	System.err.println((String)theValue);
		type = theType;
		value = theValue;
	}
	

}
