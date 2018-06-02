package Intro;

public class Iris {

    //Constants
    public static final int DIM = 4;

    private double dim[];
    //private char[] name;

    public Iris() {}


    public void setDim(double[] dim) {
        this.dim = dim;
    }
/*
    public void setName(char[] name) {
        this.name = name;
    }

    public char[] getName() {
        return this.name;
    }
*/
    public double[] getDim() {
        return this.dim;
    }

}
