/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.main.java.server;

/**
 *
 * @author blech
 */
public class Servertest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Server s = new Server(4999);
        s.run();    
    }
    
}
