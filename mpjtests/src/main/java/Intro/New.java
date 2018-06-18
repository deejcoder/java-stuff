package Intro;

/**
 * @Author: Dylan Tonks (16058989)
 * Current Systems 159.355
 * Assignment
 */

import mpi.MPI;

import java.util.*;

public class New {

    //Constants
    private static final int MAX_LINES = 150;
    private static final int MAX_TESTING = 15; //The number of test cases
    private static final int MAX_TRAINING = MAX_LINES - MAX_TESTING; //# of training
    private static final int MAX_CLASSES = 3; //# of plants

    private static final int ROOT = 0; //The root process
    private static final int MAX_NEIGH = 5; //In the assignment doc, this is K

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

        /*==============================================================================================================
            The main process/ROOT will load the data set,
            selecting 15 random test cases and fetch the
            remainder 135 to form the training set.

            The test cases are then broadcasted to all processes.
        ==============================================================================================================*/
        if(rank == ROOT) {

            Loader.load(training, testing, "iris.data");
            System.out.println("loaded");
            MPI.COMM_WORLD.Bcast(testing, 0, testing.length, MPI.OBJECT, ROOT);

        }
        else {
            //Receive the broadcasted test cases
            MPI.COMM_WORLD.Bcast(testing, 0, testing.length, MPI.OBJECT, ROOT);
        }


        /*==============================================================================================================
            1. Scatter the training set across all threads.
            2. For each test case, compute all distances.
            3. Return a SortedMap, consisting of K distances, and the original test case at the end.
            Minimizes the communication across the threads.
        ==============================================================================================================*/

        //WARNING let n = # threads, then sendSize will be some float. Not all will be sent.
        MPI.COMM_WORLD.Scatter(training, 0, sendSize, MPI.OBJECT, localTrain, 0, sendSize, MPI.OBJECT, ROOT);

        //===
        List<SortedMap<Double, String>> result = new ArrayList<>();
        for(String[] test : testing) {


            //This will store all the distances for a particular test case
            SortedMap<Double, String> distances = new TreeMap<>();

            for(String[] train : localTrain) {

                try {

                    /*
                        I'm aware this can be thrown into a loop, it just looks more logical.
                        sqrt((train[0] - test[0])^2 + ... + (train[3] - test[3])^2))
                        Pre-parsing the floats would be more time consuming
                     */
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
                    //None of these issues should arise
                    ex.printStackTrace();
                }
            }

            /*
                GENERATING THE RESULT FOR EACH THREAD
             */
            //Only take the top K for this process: minimizing what needs to be sent
            SortedMap<Double, String> top = new TreeMap<>();

            //Put the test case at the end (it's a sorted map), testKey := a unique key representing the test case
            double testKey = Float.parseFloat(test[0]) + Float.parseFloat(test[1]) + Float.parseFloat(test[2]) + Float.parseFloat(test[3]);
            top.put(testKey, test[4]);


            for(Map.Entry<Double, String> entry : distances.entrySet()) {
                if(top.size() - 1 >= MAX_NEIGH) break;

                top.put(entry.getKey(), entry.getValue());
            }
            result.add(top);
        }


        //Convert to an array for sending
        Object[] array = result.toArray();

        /*==============================================================================================================
            1. Gather the results from all processes.
            2. The root process shall join the results from each process into their test case.
            3. Each test case will then have their distances sorted and trimmed to the top MAX_NEIGH (K).
            4. The majority or class will be returned for that test case.
        ==============================================================================================================*/

        MPI.COMM_WORLD.Gather(array, 0, array.length, MPI.OBJECT, out, 0, array.length, MPI.OBJECT, ROOT);

        if(rank == ROOT) {

            /*
                Compares one SortedMap with another, and tests if they belong to the same test case.
                If they belong to the same, they are merged.
                It's important to check the objects exist, since they are deleted after merge.
             */
            List<SortedMap<Double, String>> testResults = new ArrayList<>();
            List<String> classes = new ArrayList<>(); //stores the 'majority'

            for(int i = 0; i < out.length; i++) {
                if(out[i] == null) continue;

                SortedMap<Double, String> compare1 = (SortedMap<Double, String>) out[i];

                for(int j = 0; j < out.length; j++) {
                    if(out[j] == null || i == j) continue;

                    SortedMap<Double, String> compare2 = (SortedMap<Double, String>) out[j];
                    Double key1 = compare1.lastKey();
                    Double key2 = compare2.lastKey();

                    //If the last key & value is the same it is the same test case
                    if(key1.equals(key2)) {
                        if(compare1.get(key1).equals(compare2.get(key2))) {

                            //Same memory address
                            compare1.putAll(compare2);
                            out[j] = null;
                        }
                    }
                }

                testResults.add(compare1);
                out[i] = null;
            }

            System.out.println("RAW DATA =====>");
            System.out.println("Note: the raw data is ordered from smallest distance to largest. The last element is a unique identifier and can be disregard.\n" +
                    "Also note that some may be combined. This is normal, and is why sometimes only 14 results are returned. This is because the test case was a duplicate.");
            for(SortedMap<Double, String> test : testResults) {
                System.out.println(test);
            }

            System.out.println("RESULTS =====>");
            //Let's get the accuracy & majority...
            int index = 0;
            for(SortedMap<Double, String> map : testResults) {
                HashMap<String, Integer> counts = new HashMap<>();
                double accuracy = 0.0;

                //Number of neighbors that actually matter (K)
                for (int k = 0; k < MAX_NEIGH; k++) {

                    String testName = map.get(map.firstKey());
                    map.remove(map.firstKey());

                    //Check if this plant has already been counted upon
                    //Basically counts how many occurrences...
                    if(counts.containsKey(testName)) {
                        int count = counts.get(testName);
                        counts.put(testName, count+1);
                    }
                    else {
                        counts.put(testName, 1);
                    }
                }

                //Was there one or more matches?
                try {
                    accuracy = (((double) counts.get(testing[index][4]))/MAX_NEIGH)*100;
                }
                //The test case completely did not match
                catch(NullPointerException ex) {
                    accuracy = 0.0;
                }
                System.out.println("Accuracy: " + accuracy + ", " + counts + " |<>| test case: " + testing[index][0] + "," + testing[index][1] + "," + testing[index][2] + "," + testing[index][3] + "," + testing[index][4]);
                index++;
            }
        }
    }
}
