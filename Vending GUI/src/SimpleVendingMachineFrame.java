import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleVendingMachineFrame extends JFrame {

    // Catppuccin Mocha Color Palette
    private final Color MOCHA_BASE     = Color.decode("#1e1e2e");
    private final Color MOCHA_MANTLE   = Color.decode("#181825");
    private final Color MOCHA_SURFACE0 = Color.decode("#313244");
    private final Color MOCHA_SURFACE1 = Color.decode("#45475a");
    private final Color MOCHA_TEXT     = Color.decode("#cdd6f4");
    private final Color MOCHA_GREEN    = Color.decode("#a6e3a1");
    private final Color MOCHA_RED      = Color.decode("#f38ba8");

    // Inventory and inputs tracking

    private RegularVM vendingMachine;
    private final Map<String, JTextField> quantityInputs = new LinkedHashMap<>();

    // UI Reference Buttons
    private JButton finalizePaymentBtn;
    private JButton cancelBtn;

    public SimpleVendingMachineFrame(RegularVM vm) {
        super("Simple Vending Machine");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.vendingMachine = vm;

        this.setSize(900, 750);
        this.setPreferredSize(new Dimension(900, 750));
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(MOCHA_BASE);

        JLabel headerLabel = new JLabel("Simple Vending Machine");
        headerLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 26));
        headerLabel.setForeground(MOCHA_TEXT);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        mainContent.add(headerLabel, BorderLayout.NORTH);

        mainContent.add(createProductGridPanel(), BorderLayout.CENTER);
        mainContent.add(createBottomPanel(), BorderLayout.SOUTH);

        this.setContentPane(mainContent);
    }

    private static class ProductItem {
        double price;
        int stock;
        String imagePath;

        ProductItem(double price, int stock, String imagePath) {
            this.price = price;
            this.stock = stock;
            this.imagePath = imagePath;
        }
    }

    private Map<String, ProductItem> getHardcodedInventory() {
        Map<String, ProductItem> map = new LinkedHashMap<>();
        map.put("Classic Pizza",   new ProductItem(120.00, 10, "images/pizza.png"));
        map.put("Pepperoni",       new ProductItem(140.00, 8,  "images/pepperoni.png"));
        map.put("Hawaiian",        new ProductItem(135.00, 5,  "images/hawaiian.png"));
        map.put("Garlic Sticks",   new ProductItem(75.00,  12, "images/garlic_sticks.png"));
        map.put("Coke 330ml",      new ProductItem(45.00,  15, "images/coke.png"));
        map.put("Iced Tea",        new ProductItem(50.00,  10, "images/iced_tea.png"));
        map.put("Bottled Water",   new ProductItem(25.00,  20, "images/water.png"));
        map.put("Sprite 330ml",    new ProductItem(45.00,  12, "images/sprite.png"));
        map.put("Potato Chips",    new ProductItem(60.00,  18, "images/chips.png"));
        map.put("Chocolate Bar",   new ProductItem(55.00,  14, "images/chocolate.png"));
        return map;
    }

    private JScrollPane createProductGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        gridPanel.setBackground(MOCHA_BASE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 15));

        for (MachineItem item : vendingMachine.getItems()) {
            gridPanel.add(createItemCard(item));
        }

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
            protected JButton createDecreaseButton(int orientation) { return createZeroBtn(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return createZeroBtn(); }

            private JButton createZeroBtn() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });

        return scrollPane;
    }

    private JPanel createItemCard(MachineItem item) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(MOCHA_MANTLE);
        card.setPreferredSize(new Dimension(155, 165));

        Color statusColor = (item.getStock() <= 5) ? MOCHA_RED : MOCHA_GREEN;
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(statusColor, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.CENTER;

        // Placeholder icon because MachineItem has no image path
        ImageIcon imageIcon = createPlaceholderIcon(40, 40);
        JLabel iconLabel = new JLabel(imageIcon);

        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
        nameLabel.setForeground(MOCHA_TEXT);

        JLabel infoLabel = new JLabel(String.format("PHP %.2f | %d pcs", item.getPrice(), item.getStock()));
        infoLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 11));
        infoLabel.setForeground(statusColor);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 0));
        controlPanel.setOpaque(false);

        JButton minusBtn = new JButton("-");
        minusBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        minusBtn.setBackground(MOCHA_SURFACE0);
        minusBtn.setForeground(MOCHA_TEXT);
        minusBtn.setFocusPainted(false);
        minusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        minusBtn.setPreferredSize(new Dimension(30, 26));
        minusBtn.setMargin(new Insets(0, 0, 0, 0));

        JTextField qtyInput = new JTextField("0", 2);
        qtyInput.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
        qtyInput.setBackground(MOCHA_SURFACE0);
        qtyInput.setForeground(MOCHA_TEXT);
        qtyInput.setCaretColor(MOCHA_TEXT);
        qtyInput.setHorizontalAlignment(JTextField.CENTER);
        qtyInput.setBorder(BorderFactory.createLineBorder(MOCHA_SURFACE1, 1));

        // Store references for finalize payment processing
        quantityInputs.put(item.getName(), qtyInput);

        JButton plusBtn = new JButton("+");
        plusBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        plusBtn.setBackground(MOCHA_GREEN);
        plusBtn.setForeground(MOCHA_BASE);
        plusBtn.setFocusPainted(false);
        plusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        plusBtn.setPreferredSize(new Dimension(30, 26));
        plusBtn.setMargin(new Insets(0, 0, 0, 0));

        minusBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(qtyInput.getText().trim());
                if (val > 0) qtyInput.setText(String.valueOf(val - 1));
            } catch (NumberFormatException ignored) {}
        });

        plusBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(qtyInput.getText().trim());
                if (val < item.getStock()) qtyInput.setText(String.valueOf(val + 1));
            } catch (NumberFormatException ignored) {}
        });

        controlPanel.add(minusBtn);
        controlPanel.add(qtyInput);
        controlPanel.add(plusBtn);

        c.gridy = 0;
        c.insets = new Insets(0, 0, 4, 0);
        card.add(iconLabel, c);

        c.gridy = 1;
        c.insets = new Insets(0, 0, 2, 0);
        card.add(nameLabel, c);

        c.gridy = 2;
        c.insets = new Insets(0, 0, 6, 0);
        card.add(infoLabel, c);

        c.gridy = 3;
        c.insets = new Insets(0, 0, 0, 0);
        card.add(controlPanel, c);

        return card;
    }

    private ImageIcon loadScaledImageIcon(String path, int width, int height) {
        try {
            ImageIcon rawIcon = new ImageIcon(path);
            if (rawIcon.getImageLoadStatus() != MediaTracker.COMPLETE || rawIcon.getIconWidth() <= 0) {
                java.net.URL resource = getClass().getClassLoader().getResource(path);
                if (resource != null) {
                    rawIcon = new ImageIcon(resource);
                }
            }

            if (rawIcon.getIconWidth() > 0) {
                Image scaledImg = rawIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImg);
            }
        } catch (Exception ignored) {}

        return createPlaceholderIcon(width, height);
    }

    private ImageIcon createPlaceholderIcon(int width, int height) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(MOCHA_SURFACE0);
        g2.fillRoundRect(0, 0, width, height, 8, 8);
        g2.setColor(MOCHA_TEXT);
        g2.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
        g2.drawString("?", width / 2 - 5, height / 2 + 6);
        g2.dispose();
        return new ImageIcon(img);
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setBackground(MOCHA_MANTLE);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, MOCHA_SURFACE1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        cancelBtn.setBackground(MOCHA_RED);
        cancelBtn.setForeground(MOCHA_BASE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        cancelBtn.addActionListener(e -> this.dispose());

        finalizePaymentBtn = new JButton("Finalize Payment");
        finalizePaymentBtn.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        finalizePaymentBtn.setBackground(MOCHA_GREEN);
        finalizePaymentBtn.setForeground(MOCHA_BASE);
        finalizePaymentBtn.setFocusPainted(false);
        finalizePaymentBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        finalizePaymentBtn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        // Connects selected items to PaymentPanel
        finalizePaymentBtn.addActionListener(e -> openPaymentPanel());

        bottomPanel.add(cancelBtn);
        bottomPanel.add(finalizePaymentBtn);

        return bottomPanel;
    }

    /**
     * Reads selected item quantities and opens PaymentPanel
     */
    private void openPaymentPanel() {
        List<PaymentPanel.OrderItem> orderList = new ArrayList<>();

        for (MachineItem item : vendingMachine.getItems()) {
            JTextField input = quantityInputs.get(item.getName());

            if (input != null) {
                try {
                    int qty = Integer.parseInt(input.getText().trim());
                    if (qty > 0) {
                        for (int i = 0; i < qty; i++) {
                            orderList.add(new PaymentPanel.OrderItem(item.getName(), item.getPrice()));
                        }
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        if (orderList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one item before proceeding to payment.",
                    "No Items Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Launch PaymentPanel with constructed order list
        PaymentPanel paymentWindow = new PaymentPanel(vendingMachine, orderList);
        paymentWindow.setVisible(true);

        paymentWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (paymentWindow.isPaymentSuccessful())
                {
                    for (int i = 0; i < vendingMachine.getItems().size(); i++) {
                        MachineItem item = vendingMachine.getItems().get(i); // this only takes away items in stock checklist for SpecialMachine
                        JTextField input = quantityInputs.get(item.getName());

                        if (input != null) {
                            try {
                                int qty = Integer.parseInt(input.getText().trim());
                                // Dispense the item for each quantity purchased
                                for (int j = 0; j < qty; j++) {
                                    vendingMachine.dispenseItem(i, item.getPrice());
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                    }

                    getContentPane().removeAll();
                    JLabel headerLabel = new JLabel("Simple Vending Machine");
                    headerLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 26));
                    headerLabel.setForeground(MOCHA_TEXT);
                    headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
                    add(headerLabel, BorderLayout.NORTH);
                    add(createProductGridPanel(), BorderLayout.CENTER);
                    add(createBottomPanel(), BorderLayout.SOUTH);
                    revalidate();
                    repaint();
                }
            }
        });
    }
}