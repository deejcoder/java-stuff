package Intro;

import mpi.MPI;

import java.util.*;

public class New {

    //Constants
    private static final int MAX_TRAINING = 135;
    private static final int MAX_TESTING = 15;
    private static final int ROOT = 0;
    private static final int MAX_NEIGH = 3;

    public static void main(String[] args) throws Exception {


        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        //How many elements of the training set to give to each thread
        final int sendSize = MAX_TRAINING/size;

        //This is the initial training set that is loaded from iris.data
        String[][] training = new String[MAX_TRAINING][];

        //Local storage for the training set
        String[][] localTrain = new String[sendSize][5];

        /*
            Local copies of the testing set for all threads
            This, would be beneficial if it were to be shared memory
         */
        String[][] testing = new String[MAX_TESTING][5];

        //The final result gathered back to the main thread (ROOT)
        Object[] out = new Object[MAX_TESTING*size];



        /*
            The main process/ROOT will load the data set,
            selecting 15 random test cases and fetching the
            remainder 135 to form the training set.

            The test cases are then broadcasted to all processes.
         */
        if(rank == ROOT) {

            Loader.load(training, testing, "iris.data");
            System.out.println("loaded");
            MPI.COMM_WORLD.Bcast(testing, 0, testing.length, MPI.OBJECT, ROOT);

        }
        else {
            //Receive the broadcasted test cases
            MPI.COMM_WORLD.Bcast(testing, 0, testing.length, MPI.OBJECT, ROOT);
        }


        //SCATTER the training set across all processes evenly
        //WARNING let n = # threads, then sendSize will be some float. Not all will be sent.
        System.out.println(sendSize);
        MPI.COMM_WORLD.Scatter(training, 0, sendSize, MPI.OBJECT, localTrain, 0, sendSize, MPI.OBJECT, ROOT);


        /*
            Now, for every test case,
            compute the distances for every training vector, to the test case and
            then add it into a sorted list.
         */
        List<SortedMap<Double, String>> result = new ArrayList<>();
        for(String[] test : testing) {


            SortedMap<Double, String> distances = new TreeMap<>();

            for(String[] train : localTrain) {

                try {
                    distances.put(
                            Math.sqrt(
                                    Math.pow(Float.parseFloat(train[0]) - Float.parseFloat(test[0]), 2) +
                                    Math.pow(Float.parseFloat(train[1]) - Float.parseFloat(test[1]), 2) +
                                    Math.pow(Float.parseFloat(train[2]) - Float.parseFloat(test[2]), 2) +
                                    Math.pow(Float.parseFloat(train[3]) - Float.parseFloat(test[3]), 2)
                            ), train[4]
                    );
                }
                catch(NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }

            //Only take the top K for this process: minimizing what needs to be sent
            SortedMap<Double, String> top = new TreeMap<>();
            //Put the test case at the front, just the class
            double key = Float.parseFloat(test[0]) + Float.parseFloat(test[1]) + Float.parseFloat(test[2]) + Float.parseFloat(test[3]);

            for(Map.Entry<Double, String> entry : distances.entrySet()) {
                if(top.size() - 1 >= MAX_NEIGH) break;

                top.put(entry.getKey(), entry.getValue());
            }
            result.add(top);
        }


        Object[] array = result.toArray();

        MPI.COMM_WORLD.Gather(array, 0, array.length, MPI.OBJECT, out, 0, array.length, MPI.OBJECT, ROOT);



        if(rank == ROOT) {

            List<SortedMap<Double, String>> fin = new ArrayList<>();

            for(int i = 0; i < out.length; i++) {
                if(out[i] == null) continue;
                SortedMap<Double, String> tmp = (SortedMap<Double, String>) out[i];

                for(int k = 0; k < out.length; k++) {
                    if(out[k] == null || i == k) continue;
                    SortedMap<Double, String> val = (SortedMap<Double, String>) out[k];
                    if(val.lastKey().equals(tmp.lastKey()) && val.get(val.lastKey()).equals(tmp.get(tmp.lastKey()))) {
                        tmp.putAll(val);

                        out[k] = null;
                    }
                }

                SortedMap<Double, String> list = new TreeMap<>();
                for(int k = 0; k < MAX_NEIGH; k++)
                {
                    list.put(tmp.firstKey(), tmp.get(tmp.firstKey()));
                    tmp.remove(tmp.firstKey());
                }
                list.put(tmp.lastKey(), tmp.get(tmp.lastKey()));
                fin.add(list);
                out[i] = null;

            }

            for(SortedMap<Double, String> test : fin) {
                System.out.println(test);
            }


            System.out.println("done");
        }
    }
}
