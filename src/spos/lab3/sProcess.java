package spos.lab3;

public class sProcess {
    public int cputime;
    public int ioblocking;
    public int cpudone;
    public int ionext;
    public int numblocked;
    public int arrivalTime;

    public sProcess (int cputime, int ioblocking, int cpudone, int ionext, int numblocked, int arrivalTime) {
        this.cputime = cputime;
        this.ioblocking = ioblocking;
        this.cpudone = cpudone;
        this.ionext = ionext;
        this.numblocked = numblocked;
        this.arrivalTime = arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public sProcess(int cputime, int ioblocking, int i, int i1, int i2) {
    }
}

