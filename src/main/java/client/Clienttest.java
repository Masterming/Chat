/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.main.java.client;

/**
 *
 * @author blech
 */
public class Clienttest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Client c = new Client("127.0.0.1", 4999);
        c.run();
    }
    
}
