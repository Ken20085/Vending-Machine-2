import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;

public class HistoryPanel extends JPanel {

    // --- Catppuccin Mocha Color Palette ---
    private static final Color MOCHA_BASE     = Color.decode("#1e1e2e");
    private static final Color MOCHA_MANTLE   = Color.decode("#181825");
    private static final Color MOCHA_SURFACE1 = Color.decode("#45475a");
    private static final Color MOCHA_TEXT     = Color.decode("#cdd6f4");

    private JTextArea historyTextArea;
    private RegularVM vendingMachine;

    public HistoryPanel() {
        this.setLayout(new BorderLayout());
        this.setBackground(MOCHA_BASE);
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Vending Machine History & Reports");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 22));
        titleLabel.setForeground(MOCHA_TEXT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        // Console-style text area to display report metrics and event logs
        historyTextArea = new JTextArea();
        historyTextArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        historyTextArea.setBackground(MOCHA_MANTLE);
        historyTextArea.setForeground(MOCHA_TEXT);
        historyTextArea.setEditable(false);
        historyTextArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(historyTextArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Scrollbar styling matching your app aesthetic
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = MOCHA_SURFACE1;
                this.trackColor = MOCHA_MANTLE;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) { return createZeroButton(); }
            @Override
            protected JButton createIncreaseButton(int orientation) { return createZeroButton(); }
            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                return b;
            }
        });

        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void setVendingMachine(RegularVM machine) {
        this.vendingMachine = machine;
        refresh();
    }

    /**
     * Pulls the live formatted report string from the machine's History ledger.
     */
    public void refresh() {
        if (vendingMachine == null) {
            historyTextArea.setText("No vending machine instance linked.");
            return;
        }

        if (vendingMachine.history != null) {
            // Generates the report text dynamically using live items
            String reportText = vendingMachine.history.generateReport(vendingMachine.getItems());
            historyTextArea.setText(reportText);
        } else {
            historyTextArea.setText("History ledger not initialized for this machine instance.");
        }

        historyTextArea.setCaretPosition(0);
    }
}