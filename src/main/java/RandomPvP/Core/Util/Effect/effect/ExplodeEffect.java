package RandomPvP.Core.Util.Effect.effect;

import RandomPvP.Core.Util.Effect.Effect;
import RandomPvP.Core.Util.Effect.EffectManager;
import RandomPvP.Core.Util.Effect.EffectType;
import RandomPvP.Core.Util.Effect.util.ParticleEffect;
import RandomPvP.Core.Util.Effect.util.RandomUtils;
import org.bukkit.Location;
import org.bukkit.Sound;

public class ExplodeEffect extends Effect {

    /**
     * Amount of spawned smoke-sparks
     */
    public int amount = 25;

    /**
     * Movement speed of smoke-sparks. Should be increases with force.
     */
    public float speed = .5f;

    public Sound sound = Sound.EXPLODE;

    public ExplodeEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.INSTANT;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        location.getWorld().playSound(location, sound, 4.0F, (1.0F + (RandomUtils.random.nextFloat() - RandomUtils.random.nextFloat()) * 0.2F) * 0.7F);
        ParticleEffect.EXPLODE.display(location, visibleRange, 0, 0, 0, speed, amount);
        ParticleEffect.HUGE_EXPLOSION.display(location, visibleRange, 0, 0, 0, 0, amount);
    }

}
