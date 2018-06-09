package Intro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Iris {

    //Constants
    public static final int DIM = 4;

    private double dim[];
    private String name;

    public Iris() {}


    public void setDim(double[] dim) {
        this.dim = dim;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public double[] getDim() {
        return this.dim;
    }

}
