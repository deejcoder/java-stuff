package Intro;

import mpi.MPI;

import java.util.*;

public class New {

    //Constants
    private static final int MAX_TRAINING = 150;
    private static final int MAX_TESTING = 15;
    private static final int ROOT = 0;
    private static final int k = 3;

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

                }
            }

            //Only take the top K for this process: minimizing what needs to be sent
            SortedMap<Double, String> top = new TreeMap<>();
            for(Map.Entry<Double, String> entry : distances.entrySet()) {
                if(top.size() >= k) break;

                top.put(entry.getKey(), entry.getValue());
            }
            result.add(top);
        }


        Object[] array = result.toArray();

        MPI.COMM_WORLD.Gather(array, 0, array.length, MPI.OBJECT, out, 0, array.length, MPI.OBJECT, ROOT);

        if(rank == ROOT) {

            List<String> names = new ArrayList<String>();

            int i = 0;
            for(Object o : out) {
                if(o == null) continue;
                SortedMap<Double, String> tmp = (SortedMap<Double,String>) o;

                for(int t = 0; t < size; t++) {
                    if(out[i+MAX_TESTING] != null) {
                        tmp.putAll((SortedMap<Double, String>) out[i + MAX_TESTING]);
                    }
                    out[i+MAX_TESTING] = null;
                }
                if(tmp != null) {
                    if(!tmp.isEmpty()) {
                        //System.out.println(tmp.firstKey());
                        names.add(tmp.get(tmp.firstKey()));
                    }
                }
                i++;
            }

            for(int j = 0; j < names.size(); j++) {
                System.out.println(testing[j][4] + ", " + names.get(j));
            }
            System.out.println("done");
        }
    }
}
