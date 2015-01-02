package RandomPvP.Core.Util.Effect.effect;

import RandomPvP.Core.Util.Effect.Effect;
import RandomPvP.Core.Util.Effect.EffectManager;
import RandomPvP.Core.Util.Effect.EffectType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TurnEffect extends Effect {

    /**
     * Angular movement per iteration
     */
    public float step = 11.25f;

    public TurnEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = (int) (360 * 5 / step);
    }

    @Override
    public void onRun() {
        Entity entity = getEntity();
        if (entity == null) {
            cancel();
            return;
        }
        Location loc = entity.getLocation();
        loc.setYaw(loc.getYaw() + step);
        entity.teleport(loc);
    }

}
