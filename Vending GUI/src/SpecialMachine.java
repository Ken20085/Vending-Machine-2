import java.util.ArrayList;

public class SpecialMachine extends RegularVM{

    private String name;
    private final ArrayList<MachineItem> itemsToCheckout = new ArrayList<>();
    private final ArrayList<SpecialMachineStep> steps = new ArrayList<>();
    //CashRegister register = new CashRegister();

    public SpecialMachine(String name) {
        super(name);
    }

    public void DispenseFinalItem()
    {
        double totalPrice = 0;
        for (MachineItem items : itemsToCheckout)
        {
            totalPrice += items.getPrice();
        }
    }

    /**
     * Generates a step by step log of preparation actions for a special order, iterates through machine steps and
     * matches selected itesm to produce specific preparation descriptions
     * @param selectedItems list of ingredients/toppings chosen by the user
     * @return array list of string containing the formatted sequential prepartion logs
     */
    public ArrayList<String> getPreparationAction(ArrayList<MachineItem> selectedItems) {
        ArrayList<String> logs = new ArrayList<>();
        logs.add("Preparing custom pizza...");

        for (SpecialMachineStep step : steps){
            logs.add("---" + step.getName() + "---");

            for (MachineItem item : selectedItems){
                if (step.getItems().contains(item)){
                    String itemLower = item.getName().toLowerCase();

                    if (itemLower.contains("dough")){
                        logs.add("   --> kneading and shaping the " + item.getName() + "...");
                    } else if (itemLower.contains("sauce") || itemLower.contains("marinara") || itemLower.contains("pesto")
                            || itemLower.contains("garlic")) {
                        logs.add("   --> laddling and spreading " + item.getName() + " evenly over dough");
                    } else if (itemLower.contains("cheese")) {
                        logs.add("   --> Shredding and layering " + item.getName());
                    } else {
                        logs.add("   --> Adding " + item.getName() + "...");
                    }
                }
            }
        }

        logs.add("--- Homestretch! Almost done! ---");
        logs.add("transferring assembled pizza into oven...");
        logs.add("Baking at high temperatures...");
        logs.add("Pizza is now ready! Enjoy your meal!");

        return logs;
    }

    // Maintenance Portion
    public void CollectMoney()
    {
        register.doMoneyGotten();
    }

    void addStep(SpecialMachineStep step){
        steps.add(step);
    }

    /**
     * retrieves list of preparation steps configured for this vending machine
     * @return array list of strng containing machine steps
     */
    public ArrayList<SpecialMachineStep> getSteps(){
        return steps;
    }

    /**
     * calculates the total price of the custom order based on the selected items.
     * @param selectedItems list of ingredients chosen for the order
     * @return sum of all items in selectedItems
     */
    public double calculateTotalPrice(ArrayList<MachineItem> selectedItems){
        double total = 0;
        for (MachineItem item : selectedItems){
            total += item.getPrice();
        }
        return total;
    }

    /**
     * Processes payment, verifies stock, dispenses change, and simulates product preparation.
     * Also ensures: payment is sufficient, exact change can be given, and items are in stock before
     * deducting from inventory and cash register balances.
     * @param selectedItems list of ingredients chosen by the user
     * @param payment total amount of money inserted by the user
     * @return true if the transaction and preparation is successfull; false otherwise
     */
    public boolean prepareAndDispense(ArrayList<MachineItem> selectedItems, double payment){
        double totalPrice = calculateTotalPrice(selectedItems);

        if (payment < totalPrice){
            System.out.println("Insufficient Payment!");
            return false;
        }

        double change = payment - totalPrice;

        ArrayList<Integer> changeDenominations = getRegister().calculateChange((int) change);
        if (!getRegister().changeIsPossible(change) || changeDenominations == null){
            System.out.println("Cannot dispense exact change!");
            return false;
        }

        for (MachineItem item : selectedItems){
            if (item.getStock() <= 0){
                System.out.println("Out of " + item.getName() + " in inventory!");
                return false;
            }
        }

        for (MachineItem item : selectedItems){
            item.transact();
        }

        ArrayList<String> prepSteps = getPreparationAction(selectedItems);
        for (String stepMsg : prepSteps){
            System.out.println(stepMsg);
        }

        getRegister().deductCash(changeDenominations);

        return true;
    }

//    @Override
//    public ArrayList<MachineItem> getItemsToDisplay()
//    {
//
//    }
}
