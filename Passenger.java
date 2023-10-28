package ccp_tp061214;

public class Passenger extends Thread {
    int id;
    Plane plane;

    public Passenger(int id, Plane plane) {
        this.id = id;
        this.plane = plane;
    }
    
    public void run(){
        if (plane.getStatus().equals("Docked")){
            disembark();
        } else {
            embark();
        }
    }
    
    synchronized public void embark(){
        if (id % 10 == 0) {
            System.out.println("----------- Passengers ---------------\n" + "Plane " + plane.getId() + ": 10 passengers embarked\n--------------------------------------");
        }
        Statistics.passengersEmbarked.getAndIncrement();
    }
    
    synchronized public void disembark(){
        // Print passengers disembarking 10 by 10 (So CLI Looks Cleaner).
        if (id % 10 == 0) {
            System.out.println("----------- Passengers ---------------\n" + "Plane " + plane.getId() + ": 10 passengers disembarked\n--------------------------------------");
        }
        Statistics.passengersDisembarked.getAndIncrement();
    }
}
