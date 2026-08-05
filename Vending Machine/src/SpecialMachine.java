import java.util.ArrayList;
import java.util.Arrays;

public class SpecialMachine {

    private String name;
    private final ArrayList<MachineItem> itemsToCheckout = new ArrayList<>();
    private final ArrayList<SpecialMachineStep> steps = new ArrayList<>();
    CashRegister register = new CashRegister();

    public SpecialMachine(String name) {
        this.name = name;
        initializeSpecialMachineSteps();
    }

    public void DispenseFinalItem()
    {
        double totalPrice = 0;
        for (MachineItem items : itemsToCheckout)
        {
            totalPrice += items.getPrice();
        }
    }

    // Maintenance Portion
    public void CollectMoney()
    {
        register.doMoneyGotten();
    }

    /**
     * Initializes the 6 steps required for the Special Machine setup
     */
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
                    for (int j = 0; j < pizzaSizes.length; j++){
                        step.addItem(new MachineItem(pizzaSizes[j], basePrices[j], defaultStock));
                    }
                    break;

                case 1://step 2 cut options
                    String[] cutOptions = {"4 cuts", "6 cuts", "8 cuts", "Square Cuts"};
                    for (String cut :  cutOptions){
                        step.addItem(new MachineItem(cut, 0.0, defaultStock));
                    }
                    break;

                case 2://step 3 sauce options
                    String[] sauceOptions = {"No Sauce", "Marinara", "Garlic", "Pesto", "White"};
                    double[] baseSaucePrices = {5.0, 6.0, 7.0, 10.0, 13.0};
                    for (int j = 0; j < baseSaucePrices.length; j++){
                        step.addItem(new MachineItem(sauceOptions[j], baseSaucePrices[j], defaultStock));
                    }
                    break;

                case 3://step 4 cheese options
                    String[] cheeseOptions = {"No Cheese", "Include Cheese"};
                    for (String cheese :  cheeseOptions){
                        step.addItem(new MachineItem(cheese, 0.0, defaultStock));
                    }
                    break;

                case 4://step 5 toppings options
                    String[] toppings = {"Pepperoni", "Mushroom", "Pineapple", "Ham", "Beef mince", "Basil leaves", "Onion"};
                    double[] baseToppingsPrices = {10.0, 10.0, 10.0, 10.0, 20.0, 10.0, 10.0};
                    for (int j = 0; j < toppings.length; j++){
                        step.addItem(new MachineItem(toppings[j], baseToppingsPrices[j], defaultStock));
                    }
                    break;

                case 5://step 6 extras
                    String[] extrasOptions = {"Thin Crust", "Cheese Crust"};
                    double[] baseExtraPrices = {40.0, 60.0};
                    for (int j = 0; j < extrasOptions.length; j++){
                        step.addItem(new MachineItem(extrasOptions[j], baseExtraPrices[j], defaultStock));
                    }
                    break;
            }
            this.steps.add(step);
        }
    }

    public ArrayList<SpecialMachineStep> getSteps() {
        return steps;
    }
}
