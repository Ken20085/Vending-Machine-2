import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class UserInterface extends JFrame implements Refreshable{

    private final CardLayout panels = new CardLayout();
    private final JPanel mainPanel = new JPanel(panels);

    private RegularVM vendingMachine;
    private SpecialMachine specialMachine;

    // Instance variables exposed to the Controller
    private final CreatePanel createPanel = new CreatePanel();
    private final ModifyPanel modifyPanel = new ModifyPanel();

    public UserInterface()
    {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.decode("#292929"));
        this.setSize(1024,728);
        this.setLocationRelativeTo(null);
        this.setTitle("Vending Machine");

        JPanel menuPanel = MakeMenuPanel();

        // FIX: Use the class-level instance variables instead of redeclaring local ones
        mainPanel.add(menuPanel, "menuPanel");
        mainPanel.add(this.createPanel, "createPanel");
        mainPanel.add(this.modifyPanel, "modifyPanel");

        this.add(mainPanel);
        this.setVisible(true);

        refresh();
    }

    private JPanel MakeMenuPanel()
    {
        Path path = Path.of("Pizza.png");
        ImageIcon originalIcon = new ImageIcon("src/Pizza.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(
                200, 200, java.awt.Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel iconImage = new JLabel(scaledIcon);
        this.setIconImage(scaledImage);

        JLabel titleFrame = new JLabel("Welcome to Pizza Making Factory");
        titleFrame.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        titleFrame.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,10,10,10);
        c.anchor = GridBagConstraints.WEST;
        buttonPanel.setBackground(Color.decode("#1e1e2e"));

        for (int i = 0; i < 3; i++)
        {
            String menuName = "";
            switch (i)
            {
                case 0 -> menuName = "createPanel";
                case 1 -> menuName = "modifyPanel";
                case 2 -> menuName = "testPanel";
            }
            buttonPanel.add(createMenuButton(i, menuName), c);
        }

        JLabel versionText = new JLabel("projectpizza v2.0");
        versionText.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
        versionText.setForeground(Color.decode("#45475a"));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.insets = new Insets(10,10,10,10);
        c2.anchor = GridBagConstraints.CENTER;
        c2.gridx = 0;

        panel.setBackground(Color.decode("#1e1e2e"));
        panel.add(iconImage, c2);
        panel.add(titleFrame, c2);
        panel.add(buttonPanel, c2);
        panel.add(versionText, c2);

        return panel;
    }

    private JButton createMenuButton(int i, String menuName)
    {
        JButton button = new JButton();
        button.setFocusable(false);

        ArrayList<String> buttonNames = new ArrayList<>();
        buttonNames.add("Create Vending Machine");
        buttonNames.add("Modify Vending Machine");
        buttonNames.add("Test Vending Machine");

        button.setText(Helper.setButtonName(i, buttonNames));

        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.decode("#f2cdcd"), 2));
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 13));
        button.setForeground(Color.decode("#f2cdcd"));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode("#f2cdcd"));
                button.setForeground(Color.decode("#4c4f69"));
                button.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setContentAreaFilled(false);
                button.setForeground(Color.decode("#f2cdcd"));
            }
        });

        button.addActionListener(e -> {
            if ("testPanel".equals(menuName)) {
                if (vendingMachine != null) {
                    SimpleVendingMachineFrame machineFrame = new SimpleVendingMachineFrame(vendingMachine);
                    machineFrame.setVisible(true);
                } else if (specialMachine != null) {
                    MachineMain machineMain = new MachineMain(specialMachine);
                    machineMain.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Please create a vending machine first!", "No Machine", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                panels.show(mainPanel, menuName);
            }
        });

        Dimension size = new Dimension(250, 50);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);

        return button;
    }

    public CreatePanel getCreatePanel() {
        return createPanel;
    }

    public ModifyPanel getModifyPanel() {
        return modifyPanel;
    }

    @Override
    public void refresh() {}

    public void updateMachineData(RegularVM regularVM, SpecialMachine specialVM) {
        this.vendingMachine = regularVM;
        this.specialMachine = specialVM;
    }
}