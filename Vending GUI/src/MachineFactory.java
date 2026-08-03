import javax.swing.*;

public class MachineFactory
{
    public static RegularVM vendingMachine;
    public static int machineType = -1;

    public static void main (String[] args)
    {
        JFrame frame;
        frame = new UserInterface();
        frame.setVisible(true);
    }

    public static void createMachine(String type, String name) {

        if (type.equals("Special"))
        {
            vendingMachine = new SpecialMachine(name);
            machineType = 1;
        }
        else if (type.equals("Simple"))
        {
            vendingMachine = new RegularVM(name);
            machineType = 0;
        }

        System.out.println("Name: " + vendingMachine.getName());
    }

    public static RegularVM getVendingMachine()
    {
        return vendingMachine;
    }
}