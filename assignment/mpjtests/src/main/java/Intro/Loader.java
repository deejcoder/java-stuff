package Intro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import Intro.Iris;

public class Loader {
    public static HashMap<double[], String> load(String filename) {

        ClassLoader classLoader = Loader.class.getClassLoader();
        InputStream input = classLoader.getResourceAsStream(filename);
        BufferedReader r = new BufferedReader(new InputStreamReader(input));

        HashMap<double[], String> iris = new HashMap<double[], String>();

        String line;

        try {

            while ((line = r.readLine()) != null) {


                String[] vectorString = line.split(",");

                double vector[] = new double[Iris.DIM];
                String name = "";

                for (int i = 0; i < vectorString.length; i++) {
                    if(i == vectorString.length - 1) {

                        name = vectorString[i];

                        continue;
                    }

                    if(i > Iris.DIM) {
                        System.out.println("Fatal error: there was a set of dimensions greater than size: " + Iris.DIM);
                        System.exit(0);
                    }
                    vector[i] = Double.parseDouble(vectorString[i]);
                }

                iris.put(vector, name);

            }
        }
        catch(IOException | NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Error: Unable to parse input file, a value cannot be converted to a double.");
            System.exit(0);
        }
        return iris;
    }
}
