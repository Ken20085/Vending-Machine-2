import java.util.ArrayList;

/**
 * Represents the
 */
public class History{
    private int[] startingInventory; //array to hold item counts at the start records it here before restocking finishes
    private int[] soldPerSlot;       //counts how many were sold per slot index

    private final ArrayList<String> transactions;   //array to hold string messages that describe whatever occured

    /**
     * constructor that sets up the ledger that keep tracks of events.
     *
     * @param numberOfItemSlots how many items the machine has (in this case 8)
     */
    public History(int numberOfItemSlots){
        //arrays to match the number of slots the vending machine has
        this.startingInventory = new int[numberOfItemSlots];
        this.soldPerSlot = new int[numberOfItemSlots];

        //ledger records entries per cycle
        this.transactions = new ArrayList<>();
    }

    /**
     * Adds a String sentence that describes the event to the event logs.
     *
     * @param message describes the event (e.g. Sold x pepperoni, Restocked...)
     */
    public void addLogMessage(String message){
        //add message into the ledger
        this.transactions.add(message);
    }

    /**
     * called when a transaction is made to record the sale.
     *
     * @param slotIndex position of the item in the ArrayList
     * @param itemName string name of the item
     * @param itemPrice cost of the item
     * @param quantity number of units sold for that item
     */
    public void recordSale(int slotIndex, String itemName, double itemPrice, int quantity){
        //increment sales count for this slot
        this.soldPerSlot[slotIndex] += quantity;

        //record into the ledger what was sold
        addLogMessage("Sold: " + quantity + "x " + itemName + " for " + itemPrice);
    }

    /**
     * clears the ledger when a restocking is performed or a pricing change
     * @param currentItems the current list of machine items present in the vending machine
     */
    public void restockPeriod(ArrayList<MachineItem> currentItems){
        int i;
        this.transactions.clear();  //clear the list of events

        //update sizes of array if item/s were added/removed
        if (this.startingInventory.length != currentItems.size()) {
            this.startingInventory = new int[currentItems.size()];
            this.soldPerSlot = new int[currentItems.size()];
        }

        //loop through all items available inside the vending machine
        for (i = 0; i < currentItems.size(); i++){
            this.soldPerSlot[i] = 0;                                    //clear out old sales log
            this.startingInventory[i] = currentItems.get(i).getStock(); //make new starting point
        }
        addLogMessage("SYSTEM: Maintenance cycle has started! Inventory recorded.");
    }

    /**
     * prints out report for the admin
     * @param currentItems live amount of items to calculate the ending stock
     */
    public void printReport(ArrayList<MachineItem> currentItems){
        int i;
        double totalRevenue = 0.0;

        System.out.println("\n==================================================");
        System.out.println("              VENDING MACHINE REPORT              ");
        System.out.println("==================================================");
        System.out.printf("%-15s | %-10s | %-10s | %10s\n", "Item Name", "Starting", "Ending", "Units Sold");
        System.out.println("--------------------------------------------------");

        //loop through all items to print the tracker
        for (i = 0; i < currentItems.size(); i++){
            MachineItem item = currentItems.get(i);

            double slotRevenue = this.soldPerSlot[i] * item.getPrice();
            totalRevenue +=  slotRevenue;

            //should make a comparison printout
            System.out.printf("%-15s | %-10d | %-10d | %10d\n",
                    item.getName(),             //item name
                    this.startingInventory[i],  //recorded quantity from the start of a cycle
                    item.getStock(),            //the ending inventory/what was left
                    this.soldPerSlot[i]);       //prints number of units sold successfully
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("Total Revenue: %.2f\n",totalRevenue);
        System.out.println("--------------------------------------------------");

        System.out.println("-------------------------------------------------");
        System.out.println("                    EVENT LOGS                    ");
        System.out.println("-------------------------------------------------");

        //condition for checking if nothing was recorded
        if (this.transactions.isEmpty()){
            System.out.println("No events recorded for this cycle.");
        }

        else{
            for (i = 0; i < this.transactions.size(); i++){
                System.out.println("(" + (i + 1) + ") " + this.transactions.get(i));
            }
        }
        System.out.println("==================================================");
    }
}