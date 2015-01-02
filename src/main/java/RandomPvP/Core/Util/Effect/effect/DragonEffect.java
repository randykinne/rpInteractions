package RandomPvP.Core.Util.Effect.effect;

import RandomPvP.Core.Util.Effect.Effect;
import RandomPvP.Core.Util.Effect.EffectManager;
import RandomPvP.Core.Util.Effect.EffectType;
import RandomPvP.Core.Util.Effect.util.MathUtils;
import RandomPvP.Core.Util.Effect.util.ParticleEffect;
import RandomPvP.Core.Util.Effect.util.RandomUtils;
import RandomPvP.Core.Util.Effect.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class DragonEffect extends Effect {

    protected final List<Float> rndF;
    protected final List<Double> rndAngle;
    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.FLAME;
    /**
     * Pitch of the dragon arc
     */
    public float pitch = .1f;
    /**
     * Arcs to build the breath
     */
    public int arcs = 20;
    /**
     * Particles per arc
     */
    public int particles = 30;
    /**
     * Steps per iteration
     */
    public int stepsPerIteration = 2;
    /**
     * Length in blocks
     */
    public float length = 4;
    /**
     * Current step. Works as counter
     */
    protected int step = 0;

    public DragonEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = 200;
        rndF = new ArrayList<Float>(arcs);
        rndAngle = new ArrayList<Double>(arcs);
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int j = 0; j < stepsPerIteration; j++) {
            if (step % particles == 0) {
                rndF.clear();
                rndAngle.clear();
            }
            while (rndF.size() < arcs) rndF.add(RandomUtils.random.nextFloat());
            while (rndAngle.size() < arcs) rndAngle.add(RandomUtils.getRandomAngle());
            for (int i = 0; i < arcs; i++) {
                float pitch = rndF.get(i) * 2 * this.pitch - this.pitch;
                float x = (step % particles) * length / particles;
                float y = (float) (pitch * Math.pow(x, 2));
                Vector v = new Vector(x, y, 0);
                VectorUtils.rotateAroundAxisX(v, rndAngle.get(i));
                VectorUtils.rotateAroundAxisZ(v, -location.getPitch() * MathUtils.degreesToRadians);
                VectorUtils.rotateAroundAxisY(v, -(location.getYaw() + 90) * MathUtils.degreesToRadians);
                particle.display(location.add(v), visibleRange, 0, 0, 0, 0, 1);
                location.subtract(v);
            }
            step++;
        }
    }
}
