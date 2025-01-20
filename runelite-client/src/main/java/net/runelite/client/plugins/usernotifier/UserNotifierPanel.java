package net.runelite.client.plugins.usernotifier;

import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class UserNotifierPanel extends PluginPanel {
    private final JTextArea inputArea;

    public UserNotifierPanel(UserNotifierPlugin plugin) {
        setLayout(new BorderLayout());

        // Text area for inputting usernames and reasons
        inputArea = new JTextArea(10, 20);
        inputArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(inputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Button to save input
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            Map<String, String> updatedUserReasons = parseInput();
            plugin.updateUserReasons(updatedUserReasons);
        });
        add(saveButton, BorderLayout.SOUTH);
    }

    // Update the panel with the current data, sorted by reason alphabetically
    public void updatePanel(Map<String, String> userReasons) {
        StringBuilder sb = new StringBuilder();

        // Sort by reason alphabetically
        userReasons.entrySet().stream()
            .sorted(Map.Entry.comparingByValue()) // Sort by reason (value)
            .forEach(entry -> sb.append(entry.getKey())
                                .append(": ")
                                .append(entry.getValue())
                                .append("\n"));

        inputArea.setText(sb.toString()); // Update the text area with the sorted data
    }

    // Parse the input from the text area
    private Map<String, String> parseInput() {
        Map<String, String> userReasons = new HashMap<>();
        String[] lines = inputArea.getText().split("\\n");
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                userReasons.put(parts[0].trim(), parts[1].trim());
            }
        }
        return userReasons;
    }
}
