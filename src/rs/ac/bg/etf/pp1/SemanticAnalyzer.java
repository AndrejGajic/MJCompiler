package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticAnalyzer extends VisitorAdaptor {
	
	/* Utils */
	
	Logger log = Logger.getLogger(getClass());
	private static boolean errorDetected = false;
	private int nVars = 0;
	
	public static void setErrorDetected(boolean error) {
		errorDetected = error;
	}
	
	public static boolean getErrorDetected() {
		return errorDetected;
	}
	
	public int getNVars() {
		return nVars;
	}
	
	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if(line != 0) {
			msg.append(" at line ").append(line);
		}
		log.info(msg.toString());
	}
	
	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if(line != 0) {
			msg.append(" at line ").append(line);
		}
		log.info(msg.toString());
	}
	
	private boolean checkIfEquivalent(Struct struct1, Struct struct2) { 
		boolean ret = false;
		if(struct1 == struct2) ret = true;
		else if(struct1.getKind() == Struct.Array && struct2.getKind() == Struct.Array && struct1.getElemType() == struct2.getElemType()) ret = true;
		return ret;
	}
	
	private boolean checkIfCompatible(Struct struct1, Struct struct2) {
		boolean ret = false;
		if(checkIfEquivalent(struct1, struct2)) ret = true;
		else if(struct1.getKind() == Struct.Array && struct2 == Tab.nullType) ret = true;
		else if(struct1 == Tab.nullType && struct2.getKind() == Struct.Array) ret = true;
		return ret;
	}
	
	private boolean checkIfCompatibileAssignment(Struct struct1, Struct struct2) {
		boolean ret = false;
		if(checkIfEquivalent(struct1, struct2)) ret = true;
		else if(struct1.getKind() == Struct.Array && struct2 == Tab.nullType) ret = true;
		return ret;
	}
	
	/* Global variables */
	
	private Type latestType = null;
	
	/* 1. PROGRAM visit methods */
	
	// Program BEGIN
	public void visit(ProgramName progName) {
		Obj obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		progName.obj = obj;
		Tab.openScope(); // opening new Scope for PROGRAM
	}
	
	// Program END
	public void visit(Program program) {
		Obj mainFunction = Tab.find("main");
		if(mainFunction == Tab.noObj || mainFunction.getKind() != Obj.Meth) {
			report_error("Error! Main is not declared in program!", null);
		}
		else {
			this.nVars = Tab.currentScope().getnVars();
			report_info("Semantic analysis completed.", null);
		}
		Tab.chainLocalSymbols(program.getProgName().obj); // chaining obj tables to Program
		Tab.closeScope(); // closing PROGRAM Scope
	}
	
	/* 2. CONSTANTS visit methods */
	
	/* CONSTANTS global variables */
	private Struct latestConstAssignmentType = Tab.noType;
	private int latestConstIntValue;
	private char latestConstCharValue;
	private boolean latestConstBoolValue;
	
	// Parsing constant values to see its type
	public void visit(NumberConstant numConstant) {
		this.latestConstAssignmentType = Tab.intType;
		this.latestConstIntValue = numConstant.getValue();
		SyntaxNode parent = numConstant.getParent();
		if(parent instanceof ConstType) {
			ConstType constType = (ConstType)numConstant.getParent();
			constType.obj = new Obj(Obj.Con, "$", Tab.intType);
			constType.obj.setAdr(this.latestConstIntValue);
			numConstant.obj = constType.obj;
		}
		else {
			Obj obj = new Obj(Obj.Con, "#", Tab.intType);
			obj.setAdr(this.latestConstIntValue);
			numConstant.obj = obj;
		}
	}
	
	public void visit(CharacterConstant charConstant) {
		this.latestConstAssignmentType = Tab.charType;
		this.latestConstCharValue = charConstant.getValue();
		SyntaxNode parent = charConstant.getParent();
		if(parent instanceof ConstType) {
			ConstType constType = (ConstType)charConstant.getParent();
			constType.obj = new Obj(Obj.Con, "$", Tab.intType);
			constType.obj.setAdr(this.latestConstCharValue);
		}
	}
	
	public void visit(BooleanConstant boolConstant) {
		this.latestConstAssignmentType = MyTab.boolType;
		this.latestConstBoolValue = boolConstant.getValue();
		SyntaxNode parent = boolConstant.getParent();
		if(parent instanceof ConstType) {
			ConstType constType = (ConstType)boolConstant.getParent();
			constType.obj = new Obj(Obj.Con, "$", Tab.intType);
			constType.obj.setAdr(this.latestConstBoolValue ? 1 : 0);
		}
	}
	
	// Parsing one constant declaration
	public void visit(ConstantDeclaration constDecl) {
		if(!this.latestType.struct.equals(this.latestConstAssignmentType)) {
			String typeName;
			if(this.latestConstAssignmentType == Tab.intType) {
				typeName = "int";
			}
			else if(this.latestConstAssignmentType == Tab.charType) {
				typeName = "char";
			}
			else if(this.latestConstAssignmentType == MyTab.boolType){
				typeName = "bool";
			}
			else {
				typeName = "unknown";
			}
			report_error("Error! Type " + typeName + " is not compatibile with type " + this.latestType.getTypeName() + 
					" while declaring constant", constDecl);
		}
		else {
			report_info("Constant " + constDecl.getConstName() + " declared ", constDecl);
			Obj constNode = Tab.insert(Obj.Con, constDecl.getConstName(), this.latestType.struct);
			// Setting constant value at field ADR in Object node
			if(this.latestConstAssignmentType == Tab.intType) {
				constNode.setAdr(this.latestConstIntValue);
			}
			else if(this.latestConstAssignmentType == Tab.charType) {
				constNode.setAdr(this.latestConstCharValue);
			}
			else {
				constNode.setAdr(this.latestConstBoolValue ? 1 : 0);
			}
			constNode.setLevel(0);
			constDecl.getConstType().obj = constNode;
		}
	}

	
	/* 3. GLOBAL VARIABLES visit methods */
	
	/* GLOBAL VARIABLES global variables */
	private boolean latestGlobalVariableIsArray = false;
	private boolean hasArrayIncrement = false;
	private Obj arrayIncrement = null;
	
	// Parsing 
	public void visit(VarIsArray array) {
		this.latestGlobalVariableIsArray = true;
	}
	
	public void visit(VarIsNotArray array) {
		this.latestGlobalVariableIsArray = false;
	}
	
	public void visit(HasArrayIncrement arrayIncrement) {
		this.hasArrayIncrement = true;
		this.arrayIncrement = arrayIncrement.getNumConst().obj;
	}
	
	// Parsing one global variable
	public void visit(GlobalVariableDeclaration globalVar) {
		String name = globalVar.getGlobalVarName();
		if(Tab.find(name) != Tab.noObj) {
			report_error("Error! Variable with this name already declared! Cannot declare global variable " + name, globalVar);
		}
		else {
			if(!this.latestGlobalVariableIsArray) {
				// inserting global variable in Symbol table
				Obj varNode = Tab.insert(Obj.Var, name, this.latestType.struct);
				varNode.setLevel(0);
				report_info("Global variable " + name + " declared", globalVar);
			}
			else {
				this.latestGlobalVariableIsArray = false;
				Obj varNode = Tab.insert(Obj.Var, name, new Struct(Struct.Array, this.latestType.struct));
				varNode.setLevel(0);
				if(this.hasArrayIncrement) {
					this.hasArrayIncrement = false;
					int value = this.arrayIncrement.getAdr();
					varNode.setFpPos(value);
					report_info("Global array " + name + "[] declared with increment " + value, globalVar);
				}
				else {
					report_info("Global array " + name + "[] declared", globalVar);
				}
			}
		}
	}
	
	/* 4. LOCAL VARIABLES visit methods */
	
	/* LOCAL VARIABLES global variables */
	private int localVariablesInCurrentMethodCnt = 0;
	
	public void visit(VariableDeclaration localVar) {
		String name = localVar.getVarName();
		if(Tab.currentScope().findSymbol(name) != null) {
			report_error("Error! Variable with this name already declared in this scope! Cannot declare local variable " + name, localVar);
		}
		else {
			this.localVariablesInCurrentMethodCnt++;
			if(!this.latestGlobalVariableIsArray) {
				// inserting local variable in Symbol table
				Obj varNode = Tab.insert(Obj.Var, name, this.latestType.struct);
				varNode.setLevel(1);
				report_info("Local variable " + name + " declared", localVar);
			}
			else {
				this.latestGlobalVariableIsArray = false;
				Obj varNode = Tab.insert(Obj.Var, name, new Struct(Struct.Array, this.latestType.struct));
				varNode.setLevel(1);
				report_info("Local array " + name + "[] declared", localVar);
			}
		}
	}
	
	/* 5. TYPE visit method */
	
	
	// Parsing TYPE
	public void visit(Type type) {
		String currTypeName = type.getTypeName();
		Obj typeNode = Tab.find(currTypeName);
		if(typeNode == Tab.noObj) {
			report_error("Error! Type " + currTypeName + " not found in symbol table!", null);
			type.struct = Tab.noType;
		}
		else {
			if(Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
				this.latestType = type;
			}
			else {
				report_error("Error! Name " + currTypeName + " is not a type!", type);
				type.struct = Tab.noType;
			}
		}
	}
	
	/* 6. METHODS visit methods */
	
	/* METHODS global variables */
	private Struct latestMethodType = null;
	private Obj latestMethod = Tab.noObj;
	private boolean returnFounded = false;
	private int formalParametersCnt = 0;
	private int defaultParametersCnt = 0;
	
	private class FormalParametersClass {
		public FormalParametersClass(Obj formalParameter, int position, boolean isDefault, int defaultValue) {
			this.formalParameter = formalParameter;
			this.position = position;
			this.isDefault = isDefault;
			this.defaultValue = defaultValue;
		}
		Obj formalParameter;
		int position;
		boolean isDefault;
		int defaultValue;
	}
	
	private List<FormalParametersClass> formalParameters = new ArrayList<>();
	private Map<String, List<FormalParametersClass>> functionMap = new HashMap<>();
	private Map<String, Integer> functionDefaultParamsMap = new HashMap<>();
	
	// Parsing method BEGIN
	public void visit(MethodName methodName) {
		this.returnFounded = false;
		this.formalParametersCnt = 0;
		this.defaultParametersCnt = 0;
		methodName.obj = Tab.insert(Obj.Meth, methodName.getMethodName(), this.latestMethodType);
		this.latestMethod = methodName.obj;
		Tab.openScope(); // opening Scope for method
		report_info("Function " + methodName.getMethodName() + " is being parsed ", methodName);
	}
	
	public void visit(MethodTypeNotVoid methodType) {
		this.latestMethodType = methodType.getType().struct;
	}
	
	public void visit(MethodTypeVoid methodType) {
		this.latestMethodType = Tab.noType;
	}
	
	// Parsing method END
	public void visit(MethodDeclaration methodDecl) {
		if(!this.returnFounded && this.latestMethodType != Tab.noType) {
			report_error("Error! Return value not found for function " + methodDecl.getMethodName().getMethodName(), methodDecl);
		}
		List<FormalParametersClass> list = new ArrayList<>();
		for(FormalParametersClass fp : this.formalParameters) {
			list.add(fp);
		}
		methodDecl.obj = this.latestMethod;
		methodDecl.obj.setFpPos(this.formalParameters.size());
		methodDecl.obj.setLevel(this.localVariablesInCurrentMethodCnt + this.formalParameters.size());
		this.functionDefaultParamsMap.put(methodDecl.obj.getName(), this.defaultParametersCnt);
		this.localVariablesInCurrentMethodCnt = 0;
		this.formalParametersCnt = 0;
		this.defaultParametersCnt = 0;
		this.formalParameters.clear();
		this.functionMap.put(methodDecl.getMethodName().getMethodName(), list);
		Tab.chainLocalSymbols(this.latestMethod); // chaining lower scope symbols to method
		Tab.closeScope(); // closing Scope for method
		this.latestMethod = null;
	}
	
	// Parsing formal parameter
	public void visit(FormalParameter formalParameter) {
		String name = formalParameter.getFormParName();
		if(Tab.currentScope().findSymbol(name) != null) {
			report_error("Error! Formal parameter name already declared in this scope", formalParameter);
		}
		else {
			if(!this.latestGlobalVariableIsArray) {
				Obj paramNode = Tab.insert(Obj.Var, name, this.latestType.struct);
				paramNode.setLevel(1);
				paramNode.setFpPos(1);
				this.formalParametersCnt++;
				FormalParametersClass formalParameterObj = new FormalParametersClass(paramNode,
						this.formalParameters.size(), false, -1);
				this.formalParameters.add(formalParameterObj);
				report_info("Local parameter " + name + " declared", formalParameter);
			}
			else {
				this.latestGlobalVariableIsArray = false;
				Obj paramNode = Tab.insert(Obj.Var, name, new Struct(Struct.Array, this.latestType.struct));
				paramNode.setLevel(1);
				paramNode.setFpPos(1);
				this.formalParametersCnt++;
				FormalParametersClass formalParameterObj = new FormalParametersClass(paramNode,
						this.formalParametersCnt++, false, -1);
				this.formalParameters.add(formalParameterObj);
				report_info("Local parameter " + name + "[] declared", formalParameter);
			}
		}
	}
	
	// Parsing default parameter
	public void visit(OptionalArgument defaultParameter) {
		String name = defaultParameter.getOptArgName();
		if(Tab.currentScope().findSymbol(name) != null) {
			report_error("Error! Formal parameter name already declared in this scope", defaultParameter);
		}
		else {
			Obj paramNode = Tab.insert(Obj.Var, name, this.latestType.struct);
			paramNode.setLevel(1);
			paramNode.setFpPos(2);
			this.defaultParametersCnt++;
			FormalParametersClass defaultParameterObj = new FormalParametersClass(paramNode,
					this.formalParameters.size(), true, 1);
			this.formalParameters.add(defaultParameterObj);
			report_info("Default parameter "+ name + " declared", defaultParameter);
		}
	}
	
	/* 7. DESIGNATOR visit methods */
	
	/* DESIGNATOR global variables */
	private Struct latestDesignatorArrayExpressionType = null;
	
	public void visit(DesignatorClass designator) {
		Obj obj = Tab.find(designator.getDesignatorName());
		if(obj == Tab.noObj) {
			report_error("Error! Name " + designator.getDesignatorName() + " not declared", designator);
			designator.obj = Tab.noObj;
		}
		else {
			if(obj.getKind() == Obj.Var && obj.getLevel() == 0) {
				report_info("Global variable " + designator.getDesignatorName() + " accessed", designator);
				report_info("\t" + MyTab.getObjString(obj), null);
			}
			else if(obj.getKind() == Obj.Var && obj.getLevel() > 0 && obj.getFpPos() == 0) {
				report_info("Local variable " + designator.getDesignatorName() + " accessed", designator);
				report_info("\t" + MyTab.getObjString(obj), null);
			}
			else if(obj.getKind() == Obj.Var && obj.getLevel() > 0 && obj.getFpPos() > 0) {
				report_info("Function parameter " + designator.getDesignatorName() + " accessed", designator);
				report_info("\t" + MyTab.getObjString(obj), null);
			}
			// Check parameters of function ???
			designator.obj = obj;
		}
	}
	
	public void visit(DesignatorArray array) {
		Obj obj = array.getDesignator().obj;
		if(obj.getType() == Tab.noType) {
			report_error("Error! Designator not declared!", array);
		}
		else if(obj.getType().getKind() != Struct.Array) {
			report_error("Error! Type of designator is not an array", array);
			array.obj = Tab.noObj;
		}
		else if(array.getExpr().struct != Tab.intType) {
			report_error("Error! Expression in [] must be an integer", array);
			array.obj = Tab.noObj;
		}
		else {
			report_info("Element in array " + array.getDesignator().obj.getName() + "[] accessed", array);
			Obj newObj = new Obj(Obj.Elem, obj.getName() + "_element", obj.getType().getElemType());
			report_info("\t" + MyTab.getObjString(newObj), null);
			array.obj = newObj;
		}
	}
	
	
	/* 8. FACTOR visit methods */
	
	/* FACTOR global variables */
	private boolean latestFactorFunctionCall = false;
	private FunctionCallArgs latestFactorFunctionArgs = null;
	
	
	// Parsing factor with operator NEW
	public void visit(FactorNewOperation factor) {
		Type type = factor.getType();
		Expr expr = factor.getExpr();
		if(Tab.find(type.getTypeName()) == Tab.noObj) {
			report_error("Error! Type " + type.getTypeName() + " not recognizable", factor);
		}
		else if(expr.struct != Tab.intType) {
			report_error("Error! Expression in [] must be an integer type", factor);
		}
		else {
			factor.struct = new Struct(Struct.Array, type.struct);
		}
	}
	
	
	// Parsing constant factors
	public void visit(FactorNumberConstant factor) {
		factor.struct = Tab.intType;
	}
	
	public void visit(FactorCharacterConstant factor) {
		factor.struct = Tab.charType;
	}
	
	public void visit(FactorBooleanConstant factor) {
		factor.struct = MyTab.boolType;
	}
	
	// Parsing factor with expression between parentheses
	public void visit(FactorExpression factor) {
		factor.struct = factor.getExpr().struct;
	}
	
	// Parsing function calls in expressions
	public void visit(FactorFunctionCall factor) {
		this.latestFactorFunctionCall = true;
		this.latestFactorFunctionArgs = factor.getFunctionCallArgs();
		Designator designator = factor.getDesignator();
		int kind = designator.obj.getKind();
		String name = designator.obj.getName();
		this.latestFactorFunctionCall = false;
		if(kind != Obj.Meth) {
			report_error("Error! Name " + name + " is not a global function", factor);
			factor.struct = Tab.noType;
		}
		else if(Tab.find(name) == Tab.noObj) {
			report_error("Error! Function " + name + " is not declared", factor);
			factor.struct = Tab.noType;
		}
		else if(designator.obj.getType() == Tab.noType) {
			report_error("Error! Function " + name + " cannot be used in statements because it is VOID type", factor);
			factor.struct = Tab.noType;
		}
		else if(designator.obj.getName().equals("ord")) {
			if(this.actParams.size() != 1 || this.actParams.get(0) != Tab.charType) {
				report_error("Error! Function " + name + " must be called with CHAR argument", factor);
				this.actParams.clear();
			}
			Obj obj = factor.getDesignator().obj;
			report_info("Function " + obj.getName() + " is called", factor);
			report_info("\t" + MyTab.getObjString(obj), null);
			factor.struct = obj.getType();
			this.actParams.clear();
		}
		else if(designator.obj.getName().equals("chr")) {
			if(this.actParams.size() != 1 || this.actParams.get(0) != Tab.intType) {
				report_error("Error! Function " + name + " must be called with INT argument", factor);
				this.actParams.clear();
			}
			Obj obj = factor.getDesignator().obj;
			report_info("Function " + obj.getName() + " is called", factor);
			report_info("\t" + MyTab.getObjString(obj), null);
			factor.struct = obj.getType();
			this.actParams.clear();
		}
		else if(designator.obj.getName().equals("len")) {
			this.actParams.clear();
		}
		else {
			Obj obj = factor.getDesignator().obj;
			String functionName = obj.getName();
			List<FormalParametersClass> fParams = this.functionMap.get(functionName);
			int defaultParamsCnt = this.functionDefaultParamsMap.get(functionName);
			int minumumArguments = fParams.size() - defaultParamsCnt;
			int actualParamsCnt = 0;
			if(this.hasCallArguments) {
				actualParamsCnt = this.actParams.size();
			}
			if(actualParamsCnt < minumumArguments) {
				report_error("Error! Calling function " + functionName + " with too few arguments", factor);
				this.actParams.clear();
				factor.struct = Tab.noType;
			}
			else if(actualParamsCnt > fParams.size()) {
				report_error("Error! Calling function " + functionName + " with too many arguments", factor);
				this.actParams.clear();
				factor.struct = Tab.noType;
			}
			else {
				// Checking types of parameters
				boolean errorFound = false;
				for(int i = 0; i < actualParamsCnt; i++) {
					if(fParams.get(i).formalParameter.getType() != this.actParams.get(i)) {
						errorFound = true;
						report_error("Error! While calling function " + functionName + ", "
								+ "argument at position " + i + " is not compatibile type", factor);
						this.actParams.clear();
						factor.struct = Tab.noType;
					}
				}
				if(!errorFound) {
					report_info("Function " + obj.getName() + " is called", factor);
					this.actParams.clear();
					factor.struct = obj.getType();
				}
			}
			this.actParams.clear();
		}
	}
	
	// Parsing factor with designator
	public void visit(FactorDesignator factor) {
		Obj designator = factor.getDesignator().obj;
		int kind = designator.getKind();
		String name = factor.getDesignator().obj.getName();
		if(kind != Obj.Var && kind != Obj.Con && kind != Obj.Elem) {
			report_error("Error! Name " + name + " is not variable, constant or array element", factor);
			factor.struct = Tab.noType;
		}
		else if((kind == Obj.Var || kind == Obj.Con) && Tab.find(name) == Tab.noObj) {
			report_error("Error! Name " + name + " is not declared", factor);
			factor.struct = Tab.noType;
		}
		else {
			factor.struct = designator.getType();
		}
	}
	
	/* 9. TERM visit method */
	
	public void visit(Terminal term) {
		term.struct = term.getFactor().struct;
	}
	
	// arrays ?
	public void visit(TerminalMulop term) {
		Struct termStruct = term.getTerm().struct;
		Struct factorStruct = term.getFactor().struct;
		if(termStruct != Tab.intType || factorStruct != Tab.intType) {
			report_error("Error! When performing multiplication, division or moduo, both types must be integer", term);
			term.struct = Tab.noType;
		}
		else {
			term.struct = Tab.intType;
		}
	}
	
	/* 10. EXPRESSION visit methods */
	
	/* Expression global variables */
	private boolean latestExpressionNegative = false;
	
	public void visit(ExpressionNegative expr) {
		this.latestExpressionNegative = true;
	}
	
	public void visit(Expr expr) {
		expr.struct = expr.getExpression().struct;
	}
	
	public void visit(ExpressionClass expr) {
		if(this.latestExpressionNegative) {
			this.latestExpressionNegative = false;
			if(expr.getTerm().struct != Tab.intType) {
				report_error("Error! Negative expressions must be integers", expr);
				expr.struct = Tab.noType;
			}
			else {
				expr.struct = Tab.intType;
			}
		}
		else {
			expr.struct = expr.getTerm().struct;
		}
	}
	
	public void visit(HasExprBinary exprBinary) {
		SyntaxNode parent = exprBinary.getParent();
		Expr expr = (Expr)parent;
		Expression left = expr.getExpression();
		Expression right = exprBinary.getExpression();
		if(left.struct != Tab.intType || right.struct != Tab.intType) {
			report_error("Error! While using ??, both types must be integer", exprBinary);
			left.struct = Tab.noType;
		}
	}
	

	public void visit(ExpressionAddop expr) {
		Struct exprStruct = expr.getExpression().struct;
		Struct termStruct = expr.getTerm().struct;
		if(exprStruct != Tab.intType || termStruct != Tab.intType) {
			report_error("Error! When performing addition or subtraction, both types must be integers", expr);
			expr.struct = Tab.noType;
		}
		else {
			expr.struct = Tab.intType;
		}
	}
	
	
	/* 11. CONDFACT visit methods */
	
	/* CONDFACT global variables */
	private boolean latestRelopEquals = false;
	private boolean latestRelopNotEquals = false;
	
	public void visit(RelationEquals relop) {
		this.latestRelopEquals = true;
	}
	
	public void visit(RelationNotEquals relop) {
		this.latestRelopNotEquals = true;
	}
	
	public void visit(ConditionFact condFact) {
		condFact.struct = condFact.getExpr().struct;
	}
	
	public void visit(ConditionFactRelop condFact) {
		Struct condFactStruct = condFact.getExpr().struct;
		Struct exprStruct = condFact.getExpr1().struct;
		if(!checkIfCompatible(condFactStruct, exprStruct)) {
			report_error("Error! Types are not compatibile", condFact);
			condFact.struct = Tab.noType;
		}
		else {
			if(condFactStruct.getKind() == Struct.Array || exprStruct.getKind() == Struct.Array) {
				if(this.latestRelopEquals || this.latestRelopNotEquals) {
					this.latestRelopEquals = false;
					this.latestRelopNotEquals = false;
					condFact.struct = MyTab.boolType;
				}
				else {
					report_error("Error! With array types you can only use == or !=", condFact);
					condFact.struct = Tab.noType;
				}
			}
			else {
				condFact.struct = MyTab.boolType;
			}
		}
	}
	
	/* 12. CONDTERM visit methods */
	
	public void visit(SingleConditionTerm condTerm) {
		condTerm.struct = condTerm.getCondFact().struct;
	}
	
	public void visit(MultipleConditionTerms condTerm) {
		Struct condTermStruct = condTerm.getCondTerm().struct;
		Struct condFactStruct = condTerm.getCondFact().struct;
		if(!checkIfCompatible(condTermStruct, condFactStruct)) {
			report_error("Error! Types must be compatibile", condTerm);
			condTerm.struct = Tab.noType;
		}
		else {
			condTerm.struct = MyTab.boolType;
		}
	}
	
	/* 13. CONDITION visit methods */
	
	public void visit(SingleCondition condition) {
		condition.struct = condition.getCondTerm().struct;
	}
	
	public void visit(MultipleConditions conditions) {
		Struct conditionStruct = conditions.getCondition().struct;
		Struct condTermStruct = conditions.getCondTerm().struct;
		if(!checkIfCompatible(conditionStruct, condTermStruct)) {
			report_error("Error! Types must be compatibile", conditions);
			conditions.struct = Tab.noType;
		}
		else {
			conditions.struct = conditionStruct;
		}
	}
	
	public void visit(IfConditionClass ifCondition) {
		ifCondition.struct = ifCondition.getCondition().struct;
	}
	
	public void visit(IfConditionError ifError) {
		ifError.struct = Tab.noType;
	}
	
	// ODAVDE NA DOLE NEMA VISE PROPAGACIJE NE TREBA SAMO RADIS PROVERE
	
	
	/* 14. DESIGNATOR STATEMENT visit methods */
	
	/* DESIGNATOR STATEMENT global methods */
	
	private boolean latestDesignatorAssignop = false;
	private Struct latestDesignatorAssignopExprStruct = Tab.noType;
	private boolean latestDesignatorInc = false;
	private boolean latestDesignatorDec = false;
	private boolean latestDesignatorFunctionCall = false;
	
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
	
	public void visit(DesignatorStatementClass designatorStatement) {
		Obj obj = designatorStatement.getDesignator().obj;
		if(this.latestDesignatorAssignop) {
			this.latestDesignatorAssignop = false;
			if(obj.getKind() != Obj.Var && obj.getKind() != Obj.Elem) {
				report_error("Error! Assignment operation can be done on variables and array elements only", designatorStatement);
			}
			else if(!checkIfCompatibileAssignment(obj.getType(), this.latestDesignatorAssignopExprStruct)) {
				report_error("Error! Assignment operation can be done between compatibile types only", designatorStatement);
			}
		}
		else if(this.latestDesignatorFunctionCall) {
			if(obj.getKind() != Obj.Meth) {
				report_error("Error! " + obj.getName() + " is not a function", designatorStatement);
			}
			else if(obj.getName().equals("ord")) {
				if(this.actParams.size() != 1 || this.actParams.get(0) != Tab.charType) {
					report_error("Error! Function " + obj.getName() + " must be called with CHAR argument", designatorStatement);
					this.actParams.clear();
				}
				report_info("Function " + obj.getName() + " is called", designatorStatement);
				report_info("\t" + MyTab.getObjString(obj), null);
				this.actParams.clear();
			}
			else if(obj.getName().equals("chr")) {
				if(this.actParams.size() != 1 || this.actParams.get(0) != Tab.intType) {
					report_error("Error! Function " + obj.getName() + " must be called with INT argument", designatorStatement);
					this.actParams.clear();
				}
				report_info("Function " + obj.getName() + " is called", designatorStatement);
				report_info("\t" + MyTab.getObjString(obj), null);
				this.actParams.clear();
			}
			else if(obj.getName().equals("len")) {
				this.actParams.clear();
			}
			else {
				String functionName = obj.getName();
				List<FormalParametersClass> fParams = this.functionMap.get(functionName);
				int defaultParamsCnt = this.functionDefaultParamsMap.get(functionName);
				int minumumArguments = fParams.size() - defaultParamsCnt;
				int actualParamsCnt = 0;
				if(this.hasCallArguments) {
					actualParamsCnt = this.actParams.size();
				}
				if(actualParamsCnt < minumumArguments) {
					report_error("Error! Calling function " + functionName + " with too few arguments", designatorStatement);
				}
				else if(actualParamsCnt > fParams.size()) {
					report_error("Error! Calling function " + functionName + " with too many arguments", designatorStatement);
				}
				else {
					// Checking types of parameters
					boolean errorFound = false;
					for(int i = 0; i < actualParamsCnt; i++) {
						if(fParams.get(i).formalParameter.getType() != this.actParams.get(i)) {
							errorFound = true;
							report_error("Error! While calling function " + functionName + ", "
									+ "argument at position " + i + " is not compatibile type", designatorStatement);
						}
					}
					if(!errorFound) {
						report_info("Function " + obj.getName() + " is called", designatorStatement);
					}
				}
				this.actParams.clear();
			}
			this.actParams.clear();
		}
		else if(this.latestDesignatorInc) {
			if(obj.getKind() != Obj.Var && obj.getKind() != Obj.Elem) {
				report_error("Error! Increment operation can be done on variables and array elements only", designatorStatement);
			}
			else if(obj.getType() != Tab.intType) {
				report_error("Error! Increment operator can be done with integer variables only", designatorStatement);
			}
		}
		else if(this.latestDesignatorDec) {
			if(obj.getKind() != Obj.Var && obj.getKind() != Obj.Elem) {
				report_error("Error! Decrement operation can be done on variables and array elements only", designatorStatement);
			}
			else if(obj.getType() != Tab.intType) {
				report_error("Error! Decrement operator can be done with integer variables only", designatorStatement);
			}
		}
		else {
			report_error("Unknown error designator statement", designatorStatement);
		}
	}
	
	/* 15. ACTUAL PARAMETERS visit methods */
	
	/* ACTUAL PARAMETERS global variables */
	private List<Struct> actParams = new ArrayList<>();
	private boolean hasCallArguments = false;
	
	public void visit(FunctionCallHasArguments args) {
		this.hasCallArguments = true;
		// this.actParams.clear();
	}
	
	public void visit(FunctionCallNoArguments args) {
		this.hasCallArguments = false;
	}
	
	public void visit(SingleActualParameter param) {
		param.struct = param.getExpr().struct;
		this.actParams.add(param.struct);
	}
	
	public void visit(MultipleActualParameters params) {
		params.struct = params.getExpr().struct;
		this.actParams.add(params.struct);
	}
	
	
	/* 16. STATEMENT visit methods */
	
	/* STATEMENT global variables */
	private int loopCnt = 0;
	private Struct returnExprType = Tab.noType;
	
	public void visit(DoWhileStartClass doWhileStart) {
		loopCnt++;
	}
	
	public void visit(BreakStatement breakStatement) {
		if(loopCnt <= 0) {
			report_error("Error! Break statement is allowed only in DO WHILE loop", breakStatement);
		}
	}
	
	public void visit(ContinueStatement continueStatement) {
		if(loopCnt <= 0) {
			report_error("Error! Continue statement is allowed only in DO WHILE loop", continueStatement);
		}
	}
	
	public void visit(DoWhileStatement doWhileStatement) {
		loopCnt--;
		if(doWhileStatement.getDoWhileCondition().getCondition().struct != MyTab.boolType) {
			report_error("Error! Condition in DO WHILE statement must be BOOLEAN type", doWhileStatement);
		}
	}
	
	public void visit(ReadStatement readStatement) {
		Designator designator = readStatement.getDesignator();
		Struct type = designator.obj.getType();
		int kind = designator.obj.getKind();
		if(type != Tab.intType && type != Tab.charType && type != MyTab.boolType) {
			report_error("Error! In read statement, you can only use INT, CHAR and BOOLEAN variables", readStatement);
		}
		else if(kind != Obj.Var && kind != Obj.Elem) {
			report_error("Error! In read statement, designator must be variable or array element", readStatement);
		}
	}
	
	public void visit(PrintStatement printStatement) {
		Expression expr = printStatement.getExpr().getExpression();
		if(expr.struct != Tab.intType && expr.struct != Tab.charType && expr.struct != MyTab.boolType
				&& expr.struct.getKind() != Struct.Array) {
			report_error("Error! In print statement, you can only use INT, CHAR and BOOLEAN variables", printStatement);
		}
	}
	
	public void visit(HasReturnExpression returnExpr) {
		this.returnExprType = returnExpr.getExpr().struct;
	}
	
	public void visit(NoReturnExpression returnExpr) {
		this.returnExprType = Tab.noType;
	}
	
	public void visit(ReturnStatement returnStatement) {
		this.returnFounded = true;
		if(this.latestMethod == Tab.noObj) {
			report_error("Error! Return statement is outside function!", returnStatement);
		}
		else if(!checkIfEquivalent(this.latestMethodType, this.returnExprType)) {
			report_error("Error! Type of return expression is not equivalent to method return type", returnStatement);
		}	
	}
	
	public void visit(IfStatement ifStatement) {
		if(ifStatement.getIfClause().getIfCondition().struct != MyTab.boolType) {
			report_error("Error! Condition in IF statement must be BOOLEAN type", ifStatement);
		}
	}
}
