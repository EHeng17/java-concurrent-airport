package ccp_tp061214;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Statistics {
    static AtomicInteger planesServed = new AtomicInteger(0);
    static AtomicInteger passengersEmbarked = new AtomicInteger(0);
    static AtomicInteger passengersDisembarked = new AtomicInteger(0);
    static AtomicLong totalWaitingTime = new AtomicLong(0);
    
    private static long averageWaitingTime;
    
    public static void printStatistics(){
        System.out.println("\n\n---------- Statistics ----------");
        System.out.println("Planes Served: " + planesServed);
        System.out.println("Average Waiting Time for Planes: "+ calculateAverageWaitingTime() + "(ms)");
        System.out.println("Passengers Embarked: " + passengersEmbarked + " passengers.");
        System.out.println("Passengers Disembarked: " + passengersDisembarked + " passengers.");
        System.out.println("--------------------------------");
    }
    
    public static long calculateAverageWaitingTime() {
        long totalTimeLong = totalWaitingTime.get();
        int planesServedInt = planesServed.get();
        averageWaitingTime = totalTimeLong/planesServedInt;
        return averageWaitingTime;
    };
}
