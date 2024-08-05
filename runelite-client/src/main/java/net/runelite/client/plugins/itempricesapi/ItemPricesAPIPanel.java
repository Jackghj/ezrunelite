package net.runelite.client.plugins.itempricesapi;

import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ItemPricesAPIPanel extends PluginPanel {
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final ItemPricesAPIPlugin plugin;
    private final JTextField searchField = new JTextField();

    public ItemPricesAPIPanel(ItemPricesAPIPlugin plugin) {
        this.plugin = plugin;
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        searchField.addActionListener(e -> updateSearch());
        add(searchField, BorderLayout.NORTH);

        JList<String> priceList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(priceList);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Now");
        refreshButton.addActionListener(e -> plugin.fetchLatestPrices());
        add(refreshButton, BorderLayout.SOUTH);
    }

    public void updatePrices(Map<Integer, ItemPrice> prices) {
        listModel.clear();
        for (Map.Entry<Integer, ItemPrice> entry : prices.entrySet()) {
            int id = entry.getKey();
            ItemPrice price = entry.getValue();
            String itemName = ItemPrice.getName(id);
            listModel.addElement(String.format("%s - S:%d B:%d", itemName, price.getHigh(), price.getLow()));
        }
    }

    private void updateSearch() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            plugin.fetchLatestPrices();
        } else {
            listModel.clear();
            for (Map.Entry<Integer, ItemPrice> entry : plugin.getItemPrices().entrySet()) {
                int id = entry.getKey();
                ItemPrice price = entry.getValue();
                String itemName = ItemPrice.getName(id);
                if (itemName.toLowerCase().contains(query) || String.valueOf(id).contains(query)) {
                    listModel.addElement(String.format("%s - S:%d B:%d", itemName, price.getHigh(), price.getLow()));
                }
            }
        }
    }
}
