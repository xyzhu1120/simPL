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
import com.simPL.compiler.ASTPair;
import com.simPL.compiler.ASTSTART;
import com.simPL.compiler.ASTUnaryExp;
import com.simPL.compiler.ASTUnaryOP;
import com.simPL.compiler.ASTVar;
import com.simPL.compiler.ASTWhile;
import com.simPL.compiler.SIMPLVisitor;
import com.simPL.compiler.SimpleNode;
import com.simPL.compiler.SIMPLConstants;

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
		return node.jjtGetChild(0).jjtAccept(this, data);
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
		
		Object first = node.jjtGetChild(0).jjtAccept(this, data);
		for(int i = 1; i < num;i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		if(num > 1){
			SimPLSymbol result = new SimPLSymbol(ValueType.UNIT);
			return result; 
		}
		return first;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTListInsert, java.lang.Object)
	 */
	@Override
	public Object visit(ASTList node, Object data) {
		// TODO Auto-generated method stub
		int num = node.jjtGetNumChildren();
		//System.out.println("in List:"+num);
		
		Object first = node.jjtGetChild(0).jjtAccept(this, data);
		for(int i = 1; i < num;i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		if(num > 1){
			SimPLSymbol result = new SimPLSymbol(ValueType.LIST);
			return result; 
		}
		return first;
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
		for(int i = 1; i < num;i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		if(num > 1){
			SimPLSymbol result = new SimPLSymbol(ValueType.BOOLEAN);
			return result; 
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
		for(int i = 1; i < num;i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		if(num > 1){
			SimPLSymbol result = new SimPLSymbol(ValueType.BOOLEAN);
			return result; 
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
					SimPLSymbol curS = (SimPLSymbol)cur;
					if(curS.type == ValueType.INTEGER)
						sum += sign * Integer.parseInt((String)curS.value);
					else if(curS.type == ValueType.VAR)
					{
						//sum += (int)cur.value;
					}else if(curS.type == ValueType.EXCEPTION){
						return curS;
					}
					if( i == num -1 )
						break;
					SimpleNode last = (SimpleNode)(node.jjtGetChild(i));
					System.out.println(last.jjtGetLastToken().next.image);
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
		for(int i = 1; i < num;i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		if(num > 1){
			SimPLSymbol result = new SimPLSymbol(ValueType.INTEGER);
			return result; 
		}
		return first;
	}

	

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTUnaryExp, java.lang.Object)
	 */
	@Override
	public Object visit(ASTUnaryExp node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTLet, java.lang.Object)
	 */
	@Override
	public Object visit(ASTLet node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTCond, java.lang.Object)
	 */
	@Override
	public Object visit(ASTCond node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTWhile, java.lang.Object)
	 */
	@Override
	public Object visit(ASTWhile node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
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
		
		Object first = node.jjtGetChild(0).jjtAccept(this, data);
		Object second = node.jjtGetChild(1).jjtAccept(this, data);
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
		SimPLSymbol func = (SimPLSymbol)node.jjtGetChild(0).jjtAccept(this, data);
		for(int i = 1; i < num;i++){
			node.jjtGetChild(i).jjtAccept(this, data);
		}
		if(func.type == ValueType.EXCEPTION)
			return func;
		ASTFunction f=null;
		if (func.type == ValueType.FUN) {
			f = (ASTFunction) func.value;
		}else {
			if(func.type == ValueType.VAR){
				//f = (ASTFunction)func.value;
				//look for the symbol table
			}else
				return new SimPLSymbol(ValueType.EXCEPTION,"Wrong in fun first argument");
		}
		SimPLSymbol result = new SimPLSymbol(ValueType.UNIT);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTFunction, java.lang.Object)
	 */
	@Override
	public Object visit(ASTFunction node, Object data) {
		// TODO Auto-generated method stub
		//node.childrenAccept(this, data);
		SimPLSymbol result = new SimPLSymbol(ValueType.FUN,node);
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
		}
		return result;
	}

}
