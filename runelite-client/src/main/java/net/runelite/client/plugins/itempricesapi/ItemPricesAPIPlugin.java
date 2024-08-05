package net.runelite.client.plugins.itempricesapi;

import com.google.gson.Gson;
import com.google.inject.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@PluginDescriptor(
        name = "Item Prices API",
        description = "Fetches the latest item prices from the OSRS Wiki",
        tags = {"prices", "api", "osrs"}
)
public class ItemPricesAPIPlugin extends Plugin {
    private static final String LATEST_URL = "https://prices.runescape.wiki/api/v1/osrs/latest";
    private static final String MAPPING_URL = "https://prices.runescape.wiki/api/v1/osrs/mapping";

    @Inject
    private ClientToolbar clientToolbar;

    private ItemPricesAPIPanel panel;
    private NavigationButton navButton;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Map<Integer, ItemPrice> itemPrices = new HashMap<>();
    private final Map<Integer, String> itemIdToNameMap = new HashMap<>();

    @Provides
    ItemPricesAPIConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ItemPricesAPIConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        panel = new ItemPricesAPIPanel(this);
        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "panel_icon.png");
        navButton = NavigationButton.builder()
                .tooltip("Item Prices")
                .icon(icon)
                .priority(5)
                .panel(panel)
                .build();
        clientToolbar.addNavigation(navButton);

        fetchItemMappings();
        fetchLatestPrices();
    }

    @Override
    protected void shutDown() throws Exception {
        clientToolbar.removeNavigation(navButton);
        itemPrices.clear();
        itemIdToNameMap.clear();
    }

    void fetchLatestPrices() {
        Request request = new Request.Builder().url(LATEST_URL).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String body = response.body().string();
                LatestPrices latestPrices = new Gson().fromJson(body, LatestPrices.class);
                itemPrices.clear();
                for (Map.Entry<Integer, ItemPrice> entry : latestPrices.getData().entrySet()) {
                    itemPrices.put(entry.getKey(), entry.getValue());
                }
                panel.updatePrices(itemPrices);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchItemMappings() {
        Request request = new Request.Builder().url(MAPPING_URL).build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String body = response.body().string();
                ItemMapping[] itemMappings = new Gson().fromJson(body, ItemMapping[].class);
                for (ItemMapping item : itemMappings) {
                    itemIdToNameMap.put(item.getId(), item.getName());
                }
                ItemPrice.setItemIdToNameMap(itemIdToNameMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, ItemPrice> getItemPrices() {
        return itemPrices;
    }
}
