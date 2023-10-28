package ccp_tp061214;

public class Airport {
    public static void main(String[] args) {
        
        // ATC Class
        ATC atc = new ATC();
        atc.start();
        
        // Plane Generator 
        PlaneGenerator pg = new PlaneGenerator(atc);
        pg.start();
    }
}
