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
import com.simPL.compiler.ASTExps;
import com.simPL.compiler.ASTFunction;
import com.simPL.compiler.ASTInt;
import com.simPL.compiler.ASTLet;
import com.simPL.compiler.ASTListInsert;
import com.simPL.compiler.ASTMulDiv;
import com.simPL.compiler.ASTPair;
import com.simPL.compiler.ASTSTART;
import com.simPL.compiler.ASTUnaryExp;
import com.simPL.compiler.ASTUnaryOP;
import com.simPL.compiler.ASTVar;
import com.simPL.compiler.ASTWhile;
import com.simPL.compiler.SIMPLVisitor;
import com.simPL.compiler.SimpleNode;
import com.simPL.compiler.SIMPLConstants;;

/**
 * @author xyz
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
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTExps, java.lang.Object)
	 */
	@Override
	public Object visit(ASTExps node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTAssignment, java.lang.Object)
	 */
	@Override
	public Object visit(ASTAssignment node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTListInsert, java.lang.Object)
	 */
	@Override
	public Object visit(ASTListInsert node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTAndOr, java.lang.Object)
	 */
	@Override
	public Object visit(ASTAndOr node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTCompare, java.lang.Object)
	 */
	@Override
	public Object visit(ASTCompare node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTAddMinus, java.lang.Object)
	 */
	@Override
	public Object visit(ASTAddMinus node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTMulDiv, java.lang.Object)
	 */
	@Override
	public Object visit(ASTMulDiv node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTPair, java.lang.Object)
	 */
	@Override
	public Object visit(ASTPair node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTApplication, java.lang.Object)
	 */
	@Override
	public Object visit(ASTApplication node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
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
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTUnaryOP, java.lang.Object)
	 */
	@Override
	public Object visit(ASTUnaryOP node, Object data) {
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
		env.EnterBlock();
		String firstchild =(String)node.jjtGetChild(0).jjtAccept(this, data);
		int type = (int)node.jjtGetChild(2).jjtAccept(this, data);
		env.LocalSetSymbol(firstchild, new SimPLSymbol(type));
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
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTFunction, java.lang.Object)
	 */
	@Override
	public Object visit(ASTFunction node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return this.FUN;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTVar, java.lang.Object)
	 */
	@Override
	public Object visit(ASTVar node, Object data) {
		// TODO Auto-generated method stub
		return node.jjtGetFirstToken().image;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTInt, java.lang.Object)
	 */
	@Override
	public Object visit(ASTInt node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

	/* (non-Javadoc)
	 * @see com.simPL.compiler.SIMPLVisitor#visit(com.simPL.compiler.ASTBool, java.lang.Object)
	 */
	@Override
	public Object visit(ASTBool node, Object data) {
		// TODO Auto-generated method stub
		node.childrenAccept(this, data);
		return null;
	}

}
