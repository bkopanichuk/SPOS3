package spos.lab3;

// Java program to implement Round Robin
// Scheduling with different arrival time 
class RoundRobin {
    public static void roundRobin(String processes[], int arrivalT[], int burstT[], int quantumT)
    {
        // result of average times 
        int res = 0;
        int resc = 0;

        // for sequence storage 
        String sequence = new String();

        // copy the burst array and arrival array 
        // for not effecting the actual array 
        int burstTime[] = new int[burstT.length];
        int arrivalTime[] = new int[arrivalT.length];

        for (int i = 0; i < burstTime.length; i++) {
            burstTime[i] = burstT[i];
            arrivalTime[i] = arrivalT[i];
        }

        // critical time of system
        int programTime = 0;

        // for store the waiting time
        int waitingTime[] = new int[processes.length];

        // for store the Completion time
        int completionTime[] = new int[processes.length];

        while (true) {
            boolean flag = true;
            for (int i = 0; i < processes.length; i++) {

                // these condition for if 
                // arrival is not on zero 

                // check that if there come before qtime 
                if (arrivalTime[i] <= programTime) {
                    if (arrivalTime[i] <= quantumT) {
                        if (burstTime[i] > 0) {
                            flag = false;
                            if (burstTime[i] > quantumT) {

                                // make decrease the burstT time
                                programTime = programTime + quantumT;
                                burstTime[i] = burstTime[i] - quantumT;
                                arrivalTime[i] = arrivalTime[i] + quantumT;
                                sequence += "->" + processes[i];
                            }
                            else {

                                // for last time
                                programTime = programTime + burstTime[i];

                                // store completionTime time
                                completionTime[i] = programTime - arrivalT[i];

                                // store wait time
                                waitingTime[i] = programTime - burstT[i] - arrivalT[i];
                                burstTime[i] = 0;

                                // add sequence 
                                sequence += "->" + processes[i];
                            }
                        }
                    }
                    else if (arrivalTime[i] > quantumT) {

                        // is any have less arrival time
                        // the coming process then execute them 
                        for (int j = 0; j < processes.length; j++) {

                            // compare 
                            if (arrivalTime[j] < arrivalTime[i]) {
                                if (burstTime[j] > 0) {
                                    flag = false;
                                    if (burstTime[j] > quantumT) {
                                        programTime = programTime + quantumT;
                                        burstTime[j] = burstTime[j] - quantumT;
                                        arrivalTime[j] = arrivalTime[j] + quantumT;
                                        sequence += "->" + processes[j];
                                    }
                                    else {
                                        programTime = programTime + burstTime[j];
                                        completionTime[j] = programTime - arrivalT[j];
                                        waitingTime[j] = programTime - burstT[j] - arrivalT[j];
                                        burstTime[j] = 0;
                                        sequence += "->" + processes[j];
                                    }
                                }
                            }
                        }

                        // now the previous porcess according to 
                        // ith is process 
                        if (burstTime[i] > 0) {
                            flag = false;

                            // Check for greaters 
                            if (burstTime[i] > quantumT) {
                                programTime = programTime + quantumT;
                                burstTime[i] = burstTime[i] - quantumT;
                                arrivalTime[i] = arrivalTime[i] + quantumT;
                                sequence += "->" + processes[i];
                            }
                            else {
                                programTime = programTime + burstTime[i];
                                completionTime[i] = programTime - arrivalT[i];
                                waitingTime[i] = programTime - burstT[i] - arrivalT[i];
                                burstTime[i] = 0;
                                sequence += "->" + processes[i];
                            }
                        }
                    }
                }

                // if no process is come on thse critical 
                else if (arrivalTime[i] > programTime) {
                    programTime++;
                    i--;
                }
            }
            // for exit the while loop 
            if (flag) {
                break;
            }
        }

        System.out.println("name  ctime  wtime");
        for (int i = 0; i < processes.length; i++) {
            System.out.println(" " + processes[i] + "    " + completionTime[i]
                    + "    " + waitingTime[i]);

            res = res + waitingTime[i];
            resc = resc + completionTime[i];
        }

        System.out.println("Average waiting time is "
                + (float)res / processes.length);
        System.out.println("Average compilation  time is "
                + (float)resc / processes.length);
        System.out.println("Sequence: " + sequence);
    }

    // Driver Code
    public static void main(String args[])
    {
        // name of the process
        String name[] = { "p1", "p2", "p3", "p4" };

        // arrival for every process
        int arrivaltime[] = {0, 1, 2, 3};

        // burst time for every process
        int bursttime[] = {10, 4, 5, 3};

        // quantum time of each process
        int q = 3;

        // cal the function for output
        roundRobin(name, arrivaltime, bursttime, q);
    }
} 