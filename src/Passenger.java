public abstract class Passenger {

    protected String name;
    protected int id;
    protected double weight;
    protected int age;

    protected Passenger(String name, int id, double weight, int age) {
        this.name = name;
        this.id = id;
        this.weight = weight;
        this.age = age;
    }

    protected Passenger(){
        this.name = "Anonymous  guest";
        this.id = 11111;
        this.weight = 70;
        this.age = 25;
    }
}