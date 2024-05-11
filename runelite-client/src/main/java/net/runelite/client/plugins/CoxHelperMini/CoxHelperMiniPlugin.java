package net.runelite.client.plugins.CoxHelperMini;

import net.runelite.api.Client;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.NPC;
import net.runelite.api.Projectile;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;
import com.google.inject.Provides;

@PluginDescriptor(
    name = "CoxHelper Mini",
    description = "Displays various Olm mechanics",
    tags = {"cox", "raids", "olm", "prayer", "overlay"}
)
public class CoxHelperMiniPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private CoxHelperMiniOverlay coxHelperMiniOverlay;

    private Olm olm = new Olm();
    private int lastProjectileId = -1;

    @Provides
    CoxHelperMiniConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CoxHelperMiniConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(coxHelperMiniOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(coxHelperMiniOverlay);
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        if (!(event.getActor() instanceof NPC)) return;
        NPC npc = (NPC) event.getActor();
        olm.updateFromAnimationId(npc.getAnimation());
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        Projectile projectile = event.getProjectile();
        if (projectile != null) {
            lastProjectileId = projectile.getId();
        }
    }

    public int getLastProjectileId() {
        return lastProjectileId;
    }

    public Olm getOlm() {
        return olm;
    }
}
