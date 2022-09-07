// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class FormalParameter extends FormPar {

    private Type Type;
    private String formParName;
    private VarArray VarArray;

    public FormalParameter (Type Type, String formParName, VarArray VarArray) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.formParName=formParName;
        this.VarArray=VarArray;
        if(VarArray!=null) VarArray.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getFormParName() {
        return formParName;
    }

    public void setFormParName(String formParName) {
        this.formParName=formParName;
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
        if(Type!=null) Type.accept(visitor);
        if(VarArray!=null) VarArray.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(VarArray!=null) VarArray.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(VarArray!=null) VarArray.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParameter(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+formParName);
        buffer.append("\n");

        if(VarArray!=null)
            buffer.append(VarArray.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParameter]");
        return buffer.toString();
    }
}
