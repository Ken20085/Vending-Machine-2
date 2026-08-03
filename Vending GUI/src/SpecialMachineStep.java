import java.util.ArrayList;

public class SpecialMachineStep {

    private String name;
    private final ArrayList<MachineItem> items = new ArrayList<>();

    public SpecialMachineStep(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<MachineItem> getItems() {
        return items;
    }

    public void addItem(MachineItem item){
        items.add(item);
    }

    public void removeItem(MachineItem item){
        items.remove(item);
    }

    public double getStepCalories(){
        double calories = 0;
        for (MachineItem item : items){
            calories += item.getCalories();
        }
        return calories;
    }
}
