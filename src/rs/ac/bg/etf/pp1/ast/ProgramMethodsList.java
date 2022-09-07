// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class ProgramMethodsList extends ProgMethList {

    private ProgMethList ProgMethList;
    private MethodDecl MethodDecl;

    public ProgramMethodsList (ProgMethList ProgMethList, MethodDecl MethodDecl) {
        this.ProgMethList=ProgMethList;
        if(ProgMethList!=null) ProgMethList.setParent(this);
        this.MethodDecl=MethodDecl;
        if(MethodDecl!=null) MethodDecl.setParent(this);
    }

    public ProgMethList getProgMethList() {
        return ProgMethList;
    }

    public void setProgMethList(ProgMethList ProgMethList) {
        this.ProgMethList=ProgMethList;
    }

    public MethodDecl getMethodDecl() {
        return MethodDecl;
    }

    public void setMethodDecl(MethodDecl MethodDecl) {
        this.MethodDecl=MethodDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ProgMethList!=null) ProgMethList.accept(visitor);
        if(MethodDecl!=null) MethodDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ProgMethList!=null) ProgMethList.traverseTopDown(visitor);
        if(MethodDecl!=null) MethodDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ProgMethList!=null) ProgMethList.traverseBottomUp(visitor);
        if(MethodDecl!=null) MethodDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgramMethodsList(\n");

        if(ProgMethList!=null)
            buffer.append(ProgMethList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDecl!=null)
            buffer.append(MethodDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramMethodsList]");
        return buffer.toString();
    }
}
