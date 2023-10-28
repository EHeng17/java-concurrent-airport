package ccp_tp061214;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ATC extends Thread{
            
    Semaphore runwaySemaphore = new Semaphore(1);
    Semaphore gateSemaphore = new Semaphore(3);
    
    Deque <Plane> waitingPlanes;
    FuelTruck fuelTruck = new FuelTruck();
    
    AtomicInteger currentCapacity = new AtomicInteger(0);
    
    public ATC(){
        waitingPlanes = new LinkedList<>();
    }
    
    public void run() {
        System.out.println("ATC: Online, ready to start !");
    }
    
    public void requestToLand() throws InterruptedException{
        
        // Check for available gates
        synchronized(waitingPlanes) {
            if (gateSemaphore.availablePermits() == 0) {
                System.out.println("ATC: Gates are all occupied, Plane will wait in queue");
                waitingPlanes.wait();
            }            
        }
        
        // Remove the first plane thread in queue
        Plane plane = waitingPlanes.poll();
        System.out.println("ATC: Plane " + plane.getId() + " is extracted from Queue !");
        
        // Calculate waiting time (Arrival Time - Current Time)
        long currentTime = System.currentTimeMillis();
        Statistics.totalWaitingTime.getAndAdd(currentTime - plane.getArrivalTime());

        // Acquiring Runway
        System.out.println("ATC: Plane " + plane.getId() + " aquirring Runway...");
        runwaySemaphore.acquire();
        Thread.sleep(2000);
        System.out.println("ATC: Plane " + plane.getId() + " acquired Runway !!!");
        
        plane.setStatus("Runway");
        
        currentCapacity.getAndIncrement();
    }
    
    public void requestToTakeoff(Plane plane) throws InterruptedException {
    
        runwaySemaphore.acquire();
        System.out.println("Plane " + plane.getId() + " : Acquired the runway !!!");
        
        System.out.println("Plane " + plane.getId() + " : Undocking... ");
        Thread.sleep(2000);
        
        System.out.println("Plane " + plane.getId() + " : Taking off... ");
        Thread.sleep(2000);

        System.out.println("Plane " + plane.getId() + " : Has successfully departed ");
        runwaySemaphore.release();
        gateSemaphore.release(); // Release gate semaphore
        
        currentCapacity.getAndDecrement();
        
        synchronized(waitingPlanes) {
            waitingPlanes.notify();
        }
        
    }
    
    public synchronized void addPlane(Plane plane) throws InterruptedException {
        
        if (plane.isUrgent()) {
            System.out.println("\n ----------- WARNING --------\nATC: PLANE " + plane.getId() + " REQUESTING EMERGENCY LANDING !!!\n----------------------------");
            waitingPlanes.offerFirst(plane);
            
        } else {
            waitingPlanes.offer(plane);
        }
        
        plane.setStatus("Waiting");
        
        // Use current time as arrival time
        plane.setArrivalTime(System.currentTimeMillis());
    }
    
    // ATC will allocate fuel truck to planes
    public void deployFuelTruck(Plane plane) {
        fuelTruck.refuelAircraft(plane);
    }
}
