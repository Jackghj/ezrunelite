package net.runelite.client.plugins.CoxHelperMini;

import net.runelite.client.plugins.CoxHelperMini.OlmConstants;

public class Olm {
    private int ticksUntilNextAttack = -1;
    private int attackCycle = 0;  // 1-Nauto, 2-Null, 3-Sauto, 4-Special
    private int specialCycle = 0; // 1-Crystals, 2-Lightning, 3-Portals, 4-Heal

    public int getTicksUntilNextAttack() {
        return ticksUntilNextAttack;
    }

    public void setTicksUntilNextAttack(int ticks) {
        this.ticksUntilNextAttack = ticks;
    }

    public int getAttackCycle() {
        return attackCycle;
    }

    public void setAttackCycle(int cycle) {
        this.attackCycle = cycle;
    }

    public int getSpecialCycle() {
        return specialCycle;
    }

    public void setSpecialCycle(int cycle) {
        this.specialCycle = cycle;
    }

    public void updateFromAnimationId(int animationId) {
        switch (animationId) {
            case OlmConstants.OLM_SPECIAL_ATTACK_CRYSTALS:
                setSpecialCycle(1);
                break;
            case OlmConstants.OLM_SPECIAL_ATTACK_LIGHTNING:
                setSpecialCycle(2);
                break;
            case OlmConstants.OLM_SPECIAL_ATTACK_PORTALS:
                setSpecialCycle(3);
                break;
            case OlmConstants.OLM_SPECIAL_ATTACK_HEAL:
                setSpecialCycle(4);
                break;
            case OlmConstants.OLM_ANIMATION_SAUTO:
                setAttackCycle(3);
                break;
            case OlmConstants.OLM_ANIMATION_NULL:
                setAttackCycle(2);
                break;
            default:
                break;
        }
        setTicksUntilNextAttack(4);  // Reset tick count on new animation.
    }
}
