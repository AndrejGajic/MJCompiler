// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class ProgramVariablesList extends ProgVarList {

    private ProgVarList ProgVarList;
    private ProgVar ProgVar;

    public ProgramVariablesList (ProgVarList ProgVarList, ProgVar ProgVar) {
        this.ProgVarList=ProgVarList;
        if(ProgVarList!=null) ProgVarList.setParent(this);
        this.ProgVar=ProgVar;
        if(ProgVar!=null) ProgVar.setParent(this);
    }

    public ProgVarList getProgVarList() {
        return ProgVarList;
    }

    public void setProgVarList(ProgVarList ProgVarList) {
        this.ProgVarList=ProgVarList;
    }

    public ProgVar getProgVar() {
        return ProgVar;
    }

    public void setProgVar(ProgVar ProgVar) {
        this.ProgVar=ProgVar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ProgVarList!=null) ProgVarList.accept(visitor);
        if(ProgVar!=null) ProgVar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ProgVarList!=null) ProgVarList.traverseTopDown(visitor);
        if(ProgVar!=null) ProgVar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ProgVarList!=null) ProgVarList.traverseBottomUp(visitor);
        if(ProgVar!=null) ProgVar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgramVariablesList(\n");

        if(ProgVarList!=null)
            buffer.append(ProgVarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ProgVar!=null)
            buffer.append(ProgVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramVariablesList]");
        return buffer.toString();
    }
}
