import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PaymentPanel extends JFrame {

    // --- Catppuccin Mocha Color Palette ---
    private static final Color MOCHA_BASE     = Color.decode("#1e1e2e");
    private static final Color MOCHA_MANTLE   = Color.decode("#181825");
    private static final Color MOCHA_CRUST    = Color.decode("#11111b");
    private static final Color MOCHA_SURFACE0 = Color.decode("#313244");
    private static final Color MOCHA_TEXT     = Color.decode("#cdd6f4");
    private static final Color MOCHA_SUBTEXT  = Color.decode("#a6adc8");
    private static final Color MOCHA_GREEN    = Color.decode("#a6e3a1");
    private static final Color MOCHA_RED      = Color.decode("#f38ba8");

    // --- State Management ---
    private final double totalAmountDue;
    private double totalCashHanded = 0.0;

    // --- Denomination State Arrays ---
    private static final int[] DENOMINATIONS = {1000, 500, 200, 100, 50, 20, 10, 5, 1};
    private final int[] denominationCounts = new int[DENOMINATIONS.length];
    private final JLabel[] countLabels = new JLabel[DENOMINATIONS.length];

    // UI Feedback Labels
    private JLabel totalHandedLabel;
    private JLabel changeDueLabel;
    private JButton btnFinalize;

    // Helper class to pass ordered items
    public static class OrderItem {
        private final String name;
        private final double price;

        public OrderItem(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
    }

    public PaymentPanel(List<OrderItem> selectedItems) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(MOCHA_BASE);
        this.setSize(950, 700);
        this.setLocationRelativeTo(null);
        this.setTitle("Payment & Cash Calculator");

        // Calculate total amount due
        double calculatedTotal = 0.0;
        if (selectedItems != null) {
            for (OrderItem item : selectedItems) {
                calculatedTotal += item.getPrice();
            }
        }
        this.totalAmountDue = calculatedTotal;

        this.setLayout(new BorderLayout());

        // --- CENTER: TWO COLUMN LAYOUT (Summary on Left | Counter on Right) ---
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        mainContent.add(createSummaryPanel(selectedItems));
        mainContent.add(createDenominationPanel());

        this.add(mainContent, BorderLayout.CENTER);

        // --- SOUTH: BOTTOM NAVIGATION PANEL ---
        this.add(createBottomPanel(), BorderLayout.SOUTH);

        // Initial calculation refresh
        updateCalculations();
    }

    /**
     * LEFT COLUMN: Order Summary List & Total
     */
    private JPanel createSummaryPanel(List<OrderItem> items) {
        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setOpaque(false);

        JLabel titleLabel = new JLabel("Order Summary");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        titleLabel.setForeground(MOCHA_TEXT);
        container.add(titleLabel, BorderLayout.NORTH);

        JPanel itemsListPanel = new JPanel();
        itemsListPanel.setLayout(new BoxLayout(itemsListPanel, BoxLayout.Y_AXIS));
        itemsListPanel.setBackground(MOCHA_MANTLE);
        itemsListPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        if (items == null || items.isEmpty()) {
            JLabel emptyLabel = new JLabel("No items selected.");
            emptyLabel.setFont(new Font("JetBrains Mono", Font.ITALIC, 14));
            emptyLabel.setForeground(MOCHA_SUBTEXT);
            itemsListPanel.add(emptyLabel);
        } else {
            for (OrderItem item : items) {
                itemsListPanel.add(createItemRow(item.getName(), item.getPrice()));
                itemsListPanel.add(Box.createRigidArea(new Dimension(0, 6)));
            }
        }

        JScrollPane scrollPane = new JScrollPane(itemsListPanel);
        scrollPane.getViewport().setBackground(MOCHA_MANTLE);
        scrollPane.setBorder(BorderFactory.createLineBorder(MOCHA_SURFACE0, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        container.add(scrollPane, BorderLayout.CENTER);

        // Total Cost Banner
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(MOCHA_CRUST);
        totalPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MOCHA_SURFACE0, 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel totalTextLabel = new JLabel("Total Due:");
        totalTextLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        totalTextLabel.setForeground(MOCHA_TEXT);

        JLabel totalValueLabel = new JLabel(String.format("Php %.2f", totalAmountDue));
        totalValueLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
        totalValueLabel.setForeground(MOCHA_GREEN);

        totalPanel.add(totalTextLabel, BorderLayout.WEST);
        totalPanel.add(totalValueLabel, BorderLayout.EAST);

        container.add(totalPanel, BorderLayout.SOUTH);

        return container;
    }

    /**
     * RIGHT COLUMN: Denomination Counter & Calculation Display
     */
    private JPanel createDenominationPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 10));
        container.setOpaque(false);

        JLabel titleLabel = new JLabel("Cash Counter");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        titleLabel.setForeground(MOCHA_TEXT);
        container.add(titleLabel, BorderLayout.NORTH);

        // Grid of Denominations
        JPanel grid = new JPanel(new GridLayout(DENOMINATIONS.length, 1, 0, 4));
        grid.setBackground(MOCHA_MANTLE);
        grid.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        for (int i = 0; i < DENOMINATIONS.length; i++) {
            grid.add(createDenominationRow(i));
        }

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.getViewport().setBackground(MOCHA_MANTLE);
        scrollPane.setBorder(BorderFactory.createLineBorder(MOCHA_SURFACE0, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        container.add(scrollPane, BorderLayout.CENTER);

        // Live Calculations Area (Cash Tendered & Change)
        JPanel calcPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        calcPanel.setBackground(MOCHA_CRUST);
        calcPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MOCHA_SURFACE0, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel handedRow = new JPanel(new BorderLayout());
        handedRow.setOpaque(false);
        JLabel lblHandedTitle = new JLabel("Cash Tendered:");
        lblHandedTitle.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        lblHandedTitle.setForeground(MOCHA_TEXT);
        totalHandedLabel = new JLabel("Php 0.00");
        totalHandedLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
        totalHandedLabel.setForeground(MOCHA_TEXT);
        handedRow.add(lblHandedTitle, BorderLayout.WEST);
        handedRow.add(totalHandedLabel, BorderLayout.EAST);

        JPanel changeRow = new JPanel(new BorderLayout());
        changeRow.setOpaque(false);
        JLabel lblChangeTitle = new JLabel("Change Due:");
        lblChangeTitle.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
        lblChangeTitle.setForeground(MOCHA_TEXT);
        changeDueLabel = new JLabel("Php 0.00");
        changeDueLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        changeDueLabel.setForeground(MOCHA_RED);
        changeRow.add(lblChangeTitle, BorderLayout.WEST);
        changeRow.add(changeDueLabel, BorderLayout.EAST);

        calcPanel.add(handedRow);
        calcPanel.add(changeRow);

        container.add(calcPanel, BorderLayout.SOUTH);

        return container;
    }

    private JPanel createDenominationRow(int index) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        int denomValue = DENOMINATIONS[index];

        JLabel valueLabel = new JLabel(String.format("Php %d", denomValue));
        valueLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        valueLabel.setForeground(MOCHA_TEXT);
        valueLabel.setPreferredSize(new Dimension(80, 25));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        controls.setOpaque(false);

        JButton btnMinus = createCounterButton("-");
        JLabel countLabel = new JLabel("0", SwingConstants.CENTER);
        countLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        countLabel.setForeground(MOCHA_TEXT);
        countLabel.setPreferredSize(new Dimension(28, 25));

        JButton btnPlus = createCounterButton("+");

        countLabels[index] = countLabel;

        btnMinus.addActionListener(e -> {
            if (denominationCounts[index] > 0) {
                denominationCounts[index]--;
                countLabels[index].setText(String.valueOf(denominationCounts[index]));
                updateCalculations();
            }
        });

        btnPlus.addActionListener(e -> {
            denominationCounts[index]++;
            countLabels[index].setText(String.valueOf(denominationCounts[index]));
            updateCalculations();
        });

        controls.add(btnMinus);
        controls.add(countLabel);
        controls.add(btnPlus);

        row.add(valueLabel, BorderLayout.WEST);
        row.add(controls, BorderLayout.EAST);

        return row;
    }

    private JButton createCounterButton(String symbol) {
        JButton btn = new JButton(symbol);
        btn.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
        btn.setBackground(MOCHA_SURFACE0);
        btn.setForeground(MOCHA_TEXT);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(32, 24));
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateCalculations() {
        totalCashHanded = 0.0;

        for (int i = 0; i < DENOMINATIONS.length; i++) {
            totalCashHanded += (DENOMINATIONS[i] * denominationCounts[i]);
        }

        totalHandedLabel.setText(String.format("Php %.2f", totalCashHanded));

        double change = totalCashHanded - totalAmountDue;

        if (totalCashHanded >= totalAmountDue) {
            changeDueLabel.setText(String.format("Php %.2f", change));
            changeDueLabel.setForeground(MOCHA_GREEN);
            btnFinalize.setEnabled(true);
        } else {
            double remaining = totalAmountDue - totalCashHanded;
            changeDueLabel.setText(String.format("-Php %.2f", remaining));
            changeDueLabel.setForeground(MOCHA_RED);
            btnFinalize.setEnabled(false);
        }
    }

    private JPanel createItemRow(String name, double price) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        nameLabel.setForeground(MOCHA_TEXT);

        String priceStr = price == 0.0 ? "Free" : String.format("Php %.2f", price);
        JLabel priceLabel = new JLabel(priceStr);
        priceLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        priceLabel.setForeground(MOCHA_SUBTEXT);

        row.add(nameLabel, BorderLayout.WEST);
        row.add(priceLabel, BorderLayout.EAST);

        return row;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(MOCHA_MANTLE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, MOCHA_SURFACE0));

        JButton btnBack = new JButton("Back to Kiosk");
        styleButton(btnBack, MOCHA_RED, MOCHA_BASE);
        btnBack.addActionListener(e -> this.dispose());

        btnFinalize = new JButton("Finalize Pay");
        styleButton(btnFinalize, MOCHA_GREEN, MOCHA_BASE);
        btnFinalize.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    String.format("Payment Successful!\nChange: Php %.2f", (totalCashHanded - totalAmountDue)),
                    "Receipt", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        });

        bottomPanel.add(btnBack);
        bottomPanel.add(btnFinalize);

        return bottomPanel;
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}