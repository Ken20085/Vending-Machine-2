import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CashRegisterPanel extends JPanel implements Refreshable{

    private final Color MOCHA_BASE     = Color.decode("#1e1e2e");
    private final Color MOCHA_MANTLE   = Color.decode("#181825");
    private final Color MOCHA_SURFACE0 = Color.decode("#313244");
    private final Color MOCHA_SURFACE1 = Color.decode("#45475a");
    private final Color MOCHA_TEXT     = Color.decode("#cdd6f4");
    private final Color MOCHA_GREEN    = Color.decode("#a6e3a1");
    private final Color MOCHA_RED      = Color.decode("#f38ba8");

    // Inventory map tracking live stock counts
    private final Map<String, Integer> cashInventory = getHardcodedCashInventory();

    // Denomination values for quick total calculation
    private final Map<String, Double> denominationValues = getDenominationValueMap();

    // Label displaying total money value
    private JLabel totalValueLabel;

    private RegularVM vendingMachine;
    private JPanel listPanel;

    public CashRegisterPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(MOCHA_BASE);

        // Header Title
        JLabel titleLabel = new JLabel("Cash Register - Cash Stock");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 28));
        titleLabel.setForeground(MOCHA_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        this.add(titleLabel, BorderLayout.NORTH);

        // Vertical List Panel (BoxLayout)
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(MOCHA_BASE);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        initializeCashListRow();

        // ScrollPane Setup
        JScrollPane scrollPane = new JScrollPane(listPanel);
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

        // --- BOTTOM PANEL FOR TOTAL AMOUNT ---
        this.add(createTotalBottomPanel(), BorderLayout.SOUTH);

        refresh();
    }

    /**
     * Maps denomination string names to their double face values
     */
    private Map<String, Double> getDenominationValueMap() {
        Map<String, Double> valMap = new LinkedHashMap<>();
        valMap.put("PHP 1000 Bill", 1000.0);
        valMap.put("PHP 500 Bill", 500.0);
        valMap.put("PHP 200 Bill", 200.0);
        valMap.put("PHP 100 Bill", 100.0);
        valMap.put("PHP 50 Bill", 50.0);
        valMap.put("PHP 20 Bill", 20.0);
        valMap.put("PHP 20 Coin", 20.0);
        valMap.put("PHP 10 Coin", 10.0);
        valMap.put("PHP 5 Coin", 5.0);
        valMap.put("PHP 1 Coin", 1.0);
        return valMap;
    }

    /**
     * Initial PHP Denominations & Quantities
     */
    private Map<String, Integer> getHardcodedCashInventory() {
        Map<String, Integer> cashMap = new LinkedHashMap<>();
        cashMap.put("PHP 1000 Bill", 15);
        cashMap.put("PHP 500 Bill", 20);
        cashMap.put("PHP 200 Bill", 10);
        cashMap.put("PHP 100 Bill", 35);
        cashMap.put("PHP 50 Bill", 40);
        cashMap.put("PHP 20 Bill", 50);
        cashMap.put("PHP 20 Coin", 30);
        cashMap.put("PHP 10 Coin", 60);
        cashMap.put("PHP 5 Coin", 80);
        cashMap.put("PHP 1 Coin", 100);
        return cashMap;
    }

    /**
     * Bottom summary panel containing total value calculation
     */
    private JPanel createTotalBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(MOCHA_MANTLE);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, MOCHA_SURFACE1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleText = new JLabel("Total Cash Value in Register:");
        titleText.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
        titleText.setForeground(MOCHA_TEXT);

        totalValueLabel = new JLabel();
        totalValueLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 22));
        totalValueLabel.setForeground(MOCHA_GREEN);

        updateTotalCashValue(); // Set initial total

        bottomPanel.add(titleText, BorderLayout.WEST);
        bottomPanel.add(totalValueLabel, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * Calculates sum of all denominations in inventory and updates label text
     */
    private void updateTotalCashValue() {
        double total = 0.0;
//        for (vendingMachine.getRegister(). : cashInventory.entrySet()) {
//            double value = denominationValues.getOrDefault(entry.getKey(), 0.0);
//            total += value * entry.getValue();
//        }
        if (vendingMachine != null) total = vendingMachine.getRegister().getTotalValue();
        totalValueLabel.setText(String.format("PHP %,.2f", total));
    }

    /**
     * Creates a row displaying denomination, current stock, and a restock control box
     */
    private JPanel createCashListRow(int denominationValue, int initialCount) {
        JPanel row = new JPanel(new BorderLayout(15, 0));
        row.setBackground(MOCHA_MANTLE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        row.setPreferredSize(new Dimension(0, 55));

        // Initial status border
        Color statusColor = (initialCount <= 5) ? MOCHA_RED : MOCHA_GREEN;
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(statusColor, 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

        // Denomination Name (Left)
        JLabel nameLabel = new JLabel("Php " + denominationValue);
        nameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        nameLabel.setForeground(MOCHA_TEXT);

        // --- RIGHT SIDE CONTROLS ---
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        // Stock Count Label
        JLabel countLabel = new JLabel(initialCount + " pcs left");
        countLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
        countLabel.setForeground(statusColor);

        // Input Field for amount to add (Default: 0)
        JTextField addAmountInput = new JTextField("0", 4);
        addAmountInput.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
        addAmountInput.setBackground(MOCHA_SURFACE0);
        addAmountInput.setForeground(MOCHA_TEXT);
        addAmountInput.setCaretColor(MOCHA_TEXT);
        addAmountInput.setHorizontalAlignment(JTextField.CENTER);
        addAmountInput.setBorder(BorderFactory.createLineBorder(MOCHA_SURFACE1, 1));

        // "Add" Button
        JButton addBtn = new JButton("+ Add");
        addBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        addBtn.setBackground(MOCHA_GREEN);
        addBtn.setForeground(MOCHA_BASE);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        // --- ADD BUTTON ACTION LOGIC ---
        addBtn.addActionListener(e -> {
            try {
                int amountToAdd = Integer.parseInt(addAmountInput.getText().trim());
                if (amountToAdd > 0) {
                    // Update inventory map
                    int updatedStock = updateInventoryMap(denominationValue, amountToAdd, vendingMachine);

                    // Update row UI label and border color
                    countLabel.setText(updatedStock + " pcs left");
                    Color updatedColor = (updatedStock <= 5) ? MOCHA_RED : MOCHA_GREEN;
                    countLabel.setForeground(updatedColor);
                    row.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(updatedColor, 1),
                            BorderFactory.createEmptyBorder(8, 20, 8, 20)
                    ));

                    // Refresh global total value label
                    updateTotalCashValue();

                    // Pop-up confirmation dialog
                    JOptionPane.showMessageDialog(
                            this,
                            "Successfully added " + amountToAdd + " pcs to " + denominationValue + ".\nNew Total: " + updatedStock + " pcs",
                            "Restock Successful",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    // Reset text input field back to zero
                    addAmountInput.setText("0");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        rightPanel.add(countLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Spacer
        rightPanel.add(addAmountInput);
        rightPanel.add(addBtn);

        row.add(nameLabel, BorderLayout.WEST);
        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }

    private int updateInventoryMap(int denominationValue, int amountToAdd, RegularVM vendingMachine) {
        CashRegister register = vendingMachine.getRegister();
        register.addCash(denominationValue, amountToAdd, 1);

        int updatedStock = 0;
        ArrayList<Integer> validValues = Denomination.getValidValues();
        for (int i = 0; i < validValues.size(); i++) {
            if (validValues.get(i) == denominationValue) {
                updatedStock = register.getValueOnIndex(i);
            }
        }
        return updatedStock;
    }

    private void initializeCashListRow()
    {
        ArrayList<Integer> validDenominations = Denomination.getValidValues();
        CashRegister register = null;

        if(vendingMachine != null)
        {
            register = vendingMachine.getRegister();
        }

        if (register != null) {
            for (int i = 0; i < validDenominations.size(); i++) {
                int denominationValue = validDenominations.get(i);
                int initialCount = register.getValueOnIndex(i);

                listPanel.add(createCashListRow(denominationValue, initialCount));
                listPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between rows
            }
        } else {
            JLabel errorLabel = new JLabel("Error: Cash register data is unavailable.");
            errorLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
            errorLabel.setForeground(MOCHA_RED);
            listPanel.add(errorLabel);
        }
    }

    @Override
    public void refresh() {
        if (vendingMachine == null)
        {
            listPanel.removeAll();
            listPanel.repaint();
            return;
        }

        listPanel.removeAll();

        updateTotalCashValue();
        initializeCashListRow();

        listPanel.revalidate();
        listPanel.repaint();
    }

    public void setVendingMachine(RegularVM machine) {
        this.vendingMachine = machine;
    }
}