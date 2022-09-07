// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class Program implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private ProgName ProgName;
    private ProgVarList ProgVarList;
    private ProgMethList ProgMethList;

    public Program (ProgName ProgName, ProgVarList ProgVarList, ProgMethList ProgMethList) {
        this.ProgName=ProgName;
        if(ProgName!=null) ProgName.setParent(this);
        this.ProgVarList=ProgVarList;
        if(ProgVarList!=null) ProgVarList.setParent(this);
        this.ProgMethList=ProgMethList;
        if(ProgMethList!=null) ProgMethList.setParent(this);
    }

    public ProgName getProgName() {
        return ProgName;
    }

    public void setProgName(ProgName ProgName) {
        this.ProgName=ProgName;
    }

    public ProgVarList getProgVarList() {
        return ProgVarList;
    }

    public void setProgVarList(ProgVarList ProgVarList) {
        this.ProgVarList=ProgVarList;
    }

    public ProgMethList getProgMethList() {
        return ProgMethList;
    }

    public void setProgMethList(ProgMethList ProgMethList) {
        this.ProgMethList=ProgMethList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ProgName!=null) ProgName.accept(visitor);
        if(ProgVarList!=null) ProgVarList.accept(visitor);
        if(ProgMethList!=null) ProgMethList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ProgName!=null) ProgName.traverseTopDown(visitor);
        if(ProgVarList!=null) ProgVarList.traverseTopDown(visitor);
        if(ProgMethList!=null) ProgMethList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ProgName!=null) ProgName.traverseBottomUp(visitor);
        if(ProgVarList!=null) ProgVarList.traverseBottomUp(visitor);
        if(ProgMethList!=null) ProgMethList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Program(\n");

        if(ProgName!=null)
            buffer.append(ProgName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ProgVarList!=null)
            buffer.append(ProgVarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ProgMethList!=null)
            buffer.append(ProgMethList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Program]");
        return buffer.toString();
    }
}
