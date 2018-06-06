/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass2.s_16058989.server;

/**
 *
 * @author Octet
 */
class Vector {
    public final int x;
    public final int y;
    
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /*
        ArrayList.contains invokes .equals
        Therefore, I must override equals.
    */
    @Override
    public boolean equals(Object o) {
        
        //Same object?
        if(this == o) {
            return true;
        }
        
        //Make sure the Object is an instance of Vector
        if(!(o instanceof Vector)) {
            return false;
        }
        
        //Are the coordinates the same for both objects?
        Vector other = (Vector) o;
        if(this.x == other.x && this.y == other.y) {
            return true;
        }
        
        return false;
    }
        
}