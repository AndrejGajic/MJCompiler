package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class MyTab extends Tab {

	// Added new boolean type
	public static final Struct boolType = new Struct(Struct.Bool);
	
	public static void init() {
		Tab.init();
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
	}
	
	public static String getObjString(Obj obj) {
		DumpSymbolTableVisitor visitor = new DumpSymbolTableVisitor();
		obj.accept(visitor);
		String objString = "[" + visitor.getOutput() + "]";
		return objString;
	}
	
	
}
