package ccp_tp061214;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PlaneGenerator extends Thread {
    
    ATC atc;
    public boolean maxPlanes = false;
    int i = 1;
    
    Random random = new Random();

    public PlaneGenerator(ATC atc) {
        this.atc = atc;
    }
    
    public void run(){
        while (!maxPlanes) {
            
            Plane plane;
            
            // Generate a random passenger number between 10 to 30 (Assumption: 1 plane can fit at most 30 passengers)
            int randomNumberPassenger = random.nextInt((30 - 10) + 1) + 10;
            
            if (i != 5) {
                // Create plane object
                plane = new Plane(i, false, atc, randomNumberPassenger);                
            } else {
                // Simulating Emergency Landing
                plane = new Plane(i, true, atc, randomNumberPassenger);
            }

            
            // Make it a thread and run it.
            Thread threadPlane = new Thread(plane);
            threadPlane.setName("Thread-Plane-" + plane.getId()); // Setting Thread Name
            threadPlane.start();
            
            // If 6 planes generated, can stop generating
            if (i == 6){
                maxPlanes = true;
            } else {
                i++ ;
            }
            
            try {
                // 2 to 4 seconds
                int delay = 4 + (int)(Math.random() * (4 - 2 + 1));
                TimeUnit.SECONDS.sleep(delay);
            }
            catch(InterruptedException ex) {
                ex.printStackTrace();
            } 
        }

    }
}
