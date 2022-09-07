// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class ProgramVariables extends ProgVar {

    private GlobalVars GlobalVars;

    public ProgramVariables (GlobalVars GlobalVars) {
        this.GlobalVars=GlobalVars;
        if(GlobalVars!=null) GlobalVars.setParent(this);
    }

    public GlobalVars getGlobalVars() {
        return GlobalVars;
    }

    public void setGlobalVars(GlobalVars GlobalVars) {
        this.GlobalVars=GlobalVars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(GlobalVars!=null) GlobalVars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(GlobalVars!=null) GlobalVars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(GlobalVars!=null) GlobalVars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgramVariables(\n");

        if(GlobalVars!=null)
            buffer.append(GlobalVars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramVariables]");
        return buffer.toString();
    }
}
