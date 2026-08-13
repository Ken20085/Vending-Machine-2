import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreatePanel extends JPanel {

    // --- Catppuccin Mocha Color Palette ---
    private final Color MOCHA_BASE      = Color.decode("#1e1e2e");
    private final Color MOCHA_SURFACE0  = Color.decode("#313244");
    private final Color MOCHA_SURFACE1  = Color.decode("#45475a");
    private final Color MOCHA_TEXT      = Color.decode("#cdd6f4");
    private final Color MOCHA_SUBTEXT   = Color.decode("#a6adc8");
    private final Color MOCHA_ROSEWATER = Color.decode("#f5e0dc");
    private final Color MOCHA_GREEN     = Color.decode("#a6e3a1");
    private final Color MOCHA_RED       = Color.decode("#f38ba8");
    private final Color MOCHA_CRUST     = Color.decode("#11111b");

    // Form inputs
    private JTextField nameField;
    private JButton btnSimpleType;
    private JButton btnSpecialType;

    // Action buttons
    private JButton btnCreate;
    private JButton btnCancel;

    // Selected state ("Simple" or "Special")
    private String selectedType = "Simple";

    public CreatePanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(MOCHA_BASE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across both columns
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 30, 0, 30);

        // --- TITLE ---
        JLabel titleLabel = new JLabel("Create Vending Machine");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 22));
        titleLabel.setForeground(MOCHA_TEXT);
        this.add(titleLabel, gbc);

        // --- SECTION 1: NAME INPUT ---
        gbc.gridy++;
        gbc.insets = new Insets(20, 30, 5, 30);
        JLabel nameLabel = new JLabel("Vending Machine Name");
        nameLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        nameLabel.setForeground(MOCHA_SUBTEXT);
        this.add(nameLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 30, 20, 30);
        nameField = new JTextField();
        nameField.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        nameField.setBackground(MOCHA_SURFACE0);
        nameField.setForeground(MOCHA_TEXT);
        nameField.setCaretColor(MOCHA_TEXT);
        nameField.setPreferredSize(new Dimension(350, 40));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MOCHA_SURFACE1, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        this.add(nameField, gbc);

        // --- SECTION 2: TYPE SELECTION ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 30, 8, 30);
        JLabel typeLabel = new JLabel("Select Vending Machine Type");
        typeLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        typeLabel.setForeground(MOCHA_SUBTEXT);
        this.add(typeLabel, gbc);

        // Grid row for the 2 type buttons
        gbc.gridy++;
        gbc.gridwidth = 1; // Split into 2 columns
        gbc.weightx = 0.5;

        btnSimpleType = createTypeButton("Simple Machine");
        btnSpecialType = createTypeButton("Special Machine");

        // Set default active selection styling
        updateButtonSelection(btnSimpleType, true);
        updateButtonSelection(btnSpecialType, false);

        btnSimpleType.addActionListener(e -> {
            selectedType = "Simple";
            updateButtonSelection(btnSimpleType, true);
            updateButtonSelection(btnSpecialType, false);
        });

        btnSpecialType.addActionListener(e -> {
            selectedType = "Special";
            updateButtonSelection(btnSimpleType, false);
            updateButtonSelection(btnSpecialType, true);
        });

        gbc.gridx = 0;
        gbc.insets = new Insets(0, 30, 25, 7);
        this.add(btnSimpleType, gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 7, 25, 30);
        this.add(btnSpecialType, gbc);

        // --- SECTION 3: ACTION BUTTONS (CREATE & CANCEL) ---
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 30, 0, 30);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionPanel.setOpaque(false);

        btnCancel = createActionButton("Cancel", MOCHA_RED, MOCHA_CRUST);
        btnCreate = createActionButton("Create", MOCHA_GREEN, MOCHA_CRUST);

        // Cancel button: Resets form and returns to menu
        btnCancel.addActionListener(e -> {
            resetForm();
            returnToMenuPanel();
        });

        // Create button: Prints to console, resets form, and returns to menu

        actionPanel.add(btnCancel);
        actionPanel.add(btnCreate);

        this.add(actionPanel, gbc);
    }

    private JButton createTypeButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(180, 45));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                if (!isButtonSelected(button)) {
                    button.setBackground(MOCHA_SURFACE0);
                }
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                if (!isButtonSelected(button)) {
                    button.setBackground(MOCHA_BASE);
                }
            }
        });

        return button;
    }

    private JButton createActionButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return button;
    }

    private boolean isButtonSelected(JButton button) {
        if (button == btnSimpleType && "Simple".equals(selectedType)) return true;
        return button == btnSpecialType && "Special".equals(selectedType);
    }

    private void updateButtonSelection(JButton button, boolean isSelected) {
        if (isSelected) {
            button.setBackground(MOCHA_ROSEWATER);
            button.setForeground(MOCHA_CRUST);
            button.setBorder(BorderFactory.createLineBorder(MOCHA_ROSEWATER, 2));
        } else {
            button.setBackground(MOCHA_BASE);
            button.setForeground(MOCHA_ROSEWATER);
            button.setBorder(BorderFactory.createLineBorder(MOCHA_ROSEWATER, 1));
        }
    }

    /**
     * Resets input fields to default values
     */
    public void resetForm() {
        nameField.setText("");
        selectedType = "Simple";
        updateButtonSelection(btnSimpleType, true);
        updateButtonSelection(btnSpecialType, false);
    }

    /**
     * Navigation logic copied from ModifyPanel[cite: 3]
     */
    private void returnToMenuPanel() {
        Container parent = this.getParent();
        if (parent != null && parent.getLayout() instanceof CardLayout parentLayout) {
            parentLayout.show(parent, "menuPanel");
        }
    }

    // Action Listener hooks
    public void addCreateActionListener(ActionListener listener) {
        btnCreate.addActionListener(listener);
    }

    public void addCancelActionListener(ActionListener listener) {
        btnCancel.addActionListener(listener);
    }

    // Getters
    public String getVendingMachineName() {
        return nameField.getText().trim();
    }

    public String getSelectedType() {
        return selectedType;
    }

    public JButton getCreateButton() {
        return btnCreate;
    }

    public JButton getCancelButton() {
        return btnCancel;
    }
}