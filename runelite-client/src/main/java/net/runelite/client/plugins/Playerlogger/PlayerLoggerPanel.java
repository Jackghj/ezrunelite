
package net.runelite.client.plugins.playerlogger;

import net.runelite.client.ui.PluginPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlayerLoggerPanel extends PluginPanel {
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final PlayerLoggerPlugin plugin;
    private JList<String> playerList; // Class-level variable

    public PlayerLoggerPanel(PlayerLoggerPlugin plugin) {
        this.plugin = plugin;
        setLayout(new BorderLayout());

        // Initialize the playerList
        playerList = new JList<>(listModel);
        playerList.setCellRenderer(new PlayerListCellRenderer());

        // Add the JList to a JScrollPane
        JScrollPane listScroller = new JScrollPane(playerList);
        listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Add the scroll pane to the panel, using BorderLayout.CENTER to fill the space
        add(listScroller, BorderLayout.CENTER);

        // Add the Clear Logs button
        JButton clearLogsButton = new JButton("Clear Logs");
        clearLogsButton.addActionListener(e -> clearLogs());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(clearLogsButton);
        add(buttonPanel, BorderLayout.NORTH);

        // Add a right-click context menu to the playerList
        playerList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showContextMenu(e);
                }
            }
        });
    }

    // Method to clear logs
    private void clearLogs() {
        listModel.clear();
    }

    // Method to show context menu
    private void showContextMenu(MouseEvent e) {
        int index = playerList.locationToIndex(e.getPoint());
        if (index != -1) {
            JPopupMenu contextMenu = new JPopupMenu();

            // Get the world number from the selected element
            int world = Integer.parseInt(listModel.getElementAt(index).split(":")[2].trim());

            JMenuItem copyUsernamesFromWorld = new JMenuItem("Copy Usernames from World");
            copyUsernamesFromWorld.addActionListener(ev -> copyUsernamesFromWorld(world));
            contextMenu.add(copyUsernamesFromWorld);

            JMenuItem copyUsernamesFromWorldBeforeDeath = new JMenuItem("Copy Usernames from World Before Death");
            copyUsernamesFromWorldBeforeDeath.addActionListener(ev -> copyUsernamesFromWorldBeforeDeath(world));
            contextMenu.add(copyUsernamesFromWorldBeforeDeath);

            JMenuItem copyAllInfoFromWorld = new JMenuItem("Copy All Info from World");
            copyAllInfoFromWorld.addActionListener(ev -> copyAllInfoFromWorld(world));
            contextMenu.add(copyAllInfoFromWorld);

            JMenuItem copyAllInfoFromWorldBeforeDeath = new JMenuItem("Copy All Info from World Before Death");
            copyAllInfoFromWorldBeforeDeath.addActionListener(ev -> copyAllInfoFromWorldBeforeDeath(world));
            contextMenu.add(copyAllInfoFromWorldBeforeDeath);

            contextMenu.show(playerList, e.getX(), e.getY());
        }
    }

    // Implement these methods to handle copying data from the current world
    private void copyUsernamesFromWorld(int world) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listModel.size(); i++) {
            String[] details = listModel.getElementAt(i).split(":");
            if (Integer.parseInt(details[2].trim()) == world) { // Check if world matches
                sb.append(details[0].trim()).append("\n"); // Append only username
            }
        }
        copyToClipboard(sb.toString().trim());
    }

    private void copyUsernamesFromWorldBeforeDeath(int world) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listModel.size(); i++) {
            String line = listModel.getElementAt(i);
            if (line.equals("DEATH")) break;
            String[] details = line.split(":");
            if (Integer.parseInt(details[2].trim()) == world) { // Check if world matches
                sb.append(details[0].trim()).append("\n"); // Append only username
            }
        }
        copyToClipboard(sb.toString().trim());
    }

    private void copyAllInfoFromWorld(int world) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listModel.size(); i++) {
            String[] details = listModel.getElementAt(i).split(":");
            if (Integer.parseInt(details[2].trim()) == world) { // Check if world matches
                sb.append(listModel.getElementAt(i)).append("\n");
            }
        }
        copyToClipboard(sb.toString().trim());
    }

    private void copyAllInfoFromWorldBeforeDeath(int world) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listModel.size(); i++) {
            String line = listModel.getElementAt(i);
            if (line.equals("DEATH")) break;
            String[] details = line.split(":");
            if (Integer.parseInt(details[2].trim()) == world) { // Check if world matches
                sb.append(line).append("\n");
            }
        }
        copyToClipboard(sb.toString().trim());
    }

    private void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    public void updatePlayers(List<PlayerDetails> newPlayers) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                listModel.clear();
                // Sort the newPlayers list by timestamp in descending order (newest first)
                newPlayers.sort((p1, p2) -> p2.getFirstSeen().compareTo(p1.getFirstSeen()));

                // Add each player's details to the list model
                for (PlayerDetails player : newPlayers) {
                    String playerInfo = String.format("%s: %d: %d: %s", 
                        player.getName(), 
                        player.getCombatLevel(), 
                        player.getWorld(), 
                        player.getFirstSeen().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                    );
                    listModel.addElement(playerInfo);
                }
            }
        });
    }

    // Inner class for custom cell rendering
    class PlayerListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // Check if the entry is "DEATH" to color it red
            if (value.toString().equals("DEATH")) {
                label.setForeground(Color.RED); // Set the text color to red
            } else {
                label.setForeground(Color.WHITE); // Set the text color to white
            }

            return label;
        }
    }
}
