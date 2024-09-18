package net.runelite.client.plugins.usernotifier;

import net.runelite.client.ui.PluginPanel;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class UserNotifierPanel extends PluginPanel {
    private final UserNotifierPlugin plugin;
    private final JTextArea inputArea;

    public UserNotifierPanel(UserNotifierPlugin plugin) {
        this.plugin = plugin;

        setLayout(new BorderLayout());

        // Text area for inputting usernames and reasons
        inputArea = new JTextArea(10, 20);
        JScrollPane scrollPane = new JScrollPane(inputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Button to save input
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveUserReasons());
        add(saveButton, BorderLayout.SOUTH);
    }

    private void saveUserReasons() {
        String inputText = inputArea.getText();
        String[] lines = inputText.split("\\n");
        Map<String, String> updatedUserReasons = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts.length == 2) {
                updatedUserReasons.put(parts[0].trim(), parts[1].trim());
            }
        }

        // Update the plugin with the new user reasons
        plugin.updateUserReasons(updatedUserReasons);
    }
}
