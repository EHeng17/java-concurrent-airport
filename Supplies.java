package ccp_tp061214;

public class Supplies extends Thread{
    
    Plane plane;

    public Supplies(Plane plane) {
        this.plane = plane;
    }
    
    synchronized public void run() {
        try {
            System.out.println("\n-------------- Supplies -----------------\nPlane " 
                    + plane.getId() + ": Refilling food and drinks for Plane " + plane.getId() 
                    + ".(5 seconds)\n--------------------------------------"); 
            
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            System.out.println("Plane " + plane.getId() + ": Supply Error");
        }
    }
}
