import javax.swing.*;

public class MachineFactory
{
//    public static Object createMachine(String type, String name) {
//        if (type.equals("Special")) {
//            return new SpecialMachine(name);
//        } else if (type.equals("Simple")) {
//            return new RegularVM(name);
//        }
//        return null;
//    }

    public static void main (String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserInterface mainFrame = new UserInterface();

            DashboardPanel dashboardPanel = new DashboardPanel();
            StockPanel stockPanel = new StockPanel();
            CashRegisterPanel cashRegisterPanel = new CashRegisterPanel();

            VendingController controller = new VendingController(
                    mainFrame,
                    dashboardPanel,
                    stockPanel,
                    cashRegisterPanel
            );

            mainFrame.setVisible(true);
        });
    }
}