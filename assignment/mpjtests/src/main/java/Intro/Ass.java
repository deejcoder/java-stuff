package Intro;

import mpi.MPI;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.*;

/*
    ALGORITHM:
    1. Master divides the training set into equal subsets, depending on the # of processes.
    2. Each slave receives a subset to...
        a) compute the distance for each vector
        b) store all these results to some matrix/array
        c) transmit the result back to the master
    3. The master will then use the results to find k closest neighbors (only sort k)
 */
public class Ass {
    public static void main(String[] args) throws Exception {

        final int MASTER = 0;
        final int MAX_DATA = 20;
        int sendSize;

        Map<double[], String> iris;




        double[][] data = new double[100][5];



        double[] result = new double[20];

        MPI.Init(args);

        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();

        sendSize = 20/size;
        double[][] local = new double[5][5];


        if(rank == MASTER) {
            iris = Loader.load("iris.data");
            System.out.println(iris.size());
            int iris_size = iris.size();
            System.arraycopy(iris.keySet().toArray(), 0, data, 0, 20);

        }

        //System.out.println(data);

        /*
            SCATTER THE DATA
         */
        MPI.COMM_WORLD.Scatter(data, 0, sendSize, MPI.OBJECT, local, 0, sendSize, MPI.OBJECT, MASTER);

        //====


        /*
            The following is processed by all processes
         */

        int index = 0;
        double localresult[] = new double[20];
        for(double[] vector : local) {
            double sum = 0.0;
            for (int i = 0; i < 4; i++) {
                sum += vector[i];
            }
            localresult[index] = sum;
            index ++;
        }




        /*
            GATHER THE DATA
         */
        MPI.COMM_WORLD.Gather(localresult, 0, sendSize, MPI.DOUBLE, result, 0, sendSize, MPI.DOUBLE, MASTER);

        if(rank == MASTER) {

            ArrayList<double>
            for(double value : result) {
                System.out.println(value);
            }
        }

        //====

    }
}