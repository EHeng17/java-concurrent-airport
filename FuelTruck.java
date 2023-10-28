package ccp_tp061214;

import java.util.concurrent.Semaphore;

public class FuelTruck {
    
    Semaphore semaphore = new Semaphore(1);

    public void refuelAircraft(Plane plane){
        try {
            semaphore.acquire();
            System.out.println( "------------- Fuel Truck -------------\n" + "Plane " + plane.getId() + " : Refuelling Start" + "\n--------------------------------------");
            System.out.println("Refuelling Plane " + plane.getId() + " (5 seconds)");
            Thread.sleep(5000);
            System.out.println("------------- Fuel Truck -------------\n" + "Plane " + plane.getId() + ": Refuelling Complete" + "\n--------------------------------------");
        } catch (InterruptedException e) {
            System.out.println("------------- Fuel Truck -------------\n" + "Plane " + plane.getId() + ": Refuelling Error." + "\n--------------------------------------\n");
        } finally {
            semaphore.release();
        }        
    }
}
