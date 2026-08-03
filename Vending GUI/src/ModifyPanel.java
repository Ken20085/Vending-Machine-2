import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class ModifyPanel extends JPanel {

    private final CardLayout configPanels = new CardLayout();
    private final JPanel configHolder = new JPanel(configPanels);

    // Track active selection styling
    private JButton currentlySelectedButton = null;
    private final Color defaultBg = Color.decode("#181825");
    private final Color highlightColor = Color.decode("#f2cdcd");
    private final Color highlightTextColor = Color.decode("#4c4f69");

    private final DashboardPanel dashboardPanel = new DashboardPanel();
    private final StockPanel stockPanel = new StockPanel();

    public ModifyPanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.decode("#1e1e2e"));
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        JPanel config = configPanel();
        this.add(config, c);
    }

    private JPanel configPanel()
    {
        // CONFIGURATION SETUP
        JPanel configPanel = new JPanel(new GridBagLayout());
        GridBagConstraints configC = new GridBagConstraints();
        configPanel.setBackground(Color.decode("#1e1e2e"));

        configPanel.add(sidePanel(configC), configC);

        // --- REGISTER PANELS IN CARDLAYOUT ---
        configHolder.add(createPlaceholderPanel("Select a menu to continue"), "SelectMenu");
        configHolder.add(dashboardPanel, "Dashboard");
        configHolder.add(stockPanel, "Item Stock");

        // --- ADD CASH REGISTER PANEL HERE ---
        configHolder.add(new CashRegisterPanel(), "Cash Register");

        configHolder.add(createPlaceholderPanel("History View"), "History");

        // Set initial view to the blank/placeholder panel
        configPanels.show(configHolder, "SelectMenu");

        configC.gridx = 1;
        configC.weightx = 1.0;
        configC.fill = GridBagConstraints.BOTH;
        configC.insets = new Insets(0, 0, 0, 0);

        configPanel.add(configHolder, configC);

        return configPanel;
    }

    private JPanel sidePanel(GridBagConstraints configC)
    {
        // --- SIDE BAR ---
        JPanel sideBar = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 0;
        c2.weightx = 1.0;
        c2.weighty = 0.0;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.anchor = GridBagConstraints.PAGE_START;

        sideBar.setBackground(defaultBg);
        sideBar.setPreferredSize(new Dimension(250,0));

        // Side Navigation Buttons
        sideBar.add(createButton("Dashboard", setIcon("Dashboard"), "Dashboard"), c2);

        c2.gridy = 1;
        sideBar.add(createButton("Item Stock", setIcon("ItemStock"), "Item Stock"), c2);

        c2.gridy = 2;
        sideBar.add(createButton("Cash Register", setIcon("CashRegister"), "Cash Register"), c2);

        c2.gridy = 3;
        sideBar.add(createButton("History", setIcon("History"), "History"), c2);

        c2.gridy = 4;

        // Icon setup
        Path path = Path.of("Pizza.png");
        ImageIcon originalIcon = new ImageIcon("src/UserInterfaceUtils/Pizza.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // --- RETURN TO USER INTERFACE MENU ---
        JButton returnBtn = createButton("Return to menu", scaledIcon, null);
        returnBtn.addActionListener(e -> {
            // Reset active button highlight when navigating away
            setSelectedButton(null);
            configPanels.show(configHolder, "SelectMenu");

            Container parent = this.getParent();
            if (parent != null && parent.getLayout() instanceof CardLayout parentLayout) {
                parentLayout.show(parent, "menuPanel");
            }
        });

        sideBar.add(returnBtn, c2);

        c2.gridy = 5;
        c2.weighty = 1.0;
        c2.fill = GridBagConstraints.BOTH;
        sideBar.add(Box.createVerticalGlue(), c2);

        configC.gridx = 0;
        configC.gridy = 0;
        configC.weightx = 0.0;
        configC.weighty = 1.0;
        configC.anchor = GridBagConstraints.WEST;
        configC.fill = GridBagConstraints.VERTICAL;
        configC.insets = new Insets(0,0,0,0);

        return sideBar;
    }

    private JButton createButton(String name, ImageIcon icon, String cardName)
    {
        JButton button = new JButton();

        // Styling
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 15));
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        button.setForeground(highlightColor);

        button.setIconTextGap(12);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setIcon(icon);
        button.setText(name);

        Dimension size = new Dimension(250, 50);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);

        if (cardName != null) {
            button.addActionListener(e -> {
                setSelectedButton(button);

                // --- REFRESH LOGIC ---
                // If the panel implements Refreshable, refresh it before showing
                if ("Dashboard".equals(cardName)) {
                    dashboardPanel.refresh();
                } else if ("Item Stock".equals(cardName)) {
                    stockPanel.refresh();
                }

                configPanels.show(configHolder, cardName);
            });
        }

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != currentlySelectedButton) {
                    button.setBackground(highlightColor);
                    button.setForeground(highlightTextColor);
                    button.setContentAreaFilled(true);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != currentlySelectedButton) {
                    button.setContentAreaFilled(false);
                    button.setForeground(highlightColor);
                }
            }
        });

        return button;
    }

    private void setSelectedButton(JButton button) {
        // Reset previously selected button visual state
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setContentAreaFilled(false);
            currentlySelectedButton.setForeground(highlightColor);
        }

        // Apply visual state to newly selected button
        currentlySelectedButton = button;
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setBackground(highlightColor);
            currentlySelectedButton.setForeground(highlightTextColor);
            currentlySelectedButton.setContentAreaFilled(true);
        }
    }

    private ImageIcon setIcon(String iconName)
    {
        ImageIcon originalIcon = new ImageIcon("src/" + iconName + ".png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                20,
                20,
                java.awt.Image.SCALE_SMOOTH);

        return new ImageIcon(scaledImage);
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.decode("#1e1e2e"));
        JLabel label = new JLabel(text);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        label.setForeground(Color.decode("#4c4f69"));
        panel.add(label);
        return panel;
    }
}