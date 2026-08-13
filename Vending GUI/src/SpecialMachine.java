import java.util.ArrayList;

public class SpecialMachine extends RegularVM{

    private String name;
    private final ArrayList<MachineItem> itemsToCheckout = new ArrayList<>();
    private final ArrayList<MachineItem> stockToCheckList = new ArrayList<>();
    private final ArrayList<MachineItem> allItems = new ArrayList<>();
    private final ArrayList<SpecialMachineStep> steps = new ArrayList<>();
    CashRegister register = new CashRegister();

    public SpecialMachine(String name) {
        super(name);
        initializeSpecialMachineSteps();

        for (SpecialMachineStep step : steps) {
            System.out.println(step.getName());

            for (MachineItem item : step.getItems())
            {
                System.out.println(item.getName() + " - " + item.getPrice() + " - " + item.getCalories());
            }
        }

        this.history = new History(stockToCheckList.size());
        this.history.restockPeriod(stockToCheckList);
    }

    public void DispenseFinalItem()
    {
        double totalPrice = 0;
        for (MachineItem items : itemsToCheckout)
        {
            totalPrice += items.getPrice();
        }
    }

//    /**
//     * Generates a step by step log of preparation actions for a special order, iterates through machine steps and
//     * matches selected itesm to produce specific preparation descriptions
//     * @param selectedItems list of ingredients/toppings chosen by the user
//     * @return array list of string containing the formatted sequential prepartion logs
//     */
//    public ArrayList<String> getPreparationAction(ArrayList<MachineItem> selectedItems) {
//        ArrayList<String> logs = new ArrayList<>();
//        logs.add("Preparing custom pizza...");
//
//        for (SpecialMachineStep step : steps){
//            logs.add("---" + step.getName() + "---");
//
//            for (MachineItem item : selectedItems){
//                if (step.getItems().contains(item)){
//                    String itemLower = item.getName().toLowerCase();
//
//                    if (itemLower.contains("dough")){
//                        logs.add("   --> kneading and shaping the " + item.getName() + "...");
//                    } else if (itemLower.contains("sauce") || itemLower.contains("marinara") || itemLower.contains("pesto")
//                            || itemLower.contains("garlic")) {
//                        logs.add("   --> laddling and spreading " + item.getName() + " evenly over dough");
//                    } else if (itemLower.contains("cheese")) {
//                        logs.add("   --> Shredding and layering " + item.getName());
//                    } else {
//                        logs.add("   --> Adding " + item.getName() + "...");
//                    }
//                }
//            }
//        }
//
//        logs.add("--- Homestretch! Almost done! ---");
//        logs.add("transferring assembled pizza into oven...");
//        logs.add("Baking at high temperatures...");
//        logs.add("Pizza is now ready! Enjoy your meal!");
//
//        return logs;
//    }

    // Maintenance Portion
    public void CollectMoney()
    {
        register.doMoneyGotten();
    }

    public ArrayList<MachineItem> getStepItems(int index)
    {
        return steps.get(index).getItems();
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


    public void initializeSpecialMachineSteps(){
        //step names
        String[] stepNames = {
                "Pizza Sizes",
                "Cut Options",
                "Sauce Options",
                "Cheese Options",
                "Toppings Options",
                "Extras"
        };

        int defaultStock = 10;

        for (int i = 0; i < stepNames.length; i++){
            SpecialMachineStep step = new SpecialMachineStep(stepNames[i]);

            switch (i){
                case 0://step 1 pizza size
                    String[] pizzaSizes = {"Personal", "Small", "Medium", "Large", "Extra Large"};
                    double[] basePrices = {80.0, 100.0, 130.0, 190.0, 250.0};
                    double[] pizzaCalories = {250, 380, 550, 800, 1150};
                    for (int j = 0; j < pizzaSizes.length; j++){
                        step.addItem(new MachineItem(pizzaSizes[j], basePrices[j], defaultStock, pizzaCalories[j], false));
                    }
                    break;

                case 1://step 2 cut options
                    String[] cutOptions = {"4 cuts", "6 cuts", "8 cuts", "Square Cuts"};
                    for (String cut :  cutOptions){
                        step.addItem(new MachineItem(cut, 0.0, defaultStock, 0, true));
                    }
                    break;

                case 2://step 3 sauce options
                    String[] sauceOptions = {"No Sauce", "Marinara", "Garlic", "Pesto", "White"};
                    double[] baseSaucePrices = {5.0, 6.0, 7.0, 10.0, 13.0};
                    double[] baseSauceCalories = {0, 25, 120, 165, 180};

                    for (int j = 0; j < baseSaucePrices.length; j++){
                        boolean isAtStart = j == 0;
                        step.addItem(new MachineItem(sauceOptions[j], baseSaucePrices[j], defaultStock,  baseSauceCalories[j], isAtStart));
                    }
                    break;

                case 3://step 4 cheese options
                    String[] cheeseOptions = {"No Cheese", "Include Cheese"};
                    double[] baseCheeseCalories = {0, 50};

                    for (int j = 0; j < cheeseOptions.length; j++) {
                        boolean isAtStart = j == 0;
                        step.addItem(new MachineItem(cheeseOptions[j], 0.0, defaultStock, baseCheeseCalories[j], isAtStart));
                    }
                    break;

                case 4://step 5 toppings options
                    String[] toppings = {"Pepperoni", "Mushroom", "Pineapple", "Ham", "Beef mince", "Basil leaves", "Onion"};
                    double[] baseToppingsPrices = {10.0, 10.0, 10.0, 10.0, 20.0, 10.0, 10.0};
                    double[] baseToppingsCalories = {40, 5, 25, 20, 60, 1, 5};

                    for (int j = 0; j < toppings.length; j++){
                        step.addItem(new MachineItem(toppings[j], baseToppingsPrices[j], defaultStock,  baseToppingsCalories[j],false));
                    }
                    break;

                case 5://step 6 extras
                    String[] extrasOptions = {"Thin Crust", "Cheese Crust"};
                    double[] baseExtraPrices = {40.0, 60.0};
                    double[] baseExtraCalories = {0, 120};

                    for (int j = 0; j < extrasOptions.length; j++){
                        boolean isAtStart = j == 0;
                        step.addItem(new MachineItem(extrasOptions[j], baseExtraPrices[j], defaultStock, baseExtraCalories[j], isAtStart));
                    }
                    break;
            }
            this.steps.add(step);
        }

        this.getAllItems();

        // Stock Checklist
        // Pizza Sizes
        stockToCheckList.addAll(getStepItems(0));

        // Sauces
        for (MachineItem item : getStepItems(2))
        {
            if(!item.getName().equals("No Sauce"))
            {
                stockToCheckList.add(item);
            }
        }

        // Toppings
        stockToCheckList.addAll(getStepItems(4));

        // Cheese Crust
        stockToCheckList.add(getStepItems(5).getLast());
    }

    /**
     * Processes payment, verifies stock, dispenses change, and simulates product preparation.
     * Also ensures: payment is sufficient, exact change can be given, and items are in stock before
     * deducting from inventory and cash register balances.
     */
    public void prepareAndDispense(){

        boolean invalid = false;
        for (MachineItem item : itemsToCheckout){
            if (item.getStock() <= 0){
                System.out.println("Out of " + item.getName() + " in inventory!");
                invalid = true;
            }
        }

        if (invalid) return;

        for (MachineItem item : itemsToCheckout){
            item.transact();

            int slotIndex = stockToCheckList.indexOf(item);
            if (slotIndex != -1) {
                // Recording 1 quantity sold per item checkout instance
                history.recordSale(slotIndex, item.getName(), item.getPrice(), 1);
            }
        }

        // getRegister().deductCash(changeDenominations);

        history.addLogMessage("Custom pizza successfully assembled and baked.");
    }

    @Override
    public ArrayList<MachineItem> getItems()
    {
        return stockToCheckList;
    }

    @Override
    public MachineItem getItem(String name)
    {
        MachineItem itemToGet = null;

        for (MachineItem item : stockToCheckList)
        {
            if (item.getName().equals(name))
            {
                itemToGet = item;
            }
        }

        return itemToGet;
    }

    @Override
    public void RestockItem(String itemName, int quantity)
    {
        boolean itemRestocked;

        itemRestocked = getItem(itemName).restock(quantity);
        if (itemRestocked)
        {
            history.restockPeriod(getItems());
            history.addLogMessage("Restocked " + itemName + " by " + quantity + " units.");
        }
    }

    private void getAllItems()
    {
        for (SpecialMachineStep step : steps)
        {
            allItems.addAll(step.getItems());
        }
    }

    public void addToCart(String itemName)
    {
        for (MachineItem item : allItems)
        {
            if (item.getName().equals(itemName))
            {
                itemsToCheckout.add(item);
            }
        }
    }

    public ArrayList<String> getActions() {
        ArrayList<String> logs = new ArrayList<>();
        logs.add("Preparing custom pizza...");

        double totalCalories = 0;
        ArrayList<String> summaryLines = new ArrayList<>();
        summaryLines.add("");
        summaryLines.add("--- Order Summary ---");

        // Use a tracked list or Set to ensure we don't process duplicate object entries
        java.util.Set<MachineItem> processedItems = new java.util.HashSet<>();

        for (SpecialMachineStep step : steps) {
            for (MachineItem item : itemsToCheckout) {
                // Check if this step contains the item, it's not a "no" option, and we haven't already processed it
                if (step.getItems().contains(item) && !processedItems.contains(item)) {
                    if (!item.getName().toLowerCase().startsWith("no ")) {
                        processedItems.add(item); // Mark as processed so it won't repeat

                        String stepName = step.getName();
                        String itemNameClean = item.getName().replaceAll("(?i)include\\s*", "").trim();
                        double calories = item.getCalories();
                        totalCalories += calories;

                        // Animation step log
                        switch (stepName) {
                            case "Pizza Sizes" -> logs.add("Preparing a " + itemNameClean + " pizza size...");
                            case "Cut Options" -> logs.add("Slicing into " + itemNameClean + "...");
                            case "Sauce Options" -> logs.add("Applying " + itemNameClean + " sauce...");
                            case "Cheese Options" -> logs.add("Layering " + itemNameClean + "...");
                            case "Toppings Options" -> logs.add("Adding topping: " + itemNameClean + "...");
                            default -> logs.add("Including extra: " + itemNameClean + "...");
                        }

                        // Collect for the final summary screen
                        summaryLines.add(String.format("• %s (%.0f kcal)", itemNameClean, calories));
                    }
                }
            }
        }

        // Finalize baking animation steps
        logs.add("Transferring assembled pizza into oven...");
        logs.add("Baking at high temperatures...");
        logs.add("Pizza is now ready! Enjoy your meal!");

        // Append the summary and total calories at the end of the sequence
        logs.addAll(summaryLines);
        logs.add(String.format("Total Calories: %.0f kcal", totalCalories));
        logs.add("Thank you for your order!");

        itemsToCheckout.clear();

        return logs;
    }
}
