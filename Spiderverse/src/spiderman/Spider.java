package spiderman;

public class Spider {
    private int currentD;
    private String name; 
    private int sign;

    public Spider(int currentD, String name, int sign) {
        this.currentD = currentD;
        this.name = name;
        this.sign = sign;
    }
    public int getCurrentDimension() {
        return currentD;
    }
    public void setCurrentDimension(int currentD) {
        this.currentD = currentD;
    }

    public String getName() {
        return name; 
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSign() {
        return sign;
    }
    public void setSign(int  sign) {
        this.sign = sign; 
    }
    
}
