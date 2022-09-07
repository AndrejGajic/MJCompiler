// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class ProgramConstants extends ProgVar {

    private Constants Constants;

    public ProgramConstants (Constants Constants) {
        this.Constants=Constants;
        if(Constants!=null) Constants.setParent(this);
    }

    public Constants getConstants() {
        return Constants;
    }

    public void setConstants(Constants Constants) {
        this.Constants=Constants;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Constants!=null) Constants.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Constants!=null) Constants.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Constants!=null) Constants.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ProgramConstants(\n");

        if(Constants!=null)
            buffer.append(Constants.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ProgramConstants]");
        return buffer.toString();
    }
}
