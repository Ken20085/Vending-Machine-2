import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class GridUtils {

    private static final int MAX_COLS = 5;
    private static Component currentSpacer = null; // Track glue instance

    public static JScrollPane createScrollableGrid(JPanel gridPanel) {
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.setBackground(Color.decode("#1e1e2e"));

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Apply the custom scrollbar UI
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0)); // Set custom width

        scrollPane.getViewport().setBackground(Color.decode("#1e1e2e"));

        return scrollPane;
    }

    public static void addItemToGrid(JPanel container, Component item, int index) {
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = index % MAX_COLS;
        c.gridy = index / MAX_COLS;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(8, 8, 8, 8);
        c.weightx = 1.0;

        container.add(item, c);

        updateGridSpacer(container, (index / MAX_COLS) + 1);

        container.revalidate();
        container.repaint();
    }

    private static void updateGridSpacer(JPanel container, int nextRow) {
        // Remove existing glue before adding a new one
        if (currentSpacer != null) {
            container.remove(currentSpacer);
        }

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = nextRow;
        c.gridwidth = MAX_COLS;
        c.weightx = 1.0;
        c.weighty = 1.0; // Captures vertical space below rows

        currentSpacer = Box.createGlue();
        container.add(currentSpacer, c);
    }

    public static class CustomScrollBarUI extends BasicScrollBarUI {

        private final Color customTrackColor = Color.decode("#181825");
        private final Color customThumbColor = Color.decode("#585b70");
        private final Color customHoverColor = Color.decode("#f5e0dc");

        // Hide top/bottom arrow buttons
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        // Draw track
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(customTrackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }

        // Draw thumb handle
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isThumbRollover()) {
                g2.setColor(customHoverColor);
            } else {
                g2.setColor(customThumbColor);
            }

            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }
    }
}