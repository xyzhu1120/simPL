/**
 * 
 */
package com.simPL.visitor;

import com.simPL.compiler.ASTAddMinus;
import com.simPL.compiler.ASTAndOr;
import com.simPL.compiler.ASTApplication;
import com.simPL.compiler.ASTAssignment;
import com.simPL.compiler.ASTBool;
import com.simPL.compiler.ASTBracket;
import com.simPL.compiler.ASTCompare;
import com.simPL.compiler.ASTCond;
import com.simPL.compiler.ASTExpression;
import com.simPL.compiler.ASTFunction;
import com.simPL.compiler.ASTInt;
import com.simPL.compiler.ASTLet;
import com.simPL.compiler.ASTList;
import com.simPL.compiler.ASTMulDiv;
import com.simPL.compiler.ASTNil;
import com.simPL.compiler.ASTPair;
import com.simPL.compiler.ASTSTART;
import com.simPL.compiler.ASTUnaryExp;
import com.simPL.compiler.ASTUnit;
import com.simPL.compiler.ASTVar;
import com.simPL.compiler.ASTWhile;
import com.simPL.compiler.SIMPL;
import com.simPL.compiler.SIMPLVisitor;
import com.simPL.compiler.SimpleNode;
import com.simPL.compiler.SIMPLConstants;
import com.simPL.compiler.Token;

import java.util.ArrayList;
import java.util.List;
/**
 *
 */
public class SIMPLVisitorImpl implements SIMPLVisitor, SIMPLConstants {
	
	public SimPLEnv env = new SimPLEnv();

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.SimpleNode, java.lang.Object)
	 */
	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTSTART, java.lang.Object)
	 */
	@Override
	public Object visit(ASTSTART node, Object data) {
		// TODO Auto-generated method stub
		//return node.childrenAccept(this, data);
		env.EnterBlock();
		SimPLSymbol result = (SimPLSymbol) node.jjtGetChild(0).jjtAccept(this, data);
		env.LeaveBlock();
		if(result.type == ValueType.VAR){
			if(env.GlobalExist((String)result.value)){
				return env.GlobalGetSymbol((String)result.value);
			}else
				return new SimPLSymbol(ValueType.EXCEPTION,"not var "+result.value +" exists"); 
		}
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTAssignment, java.lang.Object)
	 */
	@Override
	public Object visit(ASTAssignment node, Object data) {
		// TODO Auto-generated method stub
		//node.childrenAccept(this, data);
		int num = node.jjtGetNumChildren();
		//System.out.println("in Assignment:"+num);
		SimPLSymbol left = (SimPLSymbol)node.jjtGetChild(0).jjtAccept(this, data);
		if(num > 1){
			SimPLSymbol right = (SimPLSymbol)node.jjtGetChild(1).jjtAccept(this, data);
			if(left.type == ValueType.VAR){
				if(env.GlobalExist((String)left.value)){
					SimPLSymbol ori = env.GlobalGetSymbol((String)left.value);
					//SimPLSymbol right2= new SimPLSymbol(right.type,right.value);
					String rightName = "";
					if(right.type==ValueType.VAR){
						rightName = right.value.toString();
						right = env.GlobalGetSymbol(right.value.toString());
						if(right.type == ValueType.FREE)
							return new SimPLSymbol(ValueType.EXCEPTION,"value of var should never be free");
					}
					if(ori.type == ValueType.FREE && right.type == ValueType.FREE){
						
					}else if(ori.type == ValueType.FREE){
						env.GlobalSetSymbol(left.value.toString(), right);
					}else if(right.type == ValueType.FREE){
						env.GlobalSetSymbol(rightName, left);
					}else if(ori.type == right.type)
					{
						env.GlobalSetSymbol((String)left.value, right);
					}else{
						return new SimPLSymbol(ValueType.EXCEPTION,"type not match in assignment");						
					}
				}else{
					return new SimPLSymbol(ValueType.EXCEPTION,"var "+(String)left.value+" not exist in assignment");
				}
			}else{
				return new SimPLSymbol(ValueType.EXCEPTION,"not a left var in assignment");
			}
			SimPLSymbol result = new SimPLSymbol(ValueType.UNIT);
			if(node.jjtGetFirstToken().image == "head" || node.jjtGetFirstToken().image == "tail" || node.jjtGetFirstToken().image == "fst" || node.jjtGetFirstToken().image == "snd"){
				return new SimPLSymbol(ValueType.EXCEPTION,"cannot operate on a unit");
			}
			return result;
		}
		
		List<Token> list= new ArrayList<Token>();
		Token cur = node.jjtGetFirstToken();
		while(cur != ((SimpleNode)node.jjtGetChild(0)).jjtGetFirstToken()){
			list.add(0,cur);
			cur = cur.next;
		}
		
		SimPLSymbol n = left;
		for(int i = 0; i < list.size();i++){
			//SimPLSymbol n = new SimPLSymbol(left.type,left.value);
			cur = list.get(i);
			if(n.type == ValueType.VAR){
				if(env.GlobalExist((String)n.value)){
					n = env.GlobalGetSymbol((String)n.value);
				}else
					return new SimPLSymbol(ValueType.EXCEPTION,"not var "+n.value +" exists"); 
			}
			try{
				if(cur.image == "head"){
					if(n.type != ValueType.LIST){
						return new SimPLSymbol(ValueType.EXCEPTION,"head argument is not a list");
					}else{
						if(n.value == null)//empty list
							return new SimPLSymbol(ValueType.EXCEPTION,"head on nil");
						else {
							n = ((ArrayList<SimPLSymbol>)(n.value)).get(0);
							cur = cur.next;
							continue;
						}
					}
				}else if(cur.image == "tail"){
					if(n.type != ValueType.LIST){
						return new SimPLSymbol(ValueType.EXCEPTION,"tail argument is not a list");
					}else{
						if(n.value == null)//empty list
							return new SimPLSymbol(ValueType.EXCEPTION,"tail on nil");
						else{
							SimPLSymbol result = n;
							if(((ArrayList<SimPLSymbol>)result.value).size()==1){
								n.value=null;
								continue;
							}else if(((ArrayList<SimPLSymbol>)result.value).size()==1){
								return new SimPLSymbol(ValueType.EXCEPTION,"tail on nil");
							}
							
							((ArrayList<SimPLSymbol>)result.value).remove(0);
							n = result;
							cur = cur.next;
							
							continue;
						}
					}
				}else if(cur.image == "fst"){
					if(n.type != ValueType.PAIR){
						return new SimPLSymbol(ValueType.EXCEPTION,"fst argument is not a pair");
					}else{
						
							MyPair p  = (MyPair)(n.value);
							n = (SimPLSymbol)p.first;
							cur = cur.next;
							continue;
					}
				}else if(cur.image == "snd"){
					if(n.type != ValueType.PAIR){
						return new SimPLSymbol(ValueType.EXCEPTION,"snd argument is not a pair");
					}else{
						
							MyPair p  = (MyPair)(n.value);
							n = (SimPLSymbol)p.second;
							cur = cur.next;
							continue;
					}
				}else {
					return new SimPLSymbol(ValueType.EXCEPTION,"error in fst,head,tail,snd");
					
				}
			}catch (Exception e){
				return new SimPLSymbol(ValueType.EXCEPTION,"error in fst,head,tail,snd");
			}
			//cur = cur.next;
		}
		
		return n;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTListInsert, java.lang.Object)
	 */
	@Override
	public Object visit(ASTList node, Object data) {
		// TODO Auto-generated method stub
		int num = node.jjtGetNumChildren();
		//System.out.println("in ADDMINUS:"+num);
		
		Object first = node.jjtGetChild(0).jjtAccept(this, data);
		if(num > 1){
			try {
				SimPLSymbol left = (SimPLSymbol)first;
				if(left.type == ValueType.VAR){
					if(env.GlobalExist((String)left.value)){
						left = env.GlobalGetSymbol((String)left.value);
					}else{
						return new SimPLSymbol(ValueType.EXCEPTION,"no such symbol "+left.value.toString());
					}
				}
				SimPLSymbol right = null;
				for(int i = 1; i < num; i++){
					right = (SimPLSymbol)node.jjtGetChild(i).jjtAccept(this, data);
					if(right.type == ValueType.VAR){
						if(env.GlobalExist((String)right.value)){
							right = env.GlobalGetSymbol((String)right.value);
						}else{
							return new SimPLSymbol(ValueType.EXCEPTION,"no such symbol "+right.value.toString());
						}
					}
					if(right.type == ValueType.LIST){
						if(right.value==null){
							//left is the first element
							List<SimPLSymbol> list = new ArrayList<SimPLSymbol>();
							list.add(left);
							right.value = list;
						}else{
							try{
								List<SimPLSymbol> list = (ArrayList<SimPLSymbol>)right.value;
								if(SameListLevel(left,list.get(0))){
									//list.add(left);
									list.add(0,left);
									//right.value=list;
								}else{
									return new SimPLSymbol(ValueType.EXCEPTION,"list level not matched");
								}
							}catch (Exception e){
								return new SimPLSymbol(ValueType.EXCEPTION,"exception in list");
							}
						}
						
					}else{
						return new SimPLSymbol(ValueType.EXCEPTION,"right of list op is not a list");
					}
					left = right;
				}
				return right; 
			}
			catch (Exception e){
				return new SimPLSymbol(ValueType.EXCEPTION,"Err in AddMinus");
			}
		}
		
		return first;
	}

	private boolean SameListLevel(SimPLSymbol left, SimPLSymbol right){
		if(left.type == ValueType.VAR)
			left = env.GlobalGetSymbol((String)left.value);
		if(right.type == ValueType.VAR)
			right = env.GlobalGetSymbol((String)right.value);
		if(right.type != left.type)
			return false;
		if(left.type != ValueType.LIST)
		{
			//function judgement
			return true;
		}
		if((ArrayList<SimPLSymbol>)left.value == null)
			return true;
		if((ArrayList<SimPLSymbol>)right.value == null)
			return true;
		return SameListLevel(((ArrayList<SimPLSymbol>)left.value).get(0),((ArrayList<SimPLSymbol>)right.value).get(0));
	}
	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTAndOr, java.lang.Object)
	 */
	@Override
	public Object visit(ASTAndOr node, Object data) {
		// TODO Auto-generated method stub
		int num = node.jjtGetNumChildren();
		//System.out.println("in AndOr:"+num);
		
		Object first = node.jjtGetChild(0).jjtAccept(this, data);
		try {
			if(num > 1){
				boolean sum = true;
				Object cur = first;
				boolean sign = true;
				for(int i = 0; i < num; i++){
					SimPLSymbol curS = (SimPLSymbol)cur;
					String varName = "";
					if(curS.type == ValueType.VAR)
					{
						varName = (String)curS.value;
						if(!env.GlobalExist(varName)){
							return new SimPLSymbol(ValueType.EXCEPTION,"no such symbol "+varName);
						}else {
							curS =env.GlobalGetSymbol(varName);
						}
						//sum += (int)cur.value;
					}
					if(curS.type == ValueType.FREE){
						curS = new SimPLSymbol(ValueType.BOOLEAN,"true");
						env.GlobalSetSymbol(varName, curS);
					}
					if(curS.type == ValueType.BOOLEAN){
						if(sign)
							sum = sum && (curS.value.toString()=="true");
						else
							sum = sum || (curS.value.toString()=="true");
					} else {
						return new SimPLSymbol(ValueType.EXCEPTION,"wrong type in andor, bool needed");
					}
					if( i == num -1 )
						break;
					SimpleNode last = (SimpleNode)(node.jjtGetChild(i));
					//System.out.println(last.jjtGetLastToken().next.image);
					if(last.jjtGetLastToken().next.image == "and")
						sign = true;
					else if(last.jjtGetLastToken().next.image == "or")
						sign = false;
					cur = node.jjtGetChild(i+1).jjtAccept(this, data);
				}
				SimPLSymbol result = new SimPLSymbol(ValueType.BOOLEAN);
				result.value = sum?"true":"false";
				return result; 
			}
		}
		catch (Exception e){
			return new SimPLSymbol(ValueType.EXCEPTION,"Err in AndOr");
		}
		return first;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTCompare, java.lang.Object)
	 */
	@Override
	public Object visit(ASTCompare node, Object data) {
		// TODO Auto-generated method stub
		int num = node.jjtGetNumChildren();
		//System.out.println("in Compare:"+num);
		
		Object first = node.jjtGetChild(0).jjtAccept(this, data);
		try {
			if(num > 1){
				SimPLSymbol left = (SimPLSymbol)first;
				if(left.type == ValueType.VAR)
				 {
						String var = (String)left.value;
						if(!env.GlobalExist(var)){
							return new SimPLSymbol(ValueType.EXCEPTION,"no such symbol "+var);
						}else {
							left =env.GlobalGetSymbol(var);
						}
				}
				if(left.type != ValueType.INTEGER)
					return new SimPLSymbol(ValueType.EXCEPTION,"left in compare need int");
				SimPLSymbol right = (SimPLSymbol)node.jjtGetChild(1).jjtAccept(this, data);
				if(right.type == ValueType.VAR)
				 {
						String var = (String)right.value;
						if(!env.GlobalExist(var)){
							return new SimPLSymbol(ValueType.EXCEPTION,"no such symbol "+var);
						}else {
							right =env.GlobalGetSymbol(var);
						}
				}
				if(right.type != ValueType.INTEGER){
					return new SimPLSymbol(ValueType.EXCEPTION,"right in compare need int");
				}else{
					int lv = Integer.parseInt(left.value.toString());
					int rv = Integer.parseInt(right.value.toString());
					String op = ((SimpleNode)(node.jjtGetChild(0))).jjtGetLastToken().next.image;
					SimPLSymbol result = new SimPLSymbol(ValueType.BOOLEAN);
					if(op=="=")
						result.value = lv == rv?"true":"false";
					else if(op==">")
						result.value = lv > rv?"true":"false";
					else if(op=="<")
						result.value = lv < rv?"true":"false";
					return result;
				}
			}
		}
		catch (Exception e){
			return new SimPLSymbol(ValueType.EXCEPTION,"Err in Compare");
		}
		return first;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTAddMinus, java.lang.Object)
	 */
	@Override
	public Object visit(ASTAddMinus node, Object data) {
		// TODO Auto-generated method stub
		int num = node.jjtGetNumChildren();
		//System.out.println("in ADDMINUS:"+num);
		
		Object first = node.jjtGetChild(0).jjtAccept(this, data);
		try {
			if(num > 1){
				int sum = 0;
				Object cur = first;
				int sign = 1;
				for(int i = 0; i < num; i++){
					String varName="";
					SimPLSymbol curS = (SimPLSymbol)cur;
					 if(curS.type == ValueType.VAR)
					 {
						 varName = (String)curS.value;
							String var = varName;
							if(!env.GlobalExist(var)){
								return new SimPLSymbol(ValueType.EXCEPTION,"no such symbol "+var);
							}else {
								curS =env.GlobalGetSymbol(var);
							}
							//sum += (int)cur.value;
					}
					if(curS.type==ValueType.FREE){
						curS = new SimPLSymbol(ValueType.INTEGER,"0");
						env.GlobalSetSymbol(varName, curS);
					}
					if(curS.type == ValueType.INTEGER)
						sum += sign * Integer.parseInt((String)curS.value);
					else {
						return new SimPLSymbol(ValueType.EXCEPTION,"wrong type in addminus, integer needed");
					}
					if( i == num -1 )
						break;
					SimpleNode last = (SimpleNode)(node.jjtGetChild(i));
					//System.out.println(last.jjtGetLastToken().next.image);
					if(last.jjtGetLastToken().next.image == "+")
						sign = 1;
					else
						sign = -1;
					cur = node.jjtGetChild(i+1).jjtAccept(this, data);
				}
				SimPLSymbol result = new SimPLSymbol(ValueType.INTEGER);
				result.value = Integer.toString(sum);
				return result; 
			}
		}
		catch (Exception e){
			return new SimPLSymbol(ValueType.EXCEPTION,"Err in AddMinus");
		}
		return first;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTMulDiv, java.lang.Object)
	 */
	@Override
	public Object visit(ASTMulDiv node, Object data) {
		// TODO Auto-generated method stub
		int num = node.jjtGetNumChildren();
		//System.out.println("in MulDiv:"+num);
		
		Object first = node.jjtGetChild(0).jjtAccept(this, data);
		try {
			if(num > 1){
				int sum = 1;
				Object cur = first;
				boolean sign = true;
				for(int i = 0; i < num; i++){
					
					SimPLSymbol curS = (SimPLSymbol)cur;
					String var = "";
					if(curS.type == ValueType.VAR)
					{
						var = (String)curS.value;
						if(!env.GlobalExist(var)){
							return new SimPLSymbol(ValueType.EXCEPTION,"no such symbol "+var);
						}else {
							curS =env.GlobalGetSymbol(var);
						}
						//sum += (int)cur.value;
					}
					if(curS.type==ValueType.FREE){
						curS = new SimPLSymbol(ValueType.INTEGER,"1");
						env.GlobalSetSymbol(var, curS);
					}
					if(curS.type == ValueType.INTEGER){
						if(sign)
							sum = sum * (Integer.parseInt(curS.value.toString()));
						else
							sum = sum / (Integer.parseInt(curS.value.toString()));
					} else {
						return new SimPLSymbol(ValueType.EXCEPTION,"wrong type in muldiv, integer needed");
					}
					if( i == num -1 )
						break;
					SimpleNode last = (SimpleNode)(node.jjtGetChild(i));
					//System.out.println(last.jjtGetLastToken().next.image);
					if(last.jjtGetLastToken().next.image == "*")
						sign = true;
					else if(last.jjtGetLastToken().next.image == "/")
						sign = false;
					cur = node.jjtGetChild(i+1).jjtAccept(this, data);
				}
				SimPLSymbol result = new SimPLSymbol(ValueType.INTEGER);
				result.value = Integer.toString(sum);
				return result; 
			}
		}
		catch (Exception e){
			return new SimPLSymbol(ValueType.EXCEPTION,"Err in MultiDiv");
		}
		return first;
	}

	

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTUnaryExp, java.lang.Object)
	 */
	@Override
	public Object visit(ASTUnaryExp node, Object data) {
		// TODO Auto-generated method stub
		SimPLSymbol value = (SimPLSymbol)node.jjtGetChild(0).jjtAccept(this, data);
		if(((SimpleNode)node).jjtGetFirstToken().image == "not"){
			String var="";
			if(value.type == ValueType.VAR)
			{
				var = (String)value.value;
				if(!env.GlobalExist(var)){
					return new SimPLSymbol(ValueType.EXCEPTION, "var "+value.value+" is not defined");
				}else
					value = env.GlobalGetSymbol(value.value.toString());
			}
			if(value.type==ValueType.FREE){
				value = new SimPLSymbol(ValueType.BOOLEAN,"true");
				env.GlobalSetSymbol(var, value);
			}
			if(value.type != ValueType.BOOLEAN)
				return new SimPLSymbol(ValueType.EXCEPTION, "not op should be followed by a boolean"); 
			value.value = value.value=="true"?"false":"true";
			return value;
		}else {
			String var="";
			if(value.type == ValueType.VAR)
			{
				var = (String)value.value;
				if(!env.GlobalExist((String)value.value)){
					return new SimPLSymbol(ValueType.EXCEPTION, "var "+value.value+" is not defined");
				}else
					value = env.GlobalGetSymbol(value.value.toString());
			}
			if(value.type==ValueType.FREE){
				value = new SimPLSymbol(ValueType.INTEGER,"0");
				env.GlobalSetSymbol(var, value);
			}
			if(value.type != ValueType.INTEGER)
				return new SimPLSymbol(ValueType.EXCEPTION, "~ op should be followed by a int"); 
			value.value = Integer.toString(~Integer.parseInt(value.value.toString()));
			return value;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTLet, java.lang.Object)
	 */
	@Override
	public Object visit(ASTLet node, Object data) {
		// TODO Auto-generated method stub
		int num = node.jjtGetNumChildren();
		//System.out.println("in Let:"+num);
		
		SimPLSymbol var = (SimPLSymbol)node.jjtGetChild(0).jjtAccept(this, data);
		SimPLSymbol value = (SimPLSymbol)node.jjtGetChild(1).jjtAccept(this, data);
		
//		if(env.LocalExist((String)var.value)){
//			//should never in
//			return new SimPLSymbol(ValueType.EXCEPTION, "var "+var.value+" already exists in let");
//		}
		env.EnterBlock();
		if(value.type == ValueType.VAR)
		{
			if(!env.GlobalExist((String)value.value)){
				return new SimPLSymbol(ValueType.EXCEPTION, "var "+value.value+" is not defined");
			}
			
			env.LocalSetSymbol((String)var.value, env.GlobalGetSymbol((String)value.value));
		}
		else {
			env.LocalSetSymbol((String)var.value, value);
		}
		SimPLSymbol body = (SimPLSymbol)node.jjtGetChild(2).jjtAccept(this, data);
		env.LeaveBlock();
		return body;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTCond, java.lang.Object)
	 */
	@Override
	public Object visit(ASTCond node, Object data) {
		// TODO Auto-generated method stub
		SimPLSymbol cond = (SimPLSymbol)node.jjtGetChild(0).jjtAccept(this, data);
		String var = "";
		
		SimPLEnv envbak = env.Duplicate();
		SimPLSymbol thenValue = (SimPLSymbol)node.jjtGetChild(1).jjtAccept(this, data);
		var ="";
		if(thenValue.type == ValueType.VAR){
			if(env.GlobalExist((String)thenValue.value)){
				var = thenValue.value.toString();
				thenValue = env.GlobalGetSymbol((String)thenValue.value);
			}else
				return new SimPLSymbol(ValueType.EXCEPTION,"var "+(String)thenValue.value+" not exist in assignment");
		}
		env = envbak;
		SimPLSymbol elseValue = (SimPLSymbol)node.jjtGetChild(2).jjtAccept(this, data);
		String var2= "";
		if(elseValue.type == ValueType.VAR){
			if(env.GlobalExist((String)elseValue.value)){
				var2 = elseValue.value.toString();
				elseValue = env.GlobalGetSymbol((String)elseValue.value);
			}else
				return new SimPLSymbol(ValueType.EXCEPTION,"var "+(String)elseValue.value+" not exist in assignment");
		}
		env = envbak;
		if(thenValue.type != elseValue.type){
			if(thenValue.type != ValueType.FREE && elseValue.type != ValueType.FREE){
				return new SimPLSymbol(ValueType.EXCEPTION,"else statement and then statement should return the same type");
			}else if(thenValue.type == ValueType.FREE){
				env.GlobalSetSymbol(var, elseValue);
			}else
				env.GlobalSetSymbol(var2, thenValue);
		}
		
		if(cond.type == ValueType.VAR)
		{
			var = cond.value.toString();
			if(!env.GlobalExist((String)cond.value)){
				return new SimPLSymbol(ValueType.EXCEPTION, "var "+cond.value+" is not defined");
			}
			cond = env.GlobalGetSymbol((String)cond.value);
		}
		if(cond.type == ValueType.FREE)
		{
			cond = new SimPLSymbol(ValueType.BOOLEAN,"true");
			env.GlobalSetSymbol(var, cond);
		}
		if(cond.type != ValueType.BOOLEAN)
		{
			return new SimPLSymbol(ValueType.EXCEPTION,"if condition should be boolean");
		}
		
		if(cond.value.toString() == "true"){
			thenValue = (SimPLSymbol)node.jjtGetChild(1).jjtAccept(this, data);
			return thenValue;
		}else{
			elseValue = (SimPLSymbol)node.jjtGetChild(2).jjtAccept(this, data);
			return elseValue;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTWhile, java.lang.Object)
	 */
	@Override
	public Object visit(ASTWhile node, Object data) {
		// TODO Auto-generated method stub
		
				
		while(true){
			SimPLSymbol cond = (SimPLSymbol)node.jjtGetChild(0).jjtAccept(this, data);
			
			String var = "";
			if(cond.type == ValueType.VAR)
			{
				var = cond.value.toString();
				if(!env.GlobalExist((String)cond.value)){
					return new SimPLSymbol(ValueType.EXCEPTION, "var "+cond.value+" is not defined");
				}
				cond = env.GlobalGetSymbol((String)cond.value);
			}
			if(cond.type == ValueType.FREE)
			{
				cond = new SimPLSymbol(ValueType.BOOLEAN,"true");
				env.GlobalSetSymbol(var, cond);
			}
			
			if(cond.type != ValueType.BOOLEAN)
			{
				return new SimPLSymbol(ValueType.EXCEPTION,"if condition should be boolean");
			}
			
			if(cond.value.toString() == "true"){
				node.jjtGetChild(1).jjtAccept(this, data);
			}else {
				break;
			}
		}
		return new SimPLSymbol(ValueType.UNIT);
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTBracket, java.lang.Object)
	 */
	@Override
	public Object visit(ASTBracket node, Object data) {
		// TODO Auto-generated method stub
		return node.jjtGetChild(0).jjtAccept(this, data);
	}
	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTPair, java.lang.Object)
	 */
	@Override
	public Object visit(ASTPair node, Object data) {
		// TODO Auto-generated method stub
		int num = node.jjtGetNumChildren();
		
		SimPLSymbol first = (SimPLSymbol)node.jjtGetChild(0).jjtAccept(this, data);
		if(first.type == ValueType.VAR)
		{
			if(!env.GlobalExist((String)first.value)){
				return new SimPLSymbol(ValueType.EXCEPTION, "var "+first.value+" is not defined");
			}
			first = env.GlobalGetSymbol((String)first.value);
		}
		SimPLSymbol second = (SimPLSymbol)node.jjtGetChild(1).jjtAccept(this, data);
		if(second.type == ValueType.VAR)
		{
			if(!env.GlobalExist((String)second.value)){
				return new SimPLSymbol(ValueType.EXCEPTION, "var "+second.value+" is not defined");
			}
			second = env.GlobalGetSymbol((String)second.value);
		}
		SimPLSymbol result = new SimPLSymbol(ValueType.PAIR);
		result.value = new MyPair(first,second);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTApplication, java.lang.Object)
	 */
	@Override
	public Object visit(ASTApplication node, Object data) {
		// TODO Auto-generated method stub
		int num = node.jjtGetNumChildren();
		
		//System.out.println("in Application:"+num);
		
		SimPLSymbol param = (SimPLSymbol)node.jjtGetChild(1).jjtAccept(this, data);
		
		if(param.type == ValueType.VAR){
			if(!env.GlobalExist((String)param.value)){
				return new SimPLSymbol(ValueType.EXCEPTION, "var "+param.value+" is not defined");
			}else
				param = env.GlobalGetSymbol(param.value.toString());
		}
		int depth = env.GetDepth();
		SimPLSymbol func = (SimPLSymbol)node.jjtGetChild(0).jjtAccept(this, data);
		if(depth != env.GetDepth())
		{
			System.out.println("nests should be "+depth);
		}
		
		if(func.type == ValueType.VAR){
			if(!env.GlobalExist((String)func.value)){
				return new SimPLSymbol(ValueType.EXCEPTION, "var "+func.value+" is not defined");
			}else
				func = env.GlobalGetSymbol(func.value.toString());
		}
		
		if (func.type == ValueType.FUN) {
			MyFunc f = (MyFunc) (func.value);
			if(f.level == 0) {
				SimPLSymbol var = f.param;
				env.EnterBlock();
				env.LocalSetSymbol(var.value.toString(), param);
				SimPLSymbol exp = (SimPLSymbol)f.node.jjtAccept(this, data);
				
				if(exp.type == ValueType.VAR){
					exp = env.GlobalGetSymbol(exp.value.toString());
				}
				env.LeaveBlock();
				int nests = env.PopStackToDepth(depth);
				if(nests>0)
					System.out.println("nests is "+nests);
				return exp;
			}else {
				
				SimPLSymbol var = f.param;
				//env.EnterBlock();
				env.LocalSetSymbol(var.value.toString(), param);
				return f.body;
			}
			
		}else {
			return new SimPLSymbol(ValueType.EXCEPTION,"application need fun type");
		}
		
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTFunction, java.lang.Object)
	 */
	@Override
	public Object visit(ASTFunction node, Object data) {
		// TODO Auto-generated method stub
		//node.childrenAccept(this, data);
		
		SimPLSymbol param = (SimPLSymbol)node.jjtGetChild(0).jjtAccept(this, data);
		env.EnterBlock();
		env.LocalSetSymbol(param.value.toString(), new SimPLSymbol(ValueType.FREE));
		SimPLEnv envbak = env.Duplicate();
		SimPLSymbol body = (SimPLSymbol)node.jjtGetChild(1).jjtAccept(this, data);
		env = envbak;
		int level = 0;
		if(body.type == ValueType.FUN)
			level = ((MyFunc)(body.value)).level+1;
		SimPLSymbol result = new SimPLSymbol(ValueType.FUN);
		result.value = new MyFunc(param,level,body,(SimpleNode)node.jjtGetChild(1));
		env.LeaveBlock();
		return result;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTVar, java.lang.Object)
	 */
	@Override
	public Object visit(ASTVar node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		SimPLSymbol result = new SimPLSymbol(ValueType.VAR);
		result.value = node.jjtGetFirstToken().image;
		return result;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTInt, java.lang.Object)
	 */
	@Override
	public Object visit(ASTInt node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		SimPLSymbol result = new SimPLSymbol(ValueType.INTEGER);
		result.value = node.jjtGetFirstToken().image;
		return result;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTBool, java.lang.Object)
	 */
	@Override
	public Object visit(ASTBool node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		SimPLSymbol result = new SimPLSymbol(ValueType.BOOLEAN);
		result.value = node.jjtGetFirstToken().image;
		//System.out.println("In Bool Node, return:"+result.value.toString());
		return result;
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		// TODO Auto-generated method stub
		//node.childrenAccept(this, data);
		int num = node.jjtGetNumChildren();
		Object result = null;
		for(int i = 0; i < num;i++){
			result = node.jjtGetChild(i).jjtAccept(this, data);
			if(i!=num-1)
				if(((SimPLSymbol)result).type!=ValueType.UNIT){
					return new SimPLSymbol(ValueType.EXCEPTION, "in-final sequence expression should be unit");
				}
		}
		return result;
	}

	@Override
	public Object visit(ASTNil node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		SimPLSymbol result = new SimPLSymbol(ValueType.LIST);
		result.value = null;
		//System.out.println("In Bool Node, return:"+result.value.toString());
		return result;
	}

	@Override
	public Object visit(ASTUnit node, Object data) {
		// TODO Auto-generated method stub
		
		return new SimPLSymbol(ValueType.UNIT);
		
	}

}
