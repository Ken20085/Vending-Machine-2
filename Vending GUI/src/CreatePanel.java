import javax.swing.*;
import java.awt.*;

public class CreatePanel extends JPanel {

    public CreatePanel()
    {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.decode("#1e1e2e"));
        GridBagConstraints c = new GridBagConstraints();

        // TITLE
        JPanel titlePanel = new JPanel(new GridBagLayout());
        titlePanel.setBackground(Color.decode("#181825"));
        GridBagConstraints titleC = new GridBagConstraints();
        titleC.fill = GridBagConstraints.HORIZONTAL;
        titleC.weightx = 1.0;
        titleC.insets = new Insets(10,10,10,10);

        JLabel titleLabel = new JLabel("Create Vending Machine");
        titleLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        titlePanel.add(titleLabel, titleC);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10,10,0,10);
        this.add(titlePanel, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;

        this.add(configPanel(), c);
    }

    private JPanel configPanel()
    {
        // CONFIGURATION SETUP
        JPanel configPanel = new JPanel(new GridBagLayout());
        GridBagConstraints configC = new GridBagConstraints();
        configPanel.setBackground(Color.decode("#1e1e2e"));

        configPanel.add(sidePanel(configC), configC);

        // --- CONTENT ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.decode("#181825"));
        GridBagConstraints contentC = new GridBagConstraints();

        JLabel contentLabel = new JLabel("Create Vending Machine");
        contentLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
        contentLabel.setForeground(Color.WHITE);

        contentC.insets = new Insets(10,10,10,10);
        contentC.gridx = 0;
        contentC.gridy = 0;
        contentC.weightx = 1.0;
        contentC.weighty = 1.0;
        contentC.anchor = GridBagConstraints.NORTH;

        contentPanel.add(contentLabel, contentC);

        configC.gridx = 1;
        configC.weightx = 1.0;
        configC.fill = GridBagConstraints.BOTH;
        configC.insets = new Insets(0, 10, 10, 0);

        configPanel.add(contentPanel, configC);

        return configPanel;
    }

    private JPanel sidePanel(GridBagConstraints configC)
    {
        // --- SIDE BAR ---
        JPanel sideBar = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(5,10,5,10);
        c2.gridx = 0;
        c2.gridy = 0;
        c2.weightx = 1.0;
        c2.weighty = 0.0;
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.anchor = GridBagConstraints.PAGE_START;

        sideBar.setBackground(Color.decode("#181825"));
        sideBar.setPreferredSize(new Dimension(200,0));

        // button
        sideBar.add(createButton(), c2);

        c2.gridy = 1;
        c2.weighty = 1.0;
        sideBar.add(Box.createVerticalGlue(), c2);

        configC.gridx = 0;
        configC.gridy = 0;
        configC.weightx = 0.0;
        configC.weighty = 1.0;
        configC.anchor = GridBagConstraints.WEST;
        configC.fill = GridBagConstraints.VERTICAL;
        configC.insets = new Insets(0,0,10,0);

        return sideBar;
    }

    private JButton createButton()
    {
        JButton button = new JButton();

        Color color =  Color.decode("#89dceb");

        // Styling
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(color, 2));
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        button.setForeground(color);

        button.setText("Select Type");

        Dimension size = new Dimension(250, 50);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setForeground(Color.decode("#4c4f69"));
                button.setContentAreaFilled(true); // Fill background on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setContentAreaFilled(false);
                button.setForeground(color);
            }
        });

        return button;
    }
}
