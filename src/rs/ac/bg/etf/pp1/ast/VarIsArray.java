// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class VarIsArray extends VarArray {

    private ArrayIncrement ArrayIncrement;

    public VarIsArray (ArrayIncrement ArrayIncrement) {
        this.ArrayIncrement=ArrayIncrement;
        if(ArrayIncrement!=null) ArrayIncrement.setParent(this);
    }

    public ArrayIncrement getArrayIncrement() {
        return ArrayIncrement;
    }

    public void setArrayIncrement(ArrayIncrement ArrayIncrement) {
        this.ArrayIncrement=ArrayIncrement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArrayIncrement!=null) ArrayIncrement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrayIncrement!=null) ArrayIncrement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrayIncrement!=null) ArrayIncrement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarIsArray(\n");

        if(ArrayIncrement!=null)
            buffer.append(ArrayIncrement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarIsArray]");
        return buffer.toString();
    }
}
