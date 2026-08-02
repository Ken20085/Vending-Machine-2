import java.util.ArrayList;

public class SpecialMachine {

    private String name;
    private final ArrayList<MachineItem> itemsToCheckout = new ArrayList<>();
    private final ArrayList<SpecialMachineStep> steps = new ArrayList<>();
    CashRegister register = new CashRegister();

    public SpecialMachine(String name) {
        this.name = name;
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


}
