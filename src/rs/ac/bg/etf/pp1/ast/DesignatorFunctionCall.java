// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class DesignatorFunctionCall extends DesignatorOp {

    private FunctionCallArgs FunctionCallArgs;

    public DesignatorFunctionCall (FunctionCallArgs FunctionCallArgs) {
        this.FunctionCallArgs=FunctionCallArgs;
        if(FunctionCallArgs!=null) FunctionCallArgs.setParent(this);
    }

    public FunctionCallArgs getFunctionCallArgs() {
        return FunctionCallArgs;
    }

    public void setFunctionCallArgs(FunctionCallArgs FunctionCallArgs) {
        this.FunctionCallArgs=FunctionCallArgs;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FunctionCallArgs!=null) FunctionCallArgs.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FunctionCallArgs!=null) FunctionCallArgs.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FunctionCallArgs!=null) FunctionCallArgs.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorFunctionCall(\n");

        if(FunctionCallArgs!=null)
            buffer.append(FunctionCallArgs.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorFunctionCall]");
        return buffer.toString();
    }
}
