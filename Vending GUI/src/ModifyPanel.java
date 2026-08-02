import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class ModifyPanel extends JPanel {

    private final CardLayout configPanels = new CardLayout();
    private final JPanel configHolder = new JPanel(configPanels);

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

        // --- CONTENT ---
//        JPanel contentPanel = new JPanel(new GridBagLayout());
//        contentPanel.setBackground(Color.decode("#1e1e2e"));
//        GridBagConstraints contentC = new GridBagConstraints();
//
//        JLabel contentLabel = new JLabel("Select a menu to continue");
//        contentLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
//        contentLabel.setForeground(Color.decode("#4c4f69"));
//
//        contentC.insets = new Insets(10,10,0,10);
//        contentC.gridx = 0;
//        contentC.gridy = 0;
//        contentC.weightx = 1.0;
//        contentC.weighty = 1.0;
//        contentC.anchor = GridBagConstraints.CENTER;
//
//        contentPanel.add(contentLabel, contentC);

        // Add to config holder code
        configHolder.add(new StockPanel(), "startConfig");

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

        sideBar.setBackground(Color.decode("#181825"));
        sideBar.setPreferredSize(new Dimension(250,0));

        Path path = Path.of("Pizza.png");
        ImageIcon originalIcon =  new ImageIcon("src/Pizza.png");
        Image scaledImage =  originalIcon.getImage().getScaledInstance(
                20,
                20,
                java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        sideBar.add(createButton("Dashboard", setIcon("Dashboard")), c2);

        c2.gridy = 1;
        sideBar.add(createButton("Item Stock", setIcon("ItemStock")), c2);

        c2.gridy = 2;
        sideBar.add(createButton("Cash Register", setIcon("CashRegister")), c2);

        c2.gridy = 3;
        sideBar.add(createButton("History", setIcon("History")), c2);

        c2.gridy = 4;
        sideBar.add(createButton("Return to menu", scaledIcon), c2);

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

    private JButton createButton(String name, ImageIcon icon)
    {
        JButton button = new JButton();

        Color color =  Color.decode("#f2cdcd");

        // Styling
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 15));
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        button.setForeground(color);

        button.setIconTextGap(12);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setIcon(icon);
        button.setText(name);

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

    private ImageIcon setIcon(String iconName)
    {
        Path path = Path.of(iconName + ".png");
        ImageIcon originalIcon =  new ImageIcon("src/" + iconName + ".png");
        Image scaledImage =  originalIcon.getImage().getScaledInstance(
                20,
                20,
                java.awt.Image.SCALE_SMOOTH);


        return new ImageIcon(scaledImage);
    }
}
