package entities;

/**
 * Class Tag for TAC Code. It represents tags used in loops and conditionals ifs.
 * Example: L0, L1, ...
 */
public class Tag {
    private String name1;
    private String name2;
    private String varIterate;
    private boolean reserved;

    public static int index = 0;

    /**
     * Instantiates a new Tag.
     *
     * @param varIterate the variable to iterate
     */
    public Tag(String varIterate) {
        this.varIterate = varIterate;
        this.name1 = "L" + index;
        this.name2 = "L" + (index+1);
        index += 2;
        this.reserved = false;
    }

    /**
     * Gets var iterate.
     *
     * @return the var iterate
     */
    public String getVarIterate() {
        return varIterate;
    }

    /**
     * Gets name 1.
     *
     * @return the name 1
     */
    public String getName1() {
        return name1;
    }

    /**
     * Gets name 2.
     *
     * @return the name 2
     */
    public String getName2() {
        return name2;
    }

    /**
     * Is reserved boolean.
     *
     * @return the boolean
     */
    public boolean isReserved() {
        return reserved;
    }

    /**
     * Sets reserved.
     */
    public void setReserved() {
        this.reserved = true;
    }
}
