package spiderman;

public class Anomaly {
    private String name;
    private int time;

    public Anomaly(String name, int time) {
        this.name = name;
        this.time = time;
        
    }

    public String getName() {
        return name;

    }
    public int getTime() {
        return time;
    }
    
}
