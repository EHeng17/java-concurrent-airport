package ccp_tp061214;

import java.util.Random;

public class Plane implements Runnable{
    
    private int id;
    private boolean urgent;
    private String status;
    private long arrivalTime;
    private ATC atc;
    
    private int passengers;

    public Plane(int id, boolean urgent, ATC atc, int passengers) {
        this.id = id;
        this.urgent = urgent;
        this.atc = atc;
        this.passengers = passengers;
    }

    public ATC getAtc() {
        return atc;
    }

    public void setAtc(ATC atc) {
        this.atc = atc;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
    
    
    public void run(){
        try {
            addToRunway();
            
            while (this.getStatus().equals("Runway")) {
                docking();
            };
            
            while (this.getStatus().equals("Docked")){
                airportOperations();
            };
            
            while (this.getStatus().equals("Depart")) {
                depart();
            };
            
            if (atc.currentCapacity.get() == 0 && atc.runwaySemaphore.availablePermits() == 1 && atc.gateSemaphore.availablePermits() == 3) {
                System.out.println("\n--------------- Finished ---------------\n");
                Statistics.printStatistics();
            }
            
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    private void addToRunway() throws InterruptedException{
        System.out.println("ATC: Plane " + this.getId() + " is requesting to land.");
        Thread.sleep(1000);
        
        synchronized (atc) {
            atc.addPlane(this);
            
            // Messages for emergency
            if (this.isUrgent()) {
                System.out.println("ATC: Plane " + this.getId() + " has successfully cut queue.");
            } else {
                System.out.println("ATC: Plane " + this.getId() + " is in queue successfully !!!");  
            }
            
            atc.requestToLand();
        }
    }
    
    private void docking() throws InterruptedException {
        
        // Acquire Gate
        System.out.println("Plane " + this.getId() + " : Aquiring Gate...");
        atc.gateSemaphore.acquire();
        Thread.sleep(2000);
        System.out.println("Plane " + this.getId() + " : Acquired Gate Successfully!!!");
        Thread.sleep(1000);
        
        System.out.println("Plane " + this.getId() + " : Docking...");
        Thread.sleep(2000);

        System.out.println("Plane " + this.getId() + " Successfully docked !!!");
        Thread.sleep(2000);
        
        this.setStatus("Docked");
        
        atc.runwaySemaphore.release();
    }  
    
    private void airportOperations() throws InterruptedException {
        // Disembark
        for (int i = 1; i <= this.getPassengers() + 1; i ++) {
            Passenger passenger = new Passenger(i, this);
            passenger.start();
            passenger.join();
        }

            System.out.println("Plane " + this.getId() + ": All " + this.getPassengers() + " passemgers disembarked !!!");

            // Cleaning
            Clean clean = new Clean(this);
            clean.start();
            clean.join();

            // Refill Supplies
            Supplies supply = new Supplies(this);
            supply.start();
            supply.join();

            // Refill Petrol (Exclusive)
            System.out.println("Plane " + this.getId() + " : I need the fuel truck!");
            this.getAtc().deployFuelTruck(this);

            this.setStatus("Embark");

            // Get a random number to put into plane
            Random random = new Random();
            int randomNumberPassenger = random.nextInt((30 - 10) + 1) + 10;

            // Embark
            for (int i = 1; i <= randomNumberPassenger + 1; i ++) {
                Passenger passenger = new Passenger(i, this);
                passenger.start();
                passenger.join();
            }

            System.out.println("Plane " + this.getId() + ": All " + randomNumberPassenger + " passengers embarked !!!");
            
            this.setStatus("Depart");
    }

    private void depart() throws InterruptedException {
        atc.requestToTakeoff(this);
        Statistics.planesServed.getAndIncrement();
        this.setStatus("Complete");
    }
}
