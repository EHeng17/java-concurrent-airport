package ccp_tp061214;

public class Clean extends Thread{
    Plane plane;

    public Clean(Plane plane) {
        this.plane = plane;
    }
    
    synchronized public void run() {
        try {
            System.out.println("-------------- Clean -----------------\nPlane " 
                    + plane.getId() + ": Cleaning Exterior and Interior of Plane " 
                    + plane.getId() + ".(5 seconds)\n--------------------------------------");                           
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            System.out.println("Plane " + plane.getId() + ": Cleaning Error");
        }
    }    
}
