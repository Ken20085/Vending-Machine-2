import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardPanel extends JPanel implements Refreshable {

    private final Color MOCHA_BASE = Color.decode("#1e1e2e");
    private final Color MOCHA_MANTLE = Color.decode("#181825");
    private final Color MOCHA_SURFACE0 = Color.decode("#313244");
    private final Color MOCHA_SURFACE1 = Color.decode("#45475a");
    private final Color MOCHA_TEXT = Color.decode("#cdd6f4");
    private final Color MOCHA_SUBTEXT = Color.decode("#a6adc8");
    private final Color MOCHA_GREEN = Color.decode("#a6e3a1");
    private final Color MOCHA_RED = Color.decode("#f38ba8");

    private RegularVM vendingMachine;

    JPanel gridPanel;

    public DashboardPanel() {

        this.setLayout(new BorderLayout());
        this.setBackground(MOCHA_BASE);

        // Header Title
        JLabel titleLabel = new JLabel("Dashboard - Toppings & Sauces Stock");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 28));
        titleLabel.setForeground(MOCHA_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        this.add(titleLabel, BorderLayout.NORTH);

        // Main Grid Panel inside JScrollPane
        gridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        gridPanel.setBackground(MOCHA_BASE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Filtered Stock Data
        if (vendingMachine != null)
        {
            ArrayList<MachineItem> machineItems;
            machineItems = vendingMachine.getItems();

            for (MachineItem item: machineItems) {
                gridPanel.add(createStockCard(item.getName(), item.getStock()));
            }
        }

        // ScrollPane Wrapper
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Custom Scrollbar styling
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = MOCHA_SURFACE1;
                this.trackColor = MOCHA_BASE;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });

        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Stock Data containing ONLY Sauces & Toppings.
     * (Replace this with calls to your custom Vending Machine object!)
     */
    private Map<String, Integer> getStockInventory() {
        Map<String, Integer> items = new LinkedHashMap<>();

        // --- SAUCES ---
        items.put("Marinara Sauce", 30);
        items.put("Garlic Sauce", 15);
        items.put("Pesto Sauce", 8);
        items.put("White Sauce", 12);

        // --- TOPPINGS ---
        items.put("Pepperoni", 18);
        items.put("Mushroom", 5);   // Low stock example
        items.put("Pineapple", 12);
        items.put("Ham", 20);
        items.put("Beef Mince", 8);
        items.put("Basil Leaves", 3); // Low stock example
        items.put("Onion", 22);

        return items;
    }

    /**
     * Creates individual card showing item name and current quantity count
     */
    private JPanel createStockCard(String itemName, int stockQuantity) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(MOCHA_MANTLE);
        card.setPreferredSize(new Dimension(150, 110));

        // Highlight border red if stock is low (<= 5)
        Color statusColor = (stockQuantity <= 5) ? MOCHA_RED : MOCHA_GREEN;
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(statusColor, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;

        // Item Name
        JLabel nameLabel = new JLabel(itemName);
        nameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        nameLabel.setForeground(MOCHA_TEXT);

        // Stock Quantity Value
        JLabel countLabel = new JLabel(stockQuantity + " left");
        countLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
        countLabel.setForeground(statusColor);

        c.gridy = 0;
        card.add(nameLabel, c);

        c.gridy = 1;
        c.insets = new Insets(8, 0, 0, 0);
        card.add(countLabel, c);

        return card;
    }

    @Override
    public void refresh() {
        if (vendingMachine == null)
        {
            gridPanel.removeAll();
            gridPanel.repaint();
            return;
        }

        ArrayList<MachineItem> items = vendingMachine.getItems();

        gridPanel.removeAll();
        for (MachineItem item : items)
        {
            gridPanel.add(createStockCard(item.getName(), item.getStock()));
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public void setVendingMachine(RegularVM machine) {
        this.vendingMachine = machine;
    }
}