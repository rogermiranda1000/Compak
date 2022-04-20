package mips;

public class Register {
    //TODO: atribut que indica quan es llibera el registre
    private int positionThatEnds;
    //Estat del registre
    private boolean status;

    public Register(int positionThatEnds) {
        this.status = false;
        this.positionThatEnds = positionThatEnds;
    }

    public int getPositionThatEnds() {
        return positionThatEnds;
    }

    public void setPositionThatEnds(int positionThatEnds) {
        this.positionThatEnds = positionThatEnds;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
