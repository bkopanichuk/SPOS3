// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06
package spos.lab3;

import java.io.*;
import java.util.*;

public class Scheduling {

    private static int processnum = 5;
    private static int meanDev = 1000;
    private static int standardDev = 100;
    private static int runtime = 1000;
    private static int quantumT = 800;
    private static Vector processVector = new Vector();
    private static Results result = new Results("null","null",0);
    private static String resultsFile = "Summary-Results";

    private static void Init(String file) {
        File f = new File(file);
        String line;
        int cputime = 0;
        int ioblocking = 0;
        double X = 0.0;
        int counter = 0;
        int counter2 = 0;
        int counter3 = 0;
        int[] arrivalTime = new int[processnum];
        int[] ioblockingTime = new int[processnum];
        for (int i = 0; i < processnum; i++){
            arrivalTime[i] = i*100;
        }

        try {
            BufferedReader in = new BufferedReader(new FileReader(f));
//            DataInputStream in = new DataInputStream(new FileInputStream(f));
            while ((line = in.readLine()) != null) {
                if (line.startsWith("numprocess")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    processnum = Common.s2i(st.nextToken());
                    arrivalTime = new int[processnum];
                }
                if (line.startsWith("meandev")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    meanDev = Common.s2i(st.nextToken());
                }
                if (line.startsWith("standdev")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    standardDev = Common.s2i(st.nextToken());
                }
                if (line.startsWith("arrival")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    arrivalTime[counter] = Common.s2i(st.nextToken());
                    counter++;
                }
                if (line.startsWith("blocking")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    ioblockingTime[counter3] = Common.s2i(st.nextToken());
                    counter3++;
                }
                if (line.startsWith("waiting")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    ioblocking = Common.s2i(st.nextToken());
                    X = Common.R1();
                    while (X == -1.0) {
                        X = Common.R1();
                    }
                    X = X * standardDev;
                    cputime = (int) X + meanDev;
                    processVector.addElement(new sProcess(cputime, ioblocking, 0, 0, 0, arrivalTime[counter2], ioblockingTime[counter2]));
                    counter2++;
                }
                if (line.startsWith("runtime")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    runtime = Common.s2i(st.nextToken());
                }
                if (line.startsWith("quantum")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    quantumT = Common.s2i(st.nextToken());
                }
            }
            in.close();
        } catch (IOException e) { /* Handle exceptions */ }
    }

    private static void debug() {
        int i = 0;

        System.out.println("processnum " + processnum);
        System.out.println("meandevm " + meanDev);
        System.out.println("standdev " + standardDev);
        int size = processVector.size();
        for (i = 0; i < size; i++) {
            sProcess process = (sProcess) processVector.elementAt(i);
            System.out.println("process " + i + " " + process.cputime + " " + process.waitingTime + " " + process.cpudone + " " + process.numblocked);
        }
        System.out.println("runtime " + runtime);
    }

    public static void main(String[] args) {
        int i = 0;

        if (args.length != 1) {
            System.out.println("Usage: 'java Scheduling <INIT FILE>'");
            System.exit(-1);
        }
        File f = new File(args[0]);
        if (!(f.exists())) {
            System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
            System.exit(-1);
        }
        if (!(f.canRead())) {
            System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
            System.exit(-1);
        }
        System.out.println("Working...");
        Init(args[0]);
        if (processVector.size() < processnum) {
            i = 0;
            while (processVector.size() < processnum) {
                double X = Common.R1();
                while (X == -1.0) {
                    X = Common.R1();
                }
                X = X * standardDev;
                int cputime = (int) X + meanDev;
                processVector.addElement(new sProcess(cputime,i*100,0,0,0,(int)Math.random()*500, (int)Math.random()*500));
                i++;
            }
        }
        result = SchedulingAlgorithm.Run(runtime, processVector, result, quantumT);
        try {
            //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
            PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
            out.println("Scheduling Type: " + result.schedulingType);
            out.println("Scheduling Name: " + result.schedulingName);
            out.println("Simulation Run Time: " + result.compuTime);
            out.println("Mean: " + meanDev);
            out.println("Standard Deviation: " + standardDev);
            out.println("Process #\tCPU Time\tWaiting time\tCPU Completed\tCPU Blocked\tI/O blocking");
            for (i = 0; i < processVector.size(); i++) {
                sProcess process = (sProcess) processVector.elementAt(i);
                out.print(Integer.toString(i));
                if (i < 100) { out.print("\t\t"); } else { out.print("\t"); }
                out.print(Integer.toString(process.cputime));
                if (process.cputime < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
                out.print(Integer.toString(process.waitingTime));
                if (process.waitingTime < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
                out.print(Integer.toString(process.cpudone));
                if (process.cpudone < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
                out.print(Integer.toString(process.numblocked));
                if (process.numblocked < 100) { out.print(" times\t\t"); } else { out.print(" times\t"); }
                out.println(Integer.toString(process.ioblocking));
            }
            out.close();
        } catch (IOException e) { /* Handle exceptions */ }
        System.out.println("Completed.");
    }
}

