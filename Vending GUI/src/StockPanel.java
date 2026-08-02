import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class StockPanel extends JPanel {

    private final Color MOCHA_BASE     = Color.decode("#1e1e2e");
    private final Color MOCHA_MANTLE   = Color.decode("#181825");
    private final Color MOCHA_SURFACE0 = Color.decode("#313244");
    private final Color MOCHA_SURFACE1 = Color.decode("#45475a");
    private final Color MOCHA_TEXT     = Color.decode("#cdd6f4");
    private final Color MOCHA_GREEN    = Color.decode("#a6e3a1");
    private final Color MOCHA_RED      = Color.decode("#f38ba8");

    // Inventory map tracking live stock counts
    private final Map<String, Integer> itemInventory = getHardcodedStockInventory();

    public StockPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(MOCHA_BASE);

        // Header Title
        JLabel titleLabel = new JLabel("Item Stock - Restock Items");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 28));
        titleLabel.setForeground(MOCHA_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        this.add(titleLabel, BorderLayout.NORTH);

        // Main Grid Panel for Box Cards (3 columns)
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        gridPanel.setBackground(MOCHA_BASE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        for (Map.Entry<String, Integer> entry : itemInventory.entrySet()) {
            gridPanel.add(createStockCard(entry.getKey(), entry.getValue()));
        }

        // ScrollPane Setup
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

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
     * Initial Items Stock Inventory
     * (Replace with calls to your Vending Machine object later!)
     */
    private Map<String, Integer> getHardcodedStockInventory() {
        Map<String, Integer> items = new LinkedHashMap<>();

        // --- DOUGH & SAUCES ---
        items.put("Pizza Crust Base", 25);
        items.put("Marinara Sauce", 30);
        items.put("Garlic Sauce", 15);
        items.put("Pesto Sauce", 8);
        items.put("White Sauce", 12);

        // --- CHEESE & TOPPINGS ---
        items.put("Mozzarella Cheese", 25);
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
     * Creates a card/box displaying item name, current stock, and a restock control row
     */
    private JPanel createStockCard(String itemName, int initialCount) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(MOCHA_MANTLE);
        card.setPreferredSize(new Dimension(200, 140));

        // Initial status border (Red for <= 5, Green otherwise)
        Color statusColor = (initialCount <= 5) ? MOCHA_RED : MOCHA_GREEN;
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(statusColor, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;

        // Item Name
        JLabel nameLabel = new JLabel(itemName);
        nameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
        nameLabel.setForeground(MOCHA_TEXT);

        // Stock Count Label
        JLabel countLabel = new JLabel(initialCount + " pcs left");
        countLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        countLabel.setForeground(statusColor);

        // --- RESTOCK CONTROLS (Input + Add Button) ---
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        controlPanel.setOpaque(false);

        JTextField addAmountInput = new JTextField("0", 3);
        addAmountInput.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        addAmountInput.setBackground(MOCHA_SURFACE0);
        addAmountInput.setForeground(MOCHA_TEXT);
        addAmountInput.setCaretColor(MOCHA_TEXT);
        addAmountInput.setHorizontalAlignment(JTextField.CENTER);
        addAmountInput.setBorder(BorderFactory.createLineBorder(MOCHA_SURFACE1, 1));

        JButton addBtn = new JButton("+ Add");
        addBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
        addBtn.setBackground(MOCHA_GREEN);
        addBtn.setForeground(MOCHA_BASE);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        // --- ADD BUTTON ACTION LOGIC ---
        addBtn.addActionListener(e -> {
            try {
                int amountToAdd = Integer.parseInt(addAmountInput.getText().trim());
                if (amountToAdd > 0) {
                    // Update inventory map
                    int currentStock = itemInventory.getOrDefault(itemName, 0);
                    int updatedStock = currentStock + amountToAdd;
                    itemInventory.put(itemName, updatedStock);

                    // Update UI card label and border color
                    countLabel.setText(updatedStock + " pcs left");
                    Color updatedColor = (updatedStock <= 5) ? MOCHA_RED : MOCHA_GREEN;
                    countLabel.setForeground(updatedColor);
                    card.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(updatedColor, 1),
                            BorderFactory.createEmptyBorder(12, 12, 12, 12)
                    ));

                    // Pop-up confirmation dialog
                    JOptionPane.showMessageDialog(
                            this,
                            "Successfully added " + amountToAdd + " pcs of " + itemName + ".\nNew Total: " + updatedStock + " pcs",
                            "Restock Successful",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Reset input field back to zero
                    addAmountInput.setText("0");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        controlPanel.add(addAmountInput);
        controlPanel.add(addBtn);

        // Layout assembly
        c.gridy = 0;
        card.add(nameLabel, c);

        c.gridy = 1;
        c.insets = new Insets(6, 0, 10, 0);
        card.add(countLabel, c);

        c.gridy = 2;
        c.insets = new Insets(0, 0, 0, 0);
        card.add(controlPanel, c);

        return card;
    }
}