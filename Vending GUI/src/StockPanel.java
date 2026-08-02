import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StockPanel extends JPanel implements ActionListener {

    // Uncomment for info
    // SpecialVendingMachine machine;

    private JComboBox<String> steps;

    public StockPanel()
    {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.decode("#1e1e2e"));
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(20,20,20,20);
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;

        DropDown();

        // TITlE PAGE
        this.add(ItemLabel("Item Stock"), c);
        this.add(steps, c);
    }

    public JPanel ItemBox()
    {
        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#f5e0dc"));
        panel.setPreferredSize(new Dimension(400,400));

        return panel;
    }

    private JLabel ItemLabel(String labelName)
    {
        JLabel contentLabel = new JLabel(labelName);
        contentLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
        contentLabel.setForeground(Color.decode("#f2cdcd"));

        return contentLabel;
    }

    private void DropDown()
    {
        ArrayList<String> items = new ArrayList<>();
        items.add("Stock");
        items.add("Item");
        items.add("Stock2");

        steps = new JComboBox(items.toArray(new String[0]));
        steps.addActionListener(this);
        steps.setPreferredSize(new Dimension(400,40));

        steps.setUI(new CustomComboBoxUI());
    }

    class CustomComboBoxUI extends BasicComboBoxUI {

        // Hex colors matching the provided design
        private final Color mainPink = Color.decode("#EC809E");
        private final Color buttonPink = Color.decode("#F9C1C1");

        @Override
        protected void installDefaults() {
            super.installDefaults();
            comboBox.setBackground(mainPink);
        }

        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton() {
                @Override
                public void paintComponent(Graphics g) {
                    g.setColor(buttonPink);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            // Explicitly strip all border logic from the arrow button
            button.setBorder(null);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            return button;
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == steps)
        {
            System.out.println(steps.getSelectedItem().toString());
        }
    }
}
