package spos.lab3;

public class sProcess {
    public int cputime;
    public int waitingTime;
    public int cpudone;
    public int ionext;
    public int numblocked;
    public int arrivalTime;
    public int ioblocking;
    public int timeBeforeBlocking;

    public sProcess (int cputime, int waitingTime, int cpudone, int ionext, int numblocked, int arrivalTime, int ioblocking) {
        this.cputime = cputime;
        this.waitingTime = waitingTime;
        this.cpudone = cpudone;
        this.ionext = ionext;
        this.numblocked = numblocked;
        this.arrivalTime = arrivalTime;//added
        this.ioblocking = ioblocking;
        this.timeBeforeBlocking = ioblocking;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public sProcess(int cputime, int waitingTime, int i, int i1, int i2) {
    }
}

