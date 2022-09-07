// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class MultipleLocalVariablesList extends LocalVarList {

    private LocalVarList LocalVarList;
    private VarDecl VarDecl;

    public MultipleLocalVariablesList (LocalVarList LocalVarList, VarDecl VarDecl) {
        this.LocalVarList=LocalVarList;
        if(LocalVarList!=null) LocalVarList.setParent(this);
        this.VarDecl=VarDecl;
        if(VarDecl!=null) VarDecl.setParent(this);
    }

    public LocalVarList getLocalVarList() {
        return LocalVarList;
    }

    public void setLocalVarList(LocalVarList LocalVarList) {
        this.LocalVarList=LocalVarList;
    }

    public VarDecl getVarDecl() {
        return VarDecl;
    }

    public void setVarDecl(VarDecl VarDecl) {
        this.VarDecl=VarDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LocalVarList!=null) LocalVarList.accept(visitor);
        if(VarDecl!=null) VarDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LocalVarList!=null) LocalVarList.traverseTopDown(visitor);
        if(VarDecl!=null) VarDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LocalVarList!=null) LocalVarList.traverseBottomUp(visitor);
        if(VarDecl!=null) VarDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MultipleLocalVariablesList(\n");

        if(LocalVarList!=null)
            buffer.append(LocalVarList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecl!=null)
            buffer.append(VarDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MultipleLocalVariablesList]");
        return buffer.toString();
    }
}
