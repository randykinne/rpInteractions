package RandomPvP.Core.Util.Effect.effect;

import RandomPvP.Core.Util.Effect.Effect;
import RandomPvP.Core.Util.Effect.EffectManager;
import RandomPvP.Core.Util.Effect.EffectType;
import RandomPvP.Core.Util.Effect.util.MathUtils;
import RandomPvP.Core.Util.Effect.util.ParticleEffect;
import RandomPvP.Core.Util.Effect.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class DnaEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particleHelix = ParticleEffect.FLAME;

    /**
     * Particle of base 1
     */
    public ParticleEffect particleBase1 = ParticleEffect.WAKE;

    /**
     * Particle of base 2
     */
    public ParticleEffect particleBase2 = ParticleEffect.RED_DUST;

    /**
     * Radials to turn per step
     */
    public double radials = Math.PI / 30;

    /**
     * Radius of dna-double-helix
     */
    public float radius = 1.5f;

    /**
     * Particles to spawn per interation
     */
    public int particlesHelix = 3;

    /**
     * Particles per base
     */
    public int particlesBase = 15;

    /**
     * Length of the dna-double-helix
     */
    public float length = 15;

    /**
     * Growth per particle
     */
    public float grow = 0.2f;

    /**
     * Particles between every base
     */
    public float baseInterval = 10;

    /**
     * Current step. Works as counter
     */
    protected int step = 0;

    public DnaEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 500;
    }

    @Override
    public void onRun() {
        Location location = getLocation();
        for (int j = 0; j < particlesHelix; j++) {
            if (step * grow > length)
                step = 0;
            for (int i = 0; i < 2; i++) {
                double angle = step * radials + Math.PI * i;
                Vector v = new Vector(Math.cos(angle) * radius, step * grow, Math.sin(angle) * radius);
                drawParticle(location, v, particleHelix);
            }
            if (step % baseInterval == 0) {
                for (int i = -particlesBase; i <= particlesBase; i++) {
                    if (i == 0)
                        continue;
                    ParticleEffect particle = particleBase1;
                    if (i < 0)
                        particle = particleBase2;
                    double angle = step * radials;
                    Vector v = new Vector(Math.cos(angle), 0, Math.sin(angle)).multiply(radius * i / particlesBase).setY(step * grow);
                    drawParticle(location, v, particle);
                }
            }
            step++;
        }
    }

    protected void drawParticle(Location location, Vector v, ParticleEffect particle) {
        VectorUtils.rotateAroundAxisX(v, (location.getPitch() + 90) * MathUtils.degreesToRadians);
        VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);

        location.add(v);
        particle.display(location, visibleRange);
        location.subtract(v);
    }

}
