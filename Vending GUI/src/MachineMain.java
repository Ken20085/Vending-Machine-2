import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MachineMain extends JFrame {

    // --- Catppuccin Mocha Color Palette ---
    private static final Color MOCHA_BASE     = Color.decode("#1e1e2e");
    private static final Color MOCHA_MANTLE   = Color.decode("#181825");
    private static final Color MOCHA_CRUST    = Color.decode("#11111b");
    private static final Color MOCHA_SURFACE0 = Color.decode("#313244");
    private static final Color MOCHA_SURFACE1 = Color.decode("#45475a");
    private static final Color MOCHA_TEXT     = Color.decode("#cdd6f4");
    private static final Color MOCHA_SUBTEXT  = Color.decode("#a6adc8");
    private static final Color MOCHA_GREEN    = Color.decode("#a6e3a1");
    private static final Color MOCHA_RED      = Color.decode("#f38ba8");

    // --- State Management & Layout Components ---
    private double currentMultiplier = 1.0;
    private final List<ScalableItemCard> scalableCards = new ArrayList<>();
    private JPanel mainPanel; // Field accessible by getSelectedItems()

    public MachineMain() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(MOCHA_BASE);
        this.setSize(950, 700);
        this.setLocationRelativeTo(null);
        this.setTitle("Pizza Ordering Kiosk");

        this.setLayout(new BorderLayout());

        this.mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        this.mainPanel.setOpaque(false);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 20, 10, 20);

        for (int i = 0; i < 6; i++) {
            c.gridy = i;
            this.mainPanel.add(createSectionPanel(i), c);
        }

        // Vertical spacer pushing content up
        GridBagConstraints spacerConstraints = new GridBagConstraints();
        spacerConstraints.gridx = 0;
        spacerConstraints.gridy = 10;
        spacerConstraints.weighty = 1.0;
        spacerConstraints.fill = GridBagConstraints.VERTICAL;
        this.mainPanel.add(Box.createGlue(), spacerConstraints);

        // --- JSCROLLPANE SETUP ---
        JScrollPane scrollPane = new JScrollPane(this.mainPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

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
        this.add(createBottomPanel(), BorderLayout.SOUTH);
    }

    // =========================================================================
    // SECTION PANEL & ORDER EXTRACTION LOGIC
    // =========================================================================

    private static class SectionPanel extends JPanel {
        private JPanel selectedSingleCard = null;
        private final boolean isMultiSelect;

        public SectionPanel(boolean isMultiSelect) {
            super(new BorderLayout(0, 15));
            this.isMultiSelect = isMultiSelect;
            this.setOpaque(false);
        }

        public void handleCardClick(JPanel clickedCard) {
            if (isMultiSelect) {
                // Multi-selection behavior (Toppings)
                boolean isSelected = (Boolean) clickedCard.getClientProperty("IS_SELECTED");
                isSelected = !isSelected;
                clickedCard.putClientProperty("IS_SELECTED", isSelected);
                highlightCard(clickedCard, isSelected);
            } else {
                // Single-selection behavior
                if (selectedSingleCard != null) {
                    highlightCard(selectedSingleCard, false);
                    selectedSingleCard.putClientProperty("IS_SELECTED", false);
                }
                selectedSingleCard = clickedCard;
                clickedCard.putClientProperty("IS_SELECTED", true);
                highlightCard(clickedCard, true);
            }
        }

        public void setInitialSelection(JPanel card) {
            selectedSingleCard = card;
            card.putClientProperty("IS_SELECTED", true);
            highlightCard(card, true);
        }
    }

    private static class ScalableItemCard {
        double basePrice;
        JLabel priceLabel;
        JPanel card;

        ScalableItemCard(double basePrice, JLabel priceLabel, JPanel card) {
            this.basePrice = basePrice;
            this.priceLabel = priceLabel;
            this.card = card;
        }

        void updatePrice(double multiplier) {
            if (basePrice == 0) {
                priceLabel.setText("Free");
                card.putClientProperty("ITEM_PRICE", 0.0);
            } else {
                double scaled = basePrice * multiplier;
                priceLabel.setText(String.format("Php %.2f", scaled));
                card.putClientProperty("ITEM_PRICE", scaled);
            }
        }
    }

    /**
     * Traverses section panels to gather selected item names and current scaled prices.
     */
    public List<PaymentPanel.OrderItem> getSelectedItems() {
        List<PaymentPanel.OrderItem> selectedItems = new ArrayList<>();

        if (mainPanel == null) {
            return selectedItems;
        }

        for (Component comp : mainPanel.getComponents()) {
            if (comp instanceof SectionPanel sectionPanel) {
                for (Component subComp : sectionPanel.getComponents()) {
                    if (subComp instanceof JPanel optionsGrid) {
                        for (Component cardComp : optionsGrid.getComponents()) {
                            if (cardComp instanceof JPanel card) {
                                Boolean isSelected = (Boolean) card.getClientProperty("IS_SELECTED");

                                if (Boolean.TRUE.equals(isSelected)) {
                                    String itemName = (String) card.getClientProperty("ITEM_NAME");
                                    Double itemPrice = (Double) card.getClientProperty("ITEM_PRICE");

                                    if (itemName != null && itemPrice != null) {
                                        selectedItems.add(new PaymentPanel.OrderItem(itemName, itemPrice));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return selectedItems;
    }

    /**
     * Section Panel holding Category Title & Grid of Item Cards
     */
    private SectionPanel createSectionPanel(int sectionIndex) {
        boolean isMultiSelect = (sectionIndex == 4); // Toppings section allows multi-select
        SectionPanel sectionPanel = new SectionPanel(isMultiSelect);

        JLabel titleLabel = label(sectionIndex);
        sectionPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel optionsGrid = new JPanel(new GridLayout(0, 5, 12, 12));
        optionsGrid.setOpaque(false);

        switch (sectionIndex) {
            case 0 -> {
                // Section 1: Pizza Size
                String[] pizzaSizes = {"Personal", "Small", "Medium", "Large", "Extra Large"};
                double[] basePrices = {80.0, 100.0, 130.0, 190.0, 250.0};
                double[] scaleFactors = {1.0, 1.25, 1.5, 1.75, 2.0};

                for (int j = 0; j < pizzaSizes.length; j++) {
                    double factor = scaleFactors[j];
                    JPanel card = createOptionItemPanel(sectionPanel, pizzaSizes[j], String.format("Php %.2f", basePrices[j]), basePrices[j], false);

                    if (j == 0) {
                        sectionPanel.setInitialSelection(card);
                    }

                    card.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            currentMultiplier = factor;
                            for (ScalableItemCard item : scalableCards) {
                                item.updatePrice(currentMultiplier);
                            }
                        }
                    });

                    optionsGrid.add(card);
                }
            }
            case 1 -> {
                // Section 2: How many cuts
                String[] cutOptions = {"4 cuts", "6 cuts", "8 cuts", "Square Cuts"};
                for (String option : cutOptions) {
                    optionsGrid.add(createOptionItemPanel(sectionPanel, option, "Free", 0.0, false));
                }
            }
            case 2 -> {
                // Section 3: Sauces
                String[] sauceOptions = {"No Sauce", "Marinara", "Garlic", "Pesto", "White"};
                double[] baseSaucePrices = {5.0, 6.0, 7.0, 10.0, 13.0};

                for (int i = 0; i < sauceOptions.length; i++) {
                    optionsGrid.add(createOptionItemPanel(sectionPanel, sauceOptions[i], "", baseSaucePrices[i], true));
                }
            }
            case 3 -> {
                // Section 4: Cheese Options
                String[] cheeseOptions = {"No Cheese", "Include Cheese"};
                for (String option : cheeseOptions) {
                    optionsGrid.add(createOptionItemPanel(sectionPanel, option, "Free", 0.0, false));
                }
            }
            case 4 -> {
                // Section 5: Toppings
                String[] toppings = {"Pepperoni", "Mushroom", "Pineapple", "Ham", "Beef mince", "Basil leaves", "Onion"};
                double[] baseToppingPrices = {10.0, 10.0, 10.0, 10.0, 20.0, 10.0, 10.0};

                for (int i = 0; i < toppings.length; i++) {
                    optionsGrid.add(createOptionItemPanel(sectionPanel, toppings[i], "", baseToppingPrices[i], true));
                }
            }
            case 5 -> {
                // Section 6: Extras
                String[] extrasOptions = {"Thin Crust", "Cheese Crust"};
                double[] baseExtraPrices = {40.0, 60.0};

                for (int i = 0; i < extrasOptions.length; i++) {
                    optionsGrid.add(createOptionItemPanel(sectionPanel, extrasOptions[i], "", baseExtraPrices[i], true));
                }
            }
        }

        sectionPanel.add(optionsGrid, BorderLayout.CENTER);
        return sectionPanel;
    }

    /**
     * Creates item card with metadata properties attached for selection extraction.
     */
    private JPanel createOptionItemPanel(SectionPanel parentSection, String name, String staticPriceText, double basePrice, boolean isScalable) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(MOCHA_MANTLE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Attachment of state & selection metadata
        card.putClientProperty("IS_SELECTED", false);
        card.putClientProperty("ITEM_NAME", name);

        highlightCard(card, false);

        // 1. Inner Square Box
        JPanel imageBox = new JPanel(new GridBagLayout());
        imageBox.setBackground(MOCHA_CRUST);
        imageBox.setPreferredSize(new Dimension(120, 120));
        imageBox.setMaximumSize(new Dimension(120, 120));
        imageBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel iconLabel = new JLabel("🍕");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        imageBox.add(iconLabel);

        // 2. Name Label
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
        nameLabel.setForeground(MOCHA_TEXT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Price Label
        JLabel priceLabel = new JLabel();
        priceLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
        priceLabel.setForeground(MOCHA_SUBTEXT);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (isScalable) {
            ScalableItemCard itemCard = new ScalableItemCard(basePrice, priceLabel, card);
            itemCard.updatePrice(currentMultiplier);
            scalableCards.add(itemCard);
        } else {
            priceLabel.setText(staticPriceText);
            card.putClientProperty("ITEM_PRICE", basePrice);
        }

        card.add(imageBox);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(priceLabel);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentSection.handleCardClick(card);
            }
        });

        return card;
    }

    private static void highlightCard(JPanel card, boolean isSelected) {
        if (isSelected) {
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MOCHA_GREEN, 2),
                    BorderFactory.createEmptyBorder(11, 11, 11, 11)
            ));
        } else {
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(MOCHA_SURFACE0, 1),
                    BorderFactory.createEmptyBorder(12, 12, 12, 12)
            ));
        }
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(MOCHA_MANTLE);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, MOCHA_SURFACE0));

        JButton btnCancel = new JButton("Cancel");
        styleButton(btnCancel, MOCHA_RED, MOCHA_BASE);

        // --- CLOSE JFRAME ON CANCEL ---
        btnCancel.addActionListener(e -> this.dispose());

        JButton btnNext = new JButton("Checkout");
        styleButton(btnNext, MOCHA_GREEN, MOCHA_BASE);

        // Launch PaymentPanel on click
        btnNext.addActionListener(e -> {
            List<PaymentPanel.OrderItem> currentOrder = getSelectedItems();
            PaymentPanel paymentPanel = new PaymentPanel(currentOrder);
            paymentPanel.setVisible(true);
        });

        bottomPanel.add(btnCancel);
        bottomPanel.add(btnNext);

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

    private JLabel label(int i) {
        String textName = switch (i) {
            case 0 -> "1. Pick your size of pizza";
            case 1 -> "2. How many cuts";
            case 2 -> "3. What sauce do you want";
            case 3 -> "4. Do you want cheese";
            case 4 -> "5. What toppings do you want";
            case 5 -> "6. Extras";
            default -> "";
        };

        JLabel label = new JLabel(textName);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 22));
        label.setForeground(MOCHA_TEXT);

        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MachineMain frame = new MachineMain();
            frame.setVisible(true);
        });
    }
}