// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class IfStatement extends SingleStatement {

    private IfClause IfClause;
    private ElseClause ElseClause;

    public IfStatement (IfClause IfClause, ElseClause ElseClause) {
        this.IfClause=IfClause;
        if(IfClause!=null) IfClause.setParent(this);
        this.ElseClause=ElseClause;
        if(ElseClause!=null) ElseClause.setParent(this);
    }

    public IfClause getIfClause() {
        return IfClause;
    }

    public void setIfClause(IfClause IfClause) {
        this.IfClause=IfClause;
    }

    public ElseClause getElseClause() {
        return ElseClause;
    }

    public void setElseClause(ElseClause ElseClause) {
        this.ElseClause=ElseClause;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfClause!=null) IfClause.accept(visitor);
        if(ElseClause!=null) ElseClause.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfClause!=null) IfClause.traverseTopDown(visitor);
        if(ElseClause!=null) ElseClause.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfClause!=null) IfClause.traverseBottomUp(visitor);
        if(ElseClause!=null) ElseClause.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfStatement(\n");

        if(IfClause!=null)
            buffer.append(IfClause.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ElseClause!=null)
            buffer.append(ElseClause.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfStatement]");
        return buffer.toString();
    }
}
