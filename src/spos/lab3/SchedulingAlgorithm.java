// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
package spos.lab3;

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    public static Results Run(int runtime, Vector processVector, Results result, int quantumT) {
        int i = 0;
        int comptime = 0;
        int size = processVector.size();
        int completed = 0;
        String resultsFile = "Summary-Processes";

        result.schedulingType = "Static scheduling (clock-driven) (Preemptive)";
        result.schedulingName = "Round Robin";
        try {
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

            while (comptime < runtime) {
                boolean flag = true;
                for (i = 0; i < processVector.size(); i++) {
                    sProcess process = (sProcess) processVector.elementAt(i);

                    if (process.arrivalTime <= comptime) {
                        if (process.arrivalTime <= quantumT) {
                            if (process.cputime > process.cpudone) {
                                flag = false;
                                out.println("Process: " + i + " registered... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                if ((process.cputime - process.cpudone) > quantumT) {
                                    if (process.timeBeforeBlocking > quantumT) {
                                        comptime = comptime + quantumT;
                                        process.cpudone += quantumT;
                                        process.timeBeforeBlocking -= quantumT;
                                        out.println("Process: " + i + " Quantum expiration... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                        i--;
                                    } else {
                                        comptime = comptime + process.timeBeforeBlocking;
                                        process.cpudone += process.timeBeforeBlocking;
                                        process.arrivalTime = comptime + process.waitingTime;
                                        process.timeBeforeBlocking = process.ioblocking;
                                        out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                        process.numblocked++;
                                    }
                                } else {
                                    if (process.timeBeforeBlocking >= (process.cputime - process.cpudone)) {
                                        comptime += process.cputime - process.cpudone;
                                        process.cpudone = process.cputime;
                                        out.println("Process: " + i + " Burst time expiration... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                        process.numblocked++;
                                        completed++;
                                        if (completed == size) {
                                            result.compuTime = comptime;
                                            out.close();
                                            return result;
                                        }
                                    } else {
                                        comptime = comptime + process.timeBeforeBlocking;
                                        process.cpudone += process.timeBeforeBlocking;
                                        process.arrivalTime = comptime + process.waitingTime;
                                        process.timeBeforeBlocking = process.ioblocking;
                                        out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                        process.numblocked++;
                                    }
                                }
                            }
                        } else if (process.arrivalTime > quantumT) {
                            for (int j = 0; j < processVector.size(); j++) {
                                sProcess processTmp = (sProcess) processVector.elementAt(j);
                                if (processTmp.arrivalTime < process.arrivalTime) {
                                    if (processTmp.cputime > processTmp.cpudone) {
                                        flag = false;
                                        out.println("Process: " + j + " registered... (" + processTmp.cputime + " " + processTmp.waitingTime + " " + processTmp.cpudone + " " + comptime + " " + processTmp.arrivalTime + ")");
                                        if ((processTmp.cputime - processTmp.cpudone) > quantumT) {
                                            if (processTmp.timeBeforeBlocking > quantumT) {
                                                comptime = comptime + quantumT;
                                                processTmp.cpudone += quantumT;
                                                processTmp.timeBeforeBlocking -= quantumT;
                                                out.println("Process: " + j + " Quantum expiration... (" + processTmp.cputime + " " + processTmp.waitingTime + " " + processTmp.cpudone + " " + comptime + " " + processTmp.arrivalTime + ")");
                                                j--;
                                            } else {
                                                comptime = comptime + processTmp.timeBeforeBlocking;
                                                processTmp.cpudone += processTmp.timeBeforeBlocking;
                                                processTmp.arrivalTime = comptime + processTmp.waitingTime;
                                                processTmp.timeBeforeBlocking = processTmp.ioblocking;
                                                out.println("Process: " + j + " I/O blocked... (" + processTmp.cputime + " " + processTmp.waitingTime + " " + processTmp.cpudone + " " + comptime + " " + processTmp.arrivalTime + ")");
                                                processTmp.numblocked++;
                                            }
                                        } else {
                                            if (processTmp.timeBeforeBlocking >= (processTmp.cputime - processTmp.cpudone)) {
                                                comptime += processTmp.cputime - processTmp.cpudone;
                                                processTmp.cpudone = processTmp.cputime;
                                                out.println("Process: " + j + " Burst time expiration... (" + processTmp.cputime + " " + processTmp.waitingTime + " " + processTmp.cpudone + " " + comptime + " " + processTmp.arrivalTime + ")");
                                                processTmp.numblocked++;
                                                completed++;
                                                if (completed == size) {
                                                    result.compuTime = comptime;
                                                    out.close();
                                                    return result;
                                                }
                                            } else {
                                                comptime = comptime + processTmp.timeBeforeBlocking;
                                                processTmp.cpudone += processTmp.timeBeforeBlocking;
                                                processTmp.arrivalTime = comptime + processTmp.waitingTime;
                                                processTmp.timeBeforeBlocking = processTmp.ioblocking;
                                                out.println("Process: " + j + " I/O blocked... (" + processTmp.cputime + " " + processTmp.waitingTime + " " + processTmp.cpudone + " " + comptime + " " + processTmp.arrivalTime + ")");
                                                processTmp.numblocked++;
                                            }
                                        }
                                    }
                                }
                            }

                            if (process.cputime > process.cpudone) {
                                flag = false;
                                out.println("Process: " + i + " registered... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                if ((process.cputime - process.cpudone) > quantumT) {
                                    if (process.timeBeforeBlocking > quantumT) {
                                        comptime = comptime + quantumT;
                                        process.cpudone += quantumT;
                                        process.timeBeforeBlocking -= quantumT;
                                        out.println("Process: " + i + " Quantum expiration... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                        i--;
                                    } else {
                                        comptime = comptime + process.timeBeforeBlocking;
                                        process.cpudone += process.timeBeforeBlocking;
                                        process.arrivalTime = comptime + process.waitingTime;
                                        process.timeBeforeBlocking = process.ioblocking;
                                        out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                        process.numblocked++;
                                    }
                                } else {
                                    if (process.timeBeforeBlocking >= (process.cputime - process.cpudone)) {
                                        comptime += process.cputime - process.cpudone;
                                        process.cpudone = process.cputime;
                                        out.println("Process: " + i + " Burst time expiration... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                        process.numblocked++;
                                        completed++;
                                        if (completed == size) {
                                            result.compuTime = comptime;
                                            out.close();
                                            return result;
                                        }
                                    } else {
                                        comptime = comptime + process.timeBeforeBlocking;
                                        process.cpudone += process.timeBeforeBlocking;
                                        process.arrivalTime = comptime + process.waitingTime;
                                        process.timeBeforeBlocking = process.ioblocking;
                                        out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                        process.numblocked++;
                                    }
                                }
                            }
                        }
                    } else if (process.arrivalTime > comptime) {
                        comptime++;
                        i--;
                    }
                }
                if (flag) {
                    break;
                }
            }
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }
        result.compuTime = comptime;
        return result;
    }
}
