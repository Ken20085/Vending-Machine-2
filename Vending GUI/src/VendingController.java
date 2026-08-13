import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Container;

public class VendingController {

    private final UserInterface mainFrame;
    private final DashboardPanel dashboardPanel;
    private final StockPanel stockPanel;
    private final CashRegisterPanel cashRegisterPanel;

    private Object currentMachine; // Holds either RegularVM or SpecialMachine
    private int machineType = -1;  // 0 for Simple, 1 for Special

    public VendingController(
            UserInterface mainFrame,
            DashboardPanel dashboardPanel,
            StockPanel stockPanel,
            CashRegisterPanel cashRegisterPanel) {

        this.mainFrame = mainFrame;
        this.dashboardPanel = dashboardPanel;
        this.stockPanel = stockPanel;
        this.cashRegisterPanel = cashRegisterPanel;

        initControllerEvents();
    }

    /**
     * Binds view actions and button hooks to controller logic, completely decoupling
     * the views from static factory calls.
     */
    private void initControllerEvents() {
        CreatePanel createPanel = mainFrame.getCreatePanel();

        if (createPanel == null) {
            System.out.println("ERROR: CreatePanel is null in VendingController!");
            return;
        }

        createPanel.addCreateActionListener(e -> {
            System.out.println("CREATE BUTTON CLICKED!");

            try {
                String type = createPanel.getSelectedType();
                String name = createPanel.getVendingMachineName();

                System.out.println("Type: " + type + ", Name: " + name);

                // 1. Instantiate the model
                if ("Special".equals(type)) {
                    SpecialMachine special = new SpecialMachine(name);
                    this.currentMachine = special;
                    this.machineType = 1;
                    mainFrame.updateMachineData(null, special);
                } else {
                    RegularVM regular = new RegularVM(name);
                    this.currentMachine = regular;
                    this.machineType = 0;
                    mainFrame.updateMachineData(regular, null);
                }

                // 2. Propagate data to all other panels
                updatePanelsWithActiveMachine();

                // 3. Reset form and return to menu
                createPanel.resetForm();

                Container parent = createPanel.getParent();
                if (parent != null && parent.getLayout() instanceof CardLayout cardLayout) {
                    cardLayout.show(parent, "menuPanel");
                } else {
                    System.out.println("ERROR: Could not find CardLayout parent for CreatePanel!");
                }

            } catch (Exception ex) {
                System.out.println("ERROR OCCURRED WHILE CREATING MACHINE:");
                ex.printStackTrace(); // This will print the exact line causing the crash
            }
        });
    }

    /**
     * Safely updates all dependent panels with the current active machine model
     * and triggers their refresh contracts.
     */
    public void updatePanelsWithActiveMachine() {
        RegularVM targetVm = null;

        if (currentMachine instanceof SpecialMachine special) {
            targetVm = special;
        } else if (currentMachine instanceof RegularVM regular) {
            targetVm = regular;
        }

        ModifyPanel modifyPanel = mainFrame.getModifyPanel();

        // Pass model down to all configuration panels
        stockPanel.setVendingMachine(targetVm);
        cashRegisterPanel.setVendingMachine(targetVm);
        modifyPanel.setVendingMachine(targetVm); // <-- Crucial line

        stockPanel.refresh();
        dashboardPanel.refresh();
        cashRegisterPanel.refresh();
    }

    /**
     * Helper utility to handle CardLayout navigation from sub-components.
     */
    private void navigateToCard(JPanel panel, String cardName) {
        Container parent = panel.getParent();
        if (parent != null && parent.getLayout() instanceof CardLayout cardLayout) {
            cardLayout.show(parent, cardName);
        }
    }

    public int getMachineType() {
        return machineType;
    }

    public Object getCurrentMachine() {
        return currentMachine;
    }
}