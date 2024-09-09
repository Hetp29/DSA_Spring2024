package spiderman;
import java.util.*;

public class Dimension {
    private int num;
    private int canonEvents; 
    private int weight; 
    private List<Integer> connectedDimensions;

    public Dimension(int num, int canonEvents, int weight) 
    {
        this.num  = num;
        this.canonEvents = canonEvents;
        this.weight = weight;
        this.connectedDimensions = new ArrayList<>();
        
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getCanonEvents() {
        return  canonEvents;
    }
    public void setCanonEvents(int canonEvents) {
        this.canonEvents= canonEvents;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public List<Integer> getConnectedDimensions() {
        return connectedDimensions;

    }
    public void setConnectedDimensions(List<Integer> connectedDimensions) {
        this.connectedDimensions = connectedDimensions;
    }
    
}
