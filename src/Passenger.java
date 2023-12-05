public abstract class Passenger {

    private final String name;
    private final String id;
    private final double weight;
    private final int age;

    protected Passenger(String name, String id, double weight, int age) {
        this.name = name;
        this.id = id;
        this.weight = weight;
        this.age = age;
    }

    protected Passenger(){
        this.name = "Anonymous  guest";
        this.id = "11111";
        this.weight = 70;
        this.age = 25;
    }

    public abstract void hasArrived();

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public double getWeight() {
        return this.weight;
    }

    public int getAge() {
        return this.age;
    }
}