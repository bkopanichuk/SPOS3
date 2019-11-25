// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.
package spos.lab3;

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {

    public static Results Run(int runtime, Vector processVector, Results result) {
        int i = 0;
        int comptime = 0;
        int size = processVector.size();
        int completed = 0;
        int quantumT = 300;
        String resultsFile = "Summary-Processes";

        result.schedulingType = "Batch (Preemptive)";
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
                                out.println("Process: " + i + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                if ((process.cputime - process.cpudone) > quantumT) {
                                    comptime = comptime + quantumT; //if we want to use specific quantum time for each process, use process.ioblocking instead of quantumT
                                    process.cpudone += quantumT;
                                    process.arrivalTime += quantumT; //if we want to use specific waiting time for each process, use process.waiting instead of quantumT
                                    out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                    process.numblocked++;
                                } else {
                                    comptime += process.cputime - process.cpudone;
                                    process.cpudone = process.cputime;
                                    out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                    process.numblocked++;
                                    completed++;
                                    if (completed == size) {
                                        result.compuTime = comptime;
                                        out.close();
                                        return result;
                                    }
                                }
                            }
                        } else if (process.arrivalTime > quantumT) {
                            for (int j = 0; j < processVector.size(); j++) {
                                sProcess processTmp = (sProcess) processVector.elementAt(j);
                                if (processTmp.arrivalTime < process.arrivalTime) {
                                    if (processTmp.cputime > processTmp.cpudone) {
                                        flag = false;
                                        out.println("Process: " + j + " registered... (" + processTmp.cputime + " " + processTmp.ioblocking + " " + processTmp.cpudone + " " + comptime + " " + processTmp.arrivalTime + ")");
                                        if ((processTmp.cputime - processTmp.cpudone) > quantumT) {
                                            comptime = comptime + quantumT;
                                            processTmp.cpudone += quantumT;
                                            processTmp.arrivalTime += quantumT;
                                            out.println("Process: " + j + " I/O blocked... (" + processTmp.cputime + " " + processTmp.ioblocking + " " + processTmp.cpudone + " " + comptime + " " + processTmp.arrivalTime + ")");
                                            processTmp.numblocked++;
                                        } else {
                                            comptime += processTmp.cputime - processTmp.cpudone;
                                            processTmp.cpudone = processTmp.cputime;
                                            out.println("Process: " + j + " I/O blocked... (" + processTmp.cputime + " " + processTmp.ioblocking + " " + processTmp.cpudone + " " + comptime + " " + processTmp.arrivalTime + ")");
                                            processTmp.numblocked++;
                                            completed++;
                                            if (completed == size) {
                                                result.compuTime = comptime;
                                                out.close();
                                                return result;
                                            }
                                        }
                                    }
                                }
                            }

                            if (process.cputime > process.cpudone) {
                                flag = false;
                                out.println("Process: " + i + " registered... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                if ((process.cputime - process.cpudone) > quantumT) {
                                    comptime = comptime + quantumT;
                                    process.cpudone += quantumT;
                                    process.arrivalTime += quantumT;
                                    out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                    process.numblocked++;
                                } else {
                                    comptime += process.cputime - process.cpudone;
                                    process.cpudone = process.cputime;
                                    out.println("Process: " + i + " I/O blocked... (" + process.cputime + " " + process.ioblocking + " " + process.cpudone + " " + comptime + " " + process.arrivalTime + ")");
                                    process.numblocked++;
                                    completed++;
                                    if (completed == size) {
                                        result.compuTime = comptime;
                                        out.close();
                                        return result;
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
