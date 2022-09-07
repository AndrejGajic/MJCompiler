// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class DoWhileStatement extends SingleStatement {

    private DoWhileStart DoWhileStart;
    private Statement Statement;
    private DoWhileConditionStart DoWhileConditionStart;
    private DoWhileCondition DoWhileCondition;

    public DoWhileStatement (DoWhileStart DoWhileStart, Statement Statement, DoWhileConditionStart DoWhileConditionStart, DoWhileCondition DoWhileCondition) {
        this.DoWhileStart=DoWhileStart;
        if(DoWhileStart!=null) DoWhileStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.DoWhileConditionStart=DoWhileConditionStart;
        if(DoWhileConditionStart!=null) DoWhileConditionStart.setParent(this);
        this.DoWhileCondition=DoWhileCondition;
        if(DoWhileCondition!=null) DoWhileCondition.setParent(this);
    }

    public DoWhileStart getDoWhileStart() {
        return DoWhileStart;
    }

    public void setDoWhileStart(DoWhileStart DoWhileStart) {
        this.DoWhileStart=DoWhileStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public DoWhileConditionStart getDoWhileConditionStart() {
        return DoWhileConditionStart;
    }

    public void setDoWhileConditionStart(DoWhileConditionStart DoWhileConditionStart) {
        this.DoWhileConditionStart=DoWhileConditionStart;
    }

    public DoWhileCondition getDoWhileCondition() {
        return DoWhileCondition;
    }

    public void setDoWhileCondition(DoWhileCondition DoWhileCondition) {
        this.DoWhileCondition=DoWhileCondition;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoWhileStart!=null) DoWhileStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(DoWhileConditionStart!=null) DoWhileConditionStart.accept(visitor);
        if(DoWhileCondition!=null) DoWhileCondition.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoWhileStart!=null) DoWhileStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(DoWhileConditionStart!=null) DoWhileConditionStart.traverseTopDown(visitor);
        if(DoWhileCondition!=null) DoWhileCondition.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoWhileStart!=null) DoWhileStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(DoWhileConditionStart!=null) DoWhileConditionStart.traverseBottomUp(visitor);
        if(DoWhileCondition!=null) DoWhileCondition.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DoWhileStatement(\n");

        if(DoWhileStart!=null)
            buffer.append(DoWhileStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DoWhileConditionStart!=null)
            buffer.append(DoWhileConditionStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DoWhileCondition!=null)
            buffer.append(DoWhileCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DoWhileStatement]");
        return buffer.toString();
    }
}
