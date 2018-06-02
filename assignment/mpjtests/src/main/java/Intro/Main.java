package Intro;
/*
    Run configuration:
    -jar $MPJ_HOME$\lib\starter.jar Intro.Main -np 4
    -np 4 specifies that four processes should be initialized.

    Deadlock is still possible. There are synchronized methods
    where required: MPI.Ssend() to provide safety.
 */
import mpi.*;
public class Main {

    public static void main(String [] args) throws Exception {

        MPI.Init(args) ;

        //Rank provides a numeric representation of a process
        int rank = MPI.COMM_WORLD.Rank();
        //Size provides the total number of processes spawned
        int size = MPI.COMM_WORLD.Size();

        //Is it the master/first process spawned?
        if (rank == 0) { // master
            //If so, broadcast a message: Hello slave
            char [] seq1 = "Hello slave".toCharArray();
            MPI.COMM_WORLD.Bcast(seq1, 0, seq1.length, MPI.CHAR, 0);
        }
        else {
            //Get any messages that have been broadcast
            char [] seq1 = new char[100];
            MPI.COMM_WORLD.Bcast(seq1, 0, seq1.length, MPI.CHAR, 0);
            //Master = rank 0, thus slaves #1-3 for 4 processes
            System.out.println("I'm slave " + rank + " of " + (size-1) + " ==> Received from master : " + new String(seq1));
        }

        MPI.Finalize();
    }
}