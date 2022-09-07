// generated with ast extension for cup
// version 0.8
// 29/7/2022 17:15:28


package rs.ac.bg.etf.pp1.ast;

public class FactorDesignator extends Factor {

    private Designator Designator;
    private DesignatorAdditional DesignatorAdditional;

    public FactorDesignator (Designator Designator, DesignatorAdditional DesignatorAdditional) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesignatorAdditional=DesignatorAdditional;
        if(DesignatorAdditional!=null) DesignatorAdditional.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesignatorAdditional getDesignatorAdditional() {
        return DesignatorAdditional;
    }

    public void setDesignatorAdditional(DesignatorAdditional DesignatorAdditional) {
        this.DesignatorAdditional=DesignatorAdditional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesignatorAdditional!=null) DesignatorAdditional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesignatorAdditional!=null) DesignatorAdditional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesignatorAdditional!=null) DesignatorAdditional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDesignator(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorAdditional!=null)
            buffer.append(DesignatorAdditional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDesignator]");
        return buffer.toString();
    }
}
