// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class DesignatorAssign extends DesignatorOp {

    private DesignatorAssignop DesignatorAssignop;

    public DesignatorAssign (DesignatorAssignop DesignatorAssignop) {
        this.DesignatorAssignop=DesignatorAssignop;
        if(DesignatorAssignop!=null) DesignatorAssignop.setParent(this);
    }

    public DesignatorAssignop getDesignatorAssignop() {
        return DesignatorAssignop;
    }

    public void setDesignatorAssignop(DesignatorAssignop DesignatorAssignop) {
        this.DesignatorAssignop=DesignatorAssignop;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorAssignop!=null) DesignatorAssignop.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorAssignop!=null) DesignatorAssignop.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorAssignop!=null) DesignatorAssignop.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorAssign(\n");

        if(DesignatorAssignop!=null)
            buffer.append(DesignatorAssignop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorAssign]");
        return buffer.toString();
    }
}
