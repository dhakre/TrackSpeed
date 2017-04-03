package dhakre.uha.fr.trackspeed;

/**
 * Created by user on 01-Apr-17.
 */

public class Cars {
    private String name,brand,speedmax,currentSpeed;

    public Cars(String brand,String name,String speedmax,String currentSpeed)
    {
        this.setBrand(brand);
        this.setName(name);
        this.setSpeedmax(speedmax);
        this.setCurrentSpeed(currentSpeed);
    }

    public String getBrand() {
        return (brand);
    }

    public String getCurrentSpeed() {
        return currentSpeed;
    }

    public String getName() {
        return name;
    }

    public String getSpeedmax() {
        return speedmax;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentSpeed(String currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void setSpeedmax(String speedmax) {
        this.speedmax = speedmax;
    }
}
