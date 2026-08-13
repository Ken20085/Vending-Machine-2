import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProcessPanel extends JFrame {

    // --- Catppuccin Mocha Color Palette ---
    private static final Color MOCHA_BASE     = Color.decode("#1e1e2e");
    private static final Color MOCHA_MANTLE   = Color.decode("#181825");
    private static final Color MOCHA_TEXT     = Color.decode("#cdd6f4");

    private JTextArea consoleArea;
    private Timer timer;
    private int currentIndex = 0;
    private final ArrayList<String> processSteps;

    public ProcessPanel(ArrayList<String> steps) {
        this.processSteps = (steps != null && !steps.isEmpty())
                ? steps
                : new ArrayList<>(java.util.List.of("Processing..."));

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(MOCHA_BASE);
        this.setSize(950, 700);
        this.setLocationRelativeTo(null);
        this.setTitle("Order Processing Console");

        this.setLayout(new BorderLayout());

        // Console-style Text Area
        consoleArea = new JTextArea();
        consoleArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 15));
        consoleArea.setBackground(MOCHA_MANTLE);
        consoleArea.setForeground(MOCHA_TEXT);
        consoleArea.setEditable(false);
        consoleArea.setMargin(new Insets(25, 25, 25, 25));

        JScrollPane scrollPane = new JScrollPane(consoleArea);
        scrollPane.getViewport().setBackground(MOCHA_MANTLE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        this.add(scrollPane, BorderLayout.CENTER);

        // Timer to print text line-by-line (600ms delay per line for smooth terminal feedback)
        timer = new Timer(600, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < processSteps.size()) {
                    consoleArea.append(processSteps.get(currentIndex) + "\n");
                    // Auto-scroll to the bottom of the console log
                    consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
                    currentIndex++;
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }
}