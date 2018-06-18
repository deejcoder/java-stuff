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
        final int MAX_DATA = 150;
        int sendSize;

        String[] data = new String[MAX_DATA];


        MPI.Init(args);

        int size = MPI.COMM_WORLD.Size();
        int rank = MPI.COMM_WORLD.Rank();

        sendSize = MAX_DATA/size;
        String[] local = new String[sendSize];
        String[] result = new String[MAX_DATA];


        if(rank == MASTER) {
            //ArrayList<String> tmp = Loader.load("iris.data");

            /*System.arraycopy(
                    tmp.toArray(new String[MAX_DATA]),
                    0,
                    data,
                    0,
                    MAX_DATA
            );*/

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

        HashMap<String, ArrayList<double[]>> vectors = new HashMap<String, ArrayList<double[]>>();


        for(String line : local) {
            String[] values = line.split(",");
            double[] vector = new double[4];
            String name = "";

            for(int i = 0; i < values.length; i++) {
                try {
                    vector[i] = Double.parseDouble(values[i]);
                }
                catch(NumberFormatException ex) {
                    name = values[i];
                }
            }

            if(vectors.containsKey(name)) {
                vectors.get(name).add(vector);
            }
            else {
                ArrayList<double[]> tmp = new ArrayList<double[]>();
                tmp.add(vector);
                vectors.put(name, tmp);
            }


        }

        for(String value : vectors.keySet()) {
            System.out.println(rank + " : " + value + " " + vectors.get(value));
        }



        /*
            GATHER THE DATA
         */
        MPI.COMM_WORLD.Gather(local, 0, sendSize, MPI.OBJECT, result, 0, sendSize, MPI.OBJECT, MASTER);

        if(rank == MASTER) {

        }

        //====

    }
}