package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.ac.bg.etf.pp1.CounterVisitor.FormalParameterCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.LocalVariableCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor {

	/* Utils */
	
	private int mainPC = 0;
	
	public void setMainPC(int mainPC) {
		this.mainPC = mainPC;
	}
	
	public int getMainPC() {
		return mainPC;
	}
	
	
	/* VISIT METHODS */
	
	/* CONSTANTS visit methods */
	
	private boolean functionDeclaration = false;
	
	public void visit(NumberConstant numConst) {
		SyntaxNode parent = numConst.getParent();
		SyntaxNode grandParent = numConst.getParent().getParent();
		Obj obj = null;
		if(grandParent instanceof ConstantDeclaration) {
			ConstantDeclaration constDecl = (ConstantDeclaration)grandParent;
			obj = Tab.find(constDecl.getConstName());
		}
		else {
			obj = Tab.insert(Obj.Con, "$", Tab.intType);
			obj.setLevel(0);
			obj.setAdr(numConst.getValue());
			if(!(grandParent instanceof OptionalArgument)) Code.load(obj);
		}
		numConst.obj = obj;
	}
	
	public void visit(CharacterConstant charConst) {
		SyntaxNode grandParent = charConst.getParent().getParent();
		Obj obj = null;
		if(grandParent instanceof ConstantDeclaration) {
			ConstantDeclaration constDecl = (ConstantDeclaration)grandParent;
			obj = Tab.find(constDecl.getConstName());
		}
		else {
			obj = Tab.insert(Obj.Con, "$", Tab.charType);
			obj.setLevel(0);
			obj.setAdr(charConst.getValue());
			if(!(grandParent instanceof OptionalArgument)) Code.load(obj);
		}
	}
	
	public void visit(BooleanConstant boolConst) {
		SyntaxNode grandParent = boolConst.getParent().getParent();
		Obj obj = null;
		if(grandParent instanceof ConstantDeclaration) {
			ConstantDeclaration constDecl = (ConstantDeclaration)grandParent;
			obj = Tab.find(constDecl.getConstName());
		}
		else {
			obj = Tab.insert(Obj.Con, "$", MyTab.boolType);
			obj.setLevel(0);
			obj.setAdr(boolConst.getValue() ? 1 : 0);
			if(!(grandParent instanceof OptionalArgument)) Code.load(obj);
		}
	}
	
	/* PRINT STATEMENT visit method */
	
	private boolean hasPrintConst = false;
	private Obj printConst = null;
	
	public void visit(HasPrintConstant hasPrintConst) {
		this.hasPrintConst = true;
		this.printConst = hasPrintConst.getNumConst().obj;
	}
	
	
	public void visit(PrintStatement printStatement) {
		if(this.hasPrintConst) {
			this.hasPrintConst = false;
			if(printStatement.getExpr().struct.getKind() == Struct.Array) {
				Code.put(Code.aload);
				Code.load(this.printConst);
			}
			Code.put(Code.print);
		}
		else {
			if(printStatement.getExpr().struct == Tab.intType) {
				Code.loadConst(5);
				Code.put(Code.print);
			}
			else if(printStatement.getExpr().struct == Tab.charType) {
				Code.loadConst(1);
				Code.put(Code.bprint);
			}
			else if(printStatement.getExpr().struct == MyTab.boolType) {
				Code.loadConst(5);
				Code.put(Code.print);
			}
			else { // array
				Obj cnt = new Obj(Obj.Var, "cntt", Tab.intType);
				Code.loadConst(0);
				Code.store(cnt);
				int beginPc = Code.pc;
				Code.put(Code.dup);
				Code.put(Code.arraylength);
				Code.load(cnt);
				Code.putFalseJump(Code.gt, 0);
				int jumpPc = Code.pc - 2;
				Code.put(Code.dup);
				Code.load(cnt);
				Code.put(Code.aload);
				Code.loadConst(5);
				Code.put(Code.print);
				Code.load(cnt);
				Code.loadConst(1);
				Code.put(Code.add);
				Code.store(cnt);
				Code.putJump(beginPc);
				int currPc = Code.pc;
				Code.fixup(jumpPc);
				Code.pc = currPc;
			}
		}
		
	}
	
	/* READ STATEMENT visit method */
	
	public void visit(ReadStatement readStatement) {
		if(readStatement.getDesignator().obj.getType() == Tab.intType) {
			Code.put(Code.read);
		}
		else if(readStatement.getDesignator().obj.getType() == Tab.charType) {
			Code.put(Code.bread);
		}
		Code.store(readStatement.getDesignator().obj);
	}
	
	/* METHODS visit methods */
	
	private Map<String, List<Obj>> functionDefaultParameters = new HashMap<>();
	
	// Method declaration START
	public void visit(MethodName methodName) {
		if(!methodName.equals("main")) this.functionDeclaration = true;
		if(methodName.getMethodName().equals("main")) {
			this.mainPC = Code.pc;
		}
		methodName.obj.setAdr(Code.pc);

		SyntaxNode methodNode = methodName.getParent();
		FormalParameterCounter fpCounter = new FormalParameterCounter();
		LocalVariableCounter varCounter = new LocalVariableCounter();
		
		methodNode.traverseTopDown(fpCounter);
		methodNode.traverseTopDown(varCounter);
		
		List<Obj> defaultParameters = fpCounter.getDefaultParameters();
		functionDefaultParameters.put(methodName.obj.getName(), defaultParameters);
		
		Code.put(Code.enter);
		Code.put(fpCounter.getCount());
		Code.put(fpCounter.getCount() + varCounter.getCount());
		
	}
	
	// Method declaration END
	public void visit(MethodDeclaration methodDecl) {
		this.functionDeclaration = false;
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	/* DESIGNATOR visit methods */
	
	/* DESIGNATOR global variables */
	private boolean latestDesignatorAssignop = false;
	private Struct latestDesignatorAssignopExprStruct = Tab.noType;
	private boolean latestDesignatorFunctionCall = false;
	private boolean latestDesignatorInc = false;
	private boolean latestDesignatorDec = false;
	private boolean latestExpressionNegative = false;
	private boolean latestDesignatorIsArray = false;
	
	private int argumentsCnt = 0;
	
	public void visit(DesignatorAssignOperation designatorAssignop) {
		this.latestDesignatorAssignop = true;
		this.latestDesignatorAssignopExprStruct = designatorAssignop.getExpr().struct;
	}
	
	public void visit(DesignatorFunctionCall designatorFunctionCall) {
		this.latestDesignatorFunctionCall = true;
	}
	
	public void visit(DesignatorIncrement designatorInc) {
		this.latestDesignatorInc = true;
	}
	
	public void visit(DesignatorDecrement designatorDec) {
		this.latestDesignatorDec = true;
	}
	
	private String progName = null;
	
	public void visit(ProgramName progName) {
		this.progName = progName.getProgName();
	}
	
	public void visit(DesignatorStatementClass designatorStatement) {
		Obj obj = designatorStatement.getDesignator().obj;
		if(this.latestDesignatorAssignop) {
			this.latestDesignatorAssignop = false;
			if(this.hasBinary) {
				this.hasBinary = false;
				
			}
			Code.store(obj);
		}
		else if(this.latestDesignatorFunctionCall) {
			this.latestDesignatorFunctionCall = false;
			if(this.argumentsCnt < obj.getFpPos()) {
				// has default parameters
				List<Obj> defaultParameters = this.functionDefaultParameters.get(obj.getName());
				int start = obj.getFpPos() - this.argumentsCnt;
				for(int i = defaultParameters.size() - start; i < defaultParameters.size(); i++) {
					Code.load(defaultParameters.get(i));
				}
			}
			this.argumentsCnt = 0;
			int offset = obj.getAdr() - Code.pc;
			Code.put(Code.call);
			Code.put2(offset);
			
		}
//		else if(this.latestDesignatorIsArray) {
//			this.latestDesignatorIsArray = false;
//			if(this.latestDesignatorInc) {
//				this.latestDesignatorInc = false;
//				Obj arrObj = Tab.find(obj.getName());
//				int adr = arrObj.getFpPos();
//				
//			}
//		}
		else if(this.latestDesignatorInc) {
			this.latestDesignatorInc = false;
			if(this.latestDesignatorIsArray) {
				Code.put(Code.dup2);
				Code.load(designatorStatement.getDesignator().obj);
				Obj temp = designatorStatement.getDesignator().obj;
				String name = temp.getName().split("_")[0];
				Obj progObj = Tab.find(this.progName);
				for(Obj symbol : progObj.getLocalSymbols()) {
					if(symbol.getName().equals(name)) {
						if(symbol.getFpPos() == 0) {
							Code.loadConst(1);
						}
						else {
							int increment = symbol.getFpPos();
							Code.loadConst(increment);
						}
						break;
					}
				}
				Code.put(Code.add);
				Code.store(obj);
				
			}
			else {
				Code.load(designatorStatement.getDesignator().obj);
				Code.loadConst(1);
				Code.put(Code.add);
				Code.store(obj);
			}
		}
		else if(this.latestDesignatorDec) {
			this.latestDesignatorDec = false;
			if(this.latestDesignatorIsArray) {
				Code.put(Code.dup2);
			}
			Code.load(designatorStatement.getDesignator().obj);
			Code.loadConst(-1);
			Code.put(Code.add);
			Code.store(obj);
		}
		else {
			// unknown error
		}
		if(this.latestDesignatorIsArray) {
			this.latestDesignatorIsArray = false;
		}
	}
	
	public void visit(DesignatorClass designator) {
        if (designator.getParent() instanceof Designator || designator.getParent() instanceof FactorDesignator) {
            Code.load(designator.obj);
        }
    }
    
    public void visit(DesignatorArray designatorArray) {
    	this.latestDesignatorIsArray = true;
        if (designatorArray.getParent() instanceof Designator || designatorArray.getParent() instanceof FactorDesignator) {
            Code.load(designatorArray.obj);
        }
    }
    
    /* ?? visit method */
    
    private boolean hasBinary = false;
	
	/* OPERATIONS visit methods */
	
	public void visit(TerminalMulop terminalMulop) {
		Mulop mulop = terminalMulop.getMulop();
		int operation = -1;
		if(mulop instanceof MultiplicationOperation) {
			operation = Code.mul;
		}
		else if(mulop instanceof DivisionOperation) {
			operation = Code.div;
		}
		else {
			operation = Code.rem;
		}
		Code.put(operation);
	}
	
	public void visit(ExpressionAddop expressionAddop) {
		Addop addop = expressionAddop.getAddop();
		int operation = -1;
		if(addop instanceof AdditionOperation) {
			operation = Code.add;
		}
		else {
			operation = Code.sub;
		}
		Code.put(operation);
	}
	
	public void visit(ExpressionNegative expressionNegative) {
		this.latestExpressionNegative = true;
	}
	
	public void visit(FactorNewOperation factor) {
		Code.put(Code.newarray);
		int b;
		if(factor.getType().struct == Tab.charType) {
			b = 0;
		}
		else {
			b = 1;
		}
		Code.put(b);
	}
	
	public void visit(FactorFunctionCall functionCall) {
		Obj obj = functionCall.getDesignator().obj;
		if(this.argumentsCnt < obj.getFpPos()) {
			// has default parameters
			List<Obj> defaultParameters = this.functionDefaultParameters.get(obj.getName());
			int start = obj.getFpPos() - this.argumentsCnt;
			for(int i = defaultParameters.size() - start; i < defaultParameters.size(); i++) {
				Code.load(defaultParameters.get(i));
			}
		}
		this.argumentsCnt = 0;
		int offset = obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
	}
	
	public void visit(SingleActualParameter actualParameter) {
		this.argumentsCnt++;
	}
	
	public void visit(MultipleActualParameters params) {
		this.argumentsCnt++;
	}
	
	
	/* RELOP helper method */
	private int getRelopCode(Relop relop) {
		int operation = -1;
		if(relop instanceof RelationEquals) {
			operation = Code.eq;
		}
		else if(relop instanceof RelationNotEquals) {
			operation = Code.ne;
		}
		else if(relop instanceof RelationGreater) {
			operation = Code.gt;
		}
		else if(relop instanceof RelationGreaterOrEquals) {
			operation = Code.ge;
		}
		else if(relop instanceof RelationLess) {
			operation = Code.lt;
		}
		else if(relop instanceof RelationLessOrEquals) {
			operation = Code.le;
		}
		return operation;
	}
	
    public void visit(HasExprBinary exprBinary) {
    	SyntaxNode parent = exprBinary.getParent();
		Expr expr = (Expr)parent;
		Expression left = expr.getExpression();
		Expression right = exprBinary.getExpression();
		left.traverseBottomUp(new CodeGenerator());
		Code.loadConst(0);
		Code.putFalseJump(Code.eq, 0);
		int oldPc1 = Code.pc - 2;
		Code.put(Code.pop);
		Code.put(Code.pop);
		right.traverseBottomUp(new CodeGenerator());
		Code.putJump(0);
		int oldPc2 = Code.pc - 2;
		int pc = Code.pc;
		Code.fixup(oldPc1);
		Code.pc = pc;
		Code.put(Code.pop);
		pc = Code.pc;
		Code.fixup(oldPc2);
		Code.pc = pc;	
    }
	
	
	// POWER method
	public void visit(DesignatorPower designatorPower) {	
		// Getting the parent
		SyntaxNode parent = designatorPower.getParent();
		FactorDesignator factorDesignator = (FactorDesignator)(parent);
		Designator designator = factorDesignator.getDesignator();
		Obj cnt = new Obj(Obj.Var, "powerCnt", Tab.intType); // first lower power
		int beginPc = Code.pc;
		Code.put(Code.dup);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(cnt);
		Code.loadConst(1);
		Code.putFalseJump(Code.ne, 0);
		int jumpPc = Code.pc - 2;
		Code.load(designator.obj);
		Code.put(Code.mul);
		Code.load(cnt);
		Code.putJump(beginPc);
		int currPc = Code.pc;
		Code.fixup(jumpPc);
		Code.pc = currPc;
	}
	
	// FACTORIEL method
	public void visit(DesignatorFactoriel designatorFactoriel) {
		// Getting the parent
		SyntaxNode parent = designatorFactoriel.getParent();
		FactorDesignator factorDesignator = (FactorDesignator)(parent);
		Designator designator = factorDesignator.getDesignator();
		Obj firstNext = new Obj(Obj.Var, "firstNext", Tab.intType);
		Code.put(Code.dup);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(firstNext);
		Code.put(Code.dup);
		int beginPc = Code.pc;
		Code.loadConst(2);
		Code.putFalseJump(Code.gt, 0);
		int jumpPc = Code.pc - 2;
		Code.load(firstNext);
		Code.put(Code.mul);
		Code.load(firstNext);
		Code.put(Code.dup);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(firstNext);
		Code.putJump(beginPc);
		int currPc = Code.pc;
		Code.fixup(jumpPc);
		Code.pc = currPc;
	}
	
	
}
