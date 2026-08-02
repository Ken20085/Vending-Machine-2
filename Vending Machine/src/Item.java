public class Item {
    private final String name;
    private double calories;

    public Item(String name, double calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() {
        return this.name;
    }
    public double getCalories() {
        return this.calories;
    }
    public void setCalories(double calories) {
        this.calories = calories;
    }
}
