package spiderman;
import java.util.*;
public class Route {
    private String anomalyName;
    private String spiderName;
    private List<Integer> dimensions; 

    public Route(String anomalyName,String spiderName, List<Integer> dimensions){
        this.anomalyName = anomalyName;
        this.spiderName= spiderName;
        this.dimensions = dimensions;
    }
    public String getAnomalyName() {
        return anomalyName;
    }

    public void setAnomalyName() {
        this.anomalyName = anomalyName;
    }

    public String getSpiderName() {
        return spiderName;
    }
    public void setSpiderName() {
        this.spiderName = spiderName;
    }

    public List<Integer> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<Integer> dimensions) {
        this.dimensions = dimensions;
    }

    public String toString() {
        String r = " ";
        
        for(int dimension : dimensions) {
            r += dimension + " ";
        }
        r = r.trim();
        if(spiderName != null && anomalyName != null) {
            if(spiderName.equals(anomalyName)) {
                return anomalyName + " " + r + "\n";

            }
            else {
                return anomalyName + " " + spiderName + " " +  r + "\n";
            }
            
        }
        return anomalyName + " " + r + "\n";
        
        
        
        
    }
    
    
}
