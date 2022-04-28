package entities;

public class Tag {
    private String name1;
    private String name2;
    private String varIterate;
    private boolean reserved;

    public static int index = 0;

    public Tag() {
        this.name1 = "L" + index;
        this.name2 = "L" + (index+1);
        index += 2;
        this.reserved = false;
    }

    public Tag(String varIterate) {
        this.varIterate = varIterate;
        this.name1 = "L" + index;
        this.name2 = "L" + (index+1);
        index += 2;
        this.reserved = false;
    }

    public String getVarIterate() {
        return varIterate;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved() {
        this.reserved = true;
    }
}
