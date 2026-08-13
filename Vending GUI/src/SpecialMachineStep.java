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

    public void addItem(MachineItem item) {
        this.items.add(item);
    }

    public ArrayList<MachineItem> getItems() {
        return items;
    }
}