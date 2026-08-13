import java.util.ArrayList;
import java.util.Scanner;

public class VendingMachineFactory {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args)
    {
        // Variables
        boolean isDone = false;
        boolean isTesting;
        String choice;
        RegularVM machine = null;

        // Welcome Message
        System.out.println("Hello World!");

        // While Loop for program execution
        while (!isDone)
        {
            // The usual print stuff
            System.out.println("********************************************");
            System.out.println("Welcome to the Pizza Vending Machine Factory");
            if (machine != null) { System.out.println("Current Machine: " + machine.getName()); }
            System.out.println("********************************************");
            System.out.println("1. Create a Vending Machine");
            System.out.println("2. Test a Vending Machine");
            System.out.println("3. Exit Program");
            System.out.println("********************************************");

            System.out.print("Enter your choice (1-3): ");
            choice = scanner.nextLine();

            // Switch statement for everything
            switch(choice)
            {
                // region case 1 - Create a Vending Machine
                case "1":
                    System.out.println();
                    System.out.println("-------------------------------------------");
                    System.out.println("What vending machine do you want to create?");
                    System.out.println("1. Regular Vending Machine");
                    System.out.println("2. Special Vending Machine");
                    System.out.println("-------------------------------------------");
                    System.out.print("Enter your choice (1-2): ");
                    choice = scanner.nextLine();

                    switch(choice)
                    {
                        case "1":
                            if (machine != null)
                            {
                                System.out.println("Deleted Vending Machine with name: " + machine.getName());
                            }
                            System.out.print("Name your Vending Machine: ");
                            String name = scanner.nextLine();
                            machine = new RegularVM(name);
                            System.out.println("Created Vending Machine with name: " + name);
                            break;

                        case "2":
                            if (machine != null){
                                System.out.println("Deleted Vending Machine with name: + machine.getName()");
                            }
                            System.out.println("Name your Special Vending Machine: ");
                            String specialName = scanner.nextLine();

                            SpecialMachine specialVM = new SpecialMachine(specialName);

                            setupDefaultSteps(specialVM);
                            System.out.println("Created Special Vending Machine with name: " + specialName);
                            break;

                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }

                    System.out.println();
                    break;
                    // endregion

                // region case 2 - Test a Vending Machine
                case "2":
                    if (machine != null)
                    {
                        System.out.println("Going to testing menu...");
                        isTesting = true;

                        while (isTesting)
                        {
                            System.out.println();
                            System.out.println("-------------------------------------------");
                            System.out.println("What do you want to test?");
                            System.out.println("1. Vending Features of " + machine.getName());
                            System.out.println("2. Maintenance Features of " + machine.getName());
                            System.out.println("3. Exit Testing");
                            System.out.println("-------------------------------------------");
                            System.out.print("Enter your choice (1-3): ");
                            choice = scanner.nextLine();

                            switch(choice)
                            {
                                case "1":
                                    System.out.println();
                                    RunVendingMachine(machine);
                                    break;
                                case "2":
                                    ConductMaintenance(machine);
                                    break;
                                case "3":
                                    System.out.println("Returning to main menu...");
                                    isTesting = false;
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                                    break;
                            }
                        }
                    }
                    else
                    {
                        System.out.println("Machine to test does not exist.");
                    }

                    System.out.println();
                    break;
                    // endregion

                // region case 3 - End Program
                case "3":
                    isDone = true;
                    break;
                    // endregion

                default:
                    System.out.println("Invalid choice");
                    System.out.println();
                    break;
            }
        }

        scanner.close();
        System.out.println("Have a nice day!");
    }

    /**
     * Tests and runs the Vending Functions of RegularVM machine.
     * This method simulates how a vending machine would normally work.
     *
     * @param machine the machine to be run vending functions on
     */
    static void RunVendingMachine(RegularVM machine)
    {
        // Variables
        String choice = "-1";
        int choiceToInput = -1;
        double inputtedMoney;
        double lowestPrice = Double.POSITIVE_INFINITY;
        String input;

        // if special vending machine
//        if (machine instanceof SpecialMachine){
//            RunSpecialVendingMachine((SpecialMachine) machine);
//            return;
//        }

        // highest price set up
        for (MachineItem item : machine.getItems())
        {
            if (item.getPrice() < lowestPrice)
            {
                lowestPrice = item.getPrice();
            }
        }

        // Money Input
        System.out.println("Running Vending Machine " + machine.getName() + "...");
        machine.displayVendingMachine();
        Helper.MoneyInstructions();
        System.out.print("Input: ");
        input = scanner.nextLine();

        inputtedMoney = Helper.parseMoney(input, machine, 0);
        System.out.println("Money: " + inputtedMoney);
        System.out.println();

        if (inputtedMoney < lowestPrice)
        {
            System.out.println("Inputted money is too low for item with lowest price");
            System.out.printf("Here's your Php %.2f back\n", inputtedMoney);
            return;
        }

        // Item Index Input
        machine.getRegister().displayChangePool();
        System.out.println("0 to cancel current transaction");
        while (choice.equals("-1"))
        {
            System.out.print("Index of item order: Item ");
            choice = scanner.nextLine();
            choiceToInput = Integer.parseInt(choice);
            if (choiceToInput > machine.getItems().size() || choiceToInput <= -1)
            {
                choiceToInput = -1;
                System.out.println("Invalid choice.");
            }
        }

        if (choice.equals("0"))
        {
            System.out.println();
            System.out.println("Transaction cancelled.");
            System.out.printf("Here's your Php %.2f back\n", inputtedMoney);
            Helper.undoParse(input, machine, 0);
            return;
        }

        // Transaction stuff
        machine.dispenseItem(choiceToInput - 1, inputtedMoney);
    }

    /**
     * Tests and runs the Maintenance Functions of RegularVM machine.
     * This method simulates how am owner maintaining a vending machine would normally go.
     *
     * @param machine the machine to be run maintenance functions on
     */
    static void ConductMaintenance(RegularVM machine)
    {
        // Variables
        boolean isMaintenance = true;
        String choice;
        String itemName;

        // Menu Instructions
        System.out.println("Running Maintenance on " + machine.getName() + "...");
        while (isMaintenance)
        {
            System.out.println();
            System.out.println("=======================================");
            System.out.println("Welcome to the Maintenance Menu");
            System.out.println("Current machine: " + machine.getName());
            System.out.println("=======================================");
            System.out.println("Maintenance Options");
            System.out.println("1. Restock/Stock Item");
            System.out.println("2. Set Item Price");
            System.out.println("3. Collect Money");
            System.out.println("4. Replenish Change Pool");
            System.out.println("5. Print Transaction Summary");
            System.out.println("6. Back to Main Menu");
            System.out.println("=======================================");
            System.out.print("Enter your choice (1-6): ");
            choice = scanner.nextLine();

            switch (choice)
            {
                // region case 1 - Restock/Stock Item
                case "1":
                    System.out.println("Running Restock/Stock Item");
                    System.out.println();

                    machine.displayDebugInfo();
                    System.out.println("Input \"end\" to terminate restock");
                    System.out.print("Enter name of item to restock: ");
                    itemName = scanner.nextLine();

                    if (!itemName.equalsIgnoreCase("end"))
                    {
                        System.out.print("Enter items to restock: ");
                        int itemQuantity = scanner.nextInt();

                        machine.RestockItem(itemName, itemQuantity);
                    }
                    else
                    {
                        System.out.println("Terminating restock...");
                    }

                    break;
                    // endregion

                // region case 2 - Set Item Price
                case "2":
                    System.out.println("Running Set Item Price");
                    System.out.println();

                    machine.displayDebugInfo();
                    System.out.println("Input \"end\" to terminate Set Item Price");
                    System.out.print("Enter name of item to restock: ");
                    itemName = scanner.nextLine();

                    if (!itemName.equalsIgnoreCase("end"))
                    {
                        System.out.print("Enter new price: ");
                        int newPrice = scanner.nextInt();

                        machine.SetItemPrice(itemName, newPrice);
                    }
                    else
                    {
                        System.out.println("Terminating Set Item Price...");
                    }
                    break;
                    // endregion

                // region case 3 - Collect Money
                case "3":
                    System.out.println("Running Collect Money");
                    machine.CollectMoney();
                    break;
                    // endregion

                // region case 4 - Replenish Change Pool
                case "4":
                    Helper.MoneyInstructions();
                    System.out.print("Input: ");
                    String input = scanner.nextLine();

                    double temp = Helper.parseMoney(input, machine, 1);
                    System.out.printf("Replenished Change Pool totalling to Php %.2f\n", temp);

                    break;
                // endregion

                // region case 5 - Print Transaction Summary
                case "5":
                    machine.history.printReport(machine.getItems());
                    break;
                // endregion

                // region case 6 - Back to Main Menu
                case "6":
                    isMaintenance = false;
                    break;
                    // endregion

                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }

        System.out.println("Returning to main menu...");
    }

    private static void setupDefaultSteps(SpecialMachine vm){
        MachineItem dough      = new MachineItem("Thin Crust", 50.0, 10, 200, false);
        MachineItem sauce      = new MachineItem("Marinara", 20.0, 10, 15, false);
        MachineItem cheese     = new MachineItem("Cheese", 70.0, 10, 150, true);
        MachineItem pepperoni  = new MachineItem("Pepperoni", 35.0, 10, 100, true);

//        vm.getItems().add(dough);
//        vm.getItems().add(sauce);
//        vm.getItems().add(cheese);
//        vm.getItems().add(pepperoni);

        SpecialMachineStep step1 = new SpecialMachineStep("Dough Base");
        step1.addItem(dough);

        SpecialMachineStep step2 = new SpecialMachineStep("Sauce");
        step2.addItem(sauce);

        SpecialMachineStep step3 = new SpecialMachineStep("Toppings");
        step3.addItem(cheese);
        step3.addItem(pepperoni);

//        vm.addStep(step1);
//        vm.addStep(step2);
//        vm.addStep(step3);
    }

//    static void RunSpecialVendingMachine(SpecialMachine specialMachine){
//        ArrayList<MachineItem> selectedItems = new ArrayList<>();
//
//        System.out.println("=== Custom Pizza Order for " + specialMachine.getName() + " ===");
//
//        //display available ingredients
//        ArrayList<MachineItem> availableItems = specialMachine.getItems();
//        for (int i = 0; i < availableItems.size(); i++){
//            MachineItem item = availableItems.get(i);
//            System.out.printf("[%d] %s - Php %.2f (%f cal)\n", (i+1), item.getName(), item.getPrice(), item.getCalories());
//        }
//
//        //loop to let user choose ingredients
//        while (true){
//            System.out.print("Select item index to add to pizza (0 to finish): ");
//            String choiceStr = scanner.nextLine();
//            int idx = Integer.parseInt(choiceStr);
//
//            if (idx == 0){
//                break;
//            } else if (idx > 0 && idx <= availableItems.size()){
//                MachineItem chosen = availableItems.get(idx - 1);
//                selectedItems.add(chosen);
//                System.out.println("Added " + chosen.getName() + " to order.");
//            } else {
//                System.out.println("Invalid item index!");
//            }
//        }
//
//        if (selectedItems.isEmpty()){
//            System.out.println("No items selected. Cancelling your order!");
//            return;
//        }
//
//        double total = specialMachine.calculateTotalPrice(selectedItems);
//        System.out.printf("\nTotal Order Price: Php %.2f\n", total);
//
//        Helper.MoneyInstructions();
//        System.out.print("Insert Payment Input: ");
//        String input = scanner.nextLine();
//        double insertedMoney = Helper.parseMoney(input, specialMachine, 0);
//
//        boolean success = specialMachine.prepareAndDispense(selectedItems, insertedMoney);
//
//        if (!success){
//            System.out.printf("Transcation has failed! Returning payment: Php %.2f\n", insertedMoney);
//            Helper.undoParse(input, specialMachine, 0);
//        }
//    }
}
