package Intro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Loader {

    /**
     * Loads a specified data file, and returns an ArrayList of all the lines in this file.
     * @param filename the filename to load.
     * @return All the lines of the specified file, as an ArrayList.
     */
    protected static void load(String[][] training, String[][] testing, String filename) throws IOException {

        ClassLoader classLoader = Loader.class.getClassLoader();
        InputStream input = classLoader.getResourceAsStream(filename);
        BufferedReader r = new BufferedReader(new InputStreamReader(input));

        int max_data = training.length + testing.length;

        //Generate 15 random numbers between 0 and max_data
        List<Integer> randoms = new ArrayList<>();
        for(int n = 0; n < testing.length; n++) {
            Random randomNum = new Random();
            randoms.add(randomNum.nextInt(max_data));
        }
        //Sort these random numbers in ascending order
        Collections.sort(randoms);


        int i = 0;
        int train = 0;
        int test = 0;
        String tmp;


        while ((tmp = r.readLine()) != null) {
            if (i >= max_data) {
                break;
            }
            if(!randoms.isEmpty()) {
                if (randoms.get(0) == i) {
                    randoms.remove(0);
                    testing[test] = tmp.split(",", 5);
                    test++;
                    i++;
                    continue;
                }
            }
            training[train] = tmp.split(",");
            train++;
            i++;
        }
    }
}
