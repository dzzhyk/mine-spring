package project.test.entity;


public class Car {

    private String brand;
    private int cost;

    public Car(String brand, int cost) {
        this.brand = brand;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", cost=" + cost +
                '}';
    }
}
