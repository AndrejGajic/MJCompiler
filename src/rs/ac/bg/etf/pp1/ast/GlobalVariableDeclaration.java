// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class GlobalVariableDeclaration extends GlobalVarDecl {

    private String globalVarName;
    private VarArray VarArray;

    public GlobalVariableDeclaration (String globalVarName, VarArray VarArray) {
        this.globalVarName=globalVarName;
        this.VarArray=VarArray;
        if(VarArray!=null) VarArray.setParent(this);
    }

    public String getGlobalVarName() {
        return globalVarName;
    }

    public void setGlobalVarName(String globalVarName) {
        this.globalVarName=globalVarName;
    }

    public VarArray getVarArray() {
        return VarArray;
    }

    public void setVarArray(VarArray VarArray) {
        this.VarArray=VarArray;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarArray!=null) VarArray.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarArray!=null) VarArray.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarArray!=null) VarArray.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("GlobalVariableDeclaration(\n");

        buffer.append(" "+tab+globalVarName);
        buffer.append("\n");

        if(VarArray!=null)
            buffer.append(VarArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [GlobalVariableDeclaration]");
        return buffer.toString();
    }
}
