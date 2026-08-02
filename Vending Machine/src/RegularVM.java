import java.util.ArrayList;

public class RegularVM {

    private final String name;
    private final ArrayList<MachineItem> items = new ArrayList<>();
    private int itemCount;
    CashRegister register = new CashRegister();
    History history;

    public RegularVM(String name)
    {
        this.name = name;
        GeneratePresetItems();
        history = new History(getItemCount());
        history.restockPeriod(items);
    }

    public String getName() {
        return name;
    }
    public CashRegister getRegister() {return register;}
    public int getItemCount() {return itemCount;}

    public ArrayList<MachineItem> getItems()
    {
        return this.items;
    }

//    public void addItem(MachineItem item)
//    {
//        items.add(item);
//    }

    public void GeneratePresetItems()
    {
        items.add(new MachineItem(
                "Pepperoni",
                20,
                2));
        items.add(new MachineItem(
                "Mushroom",
                15,
                2));
        items.add(new MachineItem(
                "Pineapple",
                25,
                2));
        items.add(new MachineItem(
                "Ham",
                10,
                2));
        items.add(new MachineItem(
                "Mozzarella",
                15,
                2));
        items.add(new MachineItem(
                "Beef Mince",
                10,
                2));
        items.add(new MachineItem(
                "Basil Leaves",
                30,
                2));
        items.add(new MachineItem(
                "Sonion",
                100,
                2));

        this.itemCount = items.size();
    }

    public void RestockItem(String itemName, int quantity)
    {
        // Variables
        MachineItem itemToRestock = Helper.findItem(itemName, items);
        int index = items.indexOf(Helper.findItem(itemName, items));
        boolean successfulRestock;

        // Modify itemToRestock
        if (itemToRestock == null)
        {
            System.out.println("Item \"" + itemName + "\" not found");
            return;
        }

        successfulRestock = itemToRestock.restock(quantity);
        items.set(index, itemToRestock);

        // Add to Restock Log
        if (successfulRestock) history.restockPeriod(items);
    }

    public void CollectMoney()
    {
        this.register.doMoneyGotten();
    }

    public void SetItemPrice(String itemName, int price) // Incomplete
    {
        // Variables
        MachineItem itemToReprice = Helper.findItem(itemName, items);
        double previousPrice;
        int index = items.indexOf(Helper.findItem(itemName, items));

        // Modify itemToRestock
        if (itemToReprice == null)
        {
            System.out.println("Item " + itemName + " not found");
            return;
        }
        previousPrice = itemToReprice.getPrice();

        System.out.printf("Set price of %s from Php %.2f to Php %.2f\n",
                itemToReprice.getName(),
                previousPrice,
                (double)price);
        itemToReprice.setPrice(price);
        items.set(index, itemToReprice);

        // Add to Restock Log
    }

    public void dispenseItem(int itemIndex, double inputMoney)
    {
        // Variables
        double itemPrice = items.get(itemIndex).getPrice();
        double change = inputMoney - itemPrice;
        ArrayList<Integer> changeDenominations = register.calculateChange((int)change);
//        String changeToGive = changeDenominations.toString();

        // Check if vending machine has enough change to give
        if (register.changeIsPossible(change) && changeDenominations != null)
        {
            items.get(itemIndex).transact();
            System.out.println("Dispensed " + items.get(itemIndex).getName());
            generatePurchaseSummary(itemIndex);

            register.deductCash(changeDenominations);
            history.recordSale(itemIndex,
                    items.get(itemIndex).getName(),
                    itemPrice,
                    1);
        }
        else
        {
//            if (!register.changeIsPossible(change)) System.out.println("flag 1");
//            if (changeDenominations == null) System.out.println("flag 2");

            System.out.println("Vending machine cannot dispense enough change.");
            System.out.printf("Here's your Php %.2f back\n", inputMoney);
        }
    }

    private void generatePurchaseSummary(int itemIndex)
    {
        System.out.println("Generating purchase summary...");
        System.out.println();
        System.out.println("Item " + (itemIndex + 1) + ": " + items.get(itemIndex).getName());
        System.out.println("Quantity: 1");
        System.out.printf("Price: Php %.2f\n", items.get(itemIndex).getPrice());
        System.out.println("Have a nice day!");
    }

    public void displayVendingMachine()
    {
        int i;
        int maxSpace = 33;
        int leftPad = name.length() + (maxSpace - name.length()) / 2;
        String nameFormat = String.format("%" + leftPad + "s", name);


        System.out.println("* * * * * * * * * * * * * * * * * * *");
        System.out.printf("* %-33s *\n", nameFormat);
        System.out.println("* * * * * * * * * * * * * * * * * * *");
        for (i = 1; i <= items.size(); i += 2)
        {
            System.out.printf("* %s %-10d * %s %-10d *\n", "Item", i, "Item", i + 1);
            System.out.printf("* %-15s * %-15s *\n", items.get(i - 1).getName(), items.get(i).getName());
            System.out.printf("* %s %-11.2f * %s %-11.2f *\n",
                    "Php",
                    items.get(i - 1).getPrice(),
                    "Php",
                    items.get(i).getPrice());
            System.out.printf("* %s %-5d * %s %-5d *\n",
                    "In Stock:",
                    items.get(i - 1).getStock(),
                    "In Stock:",
                    items.get(i).getStock());
            System.out.println("* * * * * * * * * * * * * * * * * * *");
        }
        System.out.println("*       ■■■■■■■■■■■■■■■■■■■■■       *");
        System.out.println("* * * * * * * * * * * * * * * * * * *");
    }

    public void displayDebugInfo()
    {
        // Print Current Items with their stock and price
        System.out.println("+-----+-----+-----+-----+");
        System.out.println("Current items in Machine");
        System.out.println("+-----+-----+-----+-----+");

        int i = 0;
        for (MachineItem item : items)
        {
            i++;
            System.out.printf("%d. %-15s : Php%-10.2f : %d in stock%n", i, item.getName(), (float)item.getPrice(), item.getStock());
        }

        // Print current denominations in change pool

    }
}
