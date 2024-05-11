package net.runelite.client.plugins.playerlogger;

import net.runelite.client.ui.PluginPanel;
import javax.swing.*;
import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlayerLoggerPanel extends PluginPanel {
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final List<PlayerDetails> players = new ArrayList<>();
    private final PlayerLoggerPlugin plugin;  // Reference to the plugin

    public PlayerLoggerPanel(PlayerLoggerPlugin plugin) {
        this.plugin = plugin;  // Store the plugin reference
        setLayout(new BorderLayout());

        JList<String> playerList = new JList<>(listModel);
        playerList.setCellRenderer(new PlayerListCellRenderer());

        JScrollPane listScroller = new JScrollPane(playerList);
        listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(listScroller, BorderLayout.CENTER);

        JButton clearLogsButton = new JButton("Clear Logs");
        clearLogsButton.addActionListener(e -> clearLogs());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(clearLogsButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void updatePlayers(List<PlayerDetails> newPlayers) {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            players.clear();
            players.addAll(newPlayers);
            players.sort(Comparator.comparing(PlayerDetails::getFirstSeen).reversed());

            players.forEach(player -> {
                listModel.addElement(getPlayerInfo(player));
            });
        });
    }

    private void clearLogs() {
        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            players.clear();
        });
    }

    private String getPlayerInfo(PlayerDetails player) {
        String formattedTime = DateTimeFormatter.ofPattern("HH:mm:ss")
                .withZone(ZoneId.systemDefault())
                .format(player.getFirstSeen());
        return String.format("%s : %d : W%d : %s", player.getName(), player.getCombatLevel(), player.getWorld(), formattedTime);
    }

    private static class PlayerListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setText((String)value);
            return this;
        }
    }
}
