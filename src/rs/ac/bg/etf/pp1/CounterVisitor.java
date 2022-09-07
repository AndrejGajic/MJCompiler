package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.concepts.Obj;

public class CounterVisitor extends VisitorAdaptor {

	protected int count = 0;
	protected List<Obj> defaultParameters = new ArrayList<>();
	
	public int getCount() {
		return count;
	}
	
	public List<Obj> getDefaultParameters() {
		return defaultParameters;
	}
	
	public static class FormalParameterCounter extends CounterVisitor {
		
		public void visit(FormalParameter formalParameter) {
			count++;
		}
		
		public void visit(OptionalArgument defaultParameter) {
			count++;
			defaultParameters.add(defaultParameter.getConstType().obj);
		}
	}
	
	public static class LocalVariableCounter extends CounterVisitor {
		
		public void visit(VariableDeclaration localVariable) {
			count++;
		}
		
	}
	
}
