package org.slave.minecraft.retweak.loading;

import org.objectweb.asm.tree.ClassNode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.loading.tweaks.SRGTweak;
import org.slave.minecraft.retweak.loading.tweaks.Tweak;
import org.slave.minecraft.retweak.resources.ReTweakConfig;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Master on 4/27/2016 at 7:19 AM.
 *
 * @author Master
 */
public final class ReTweakTweakHandler {

    public static final ReTweakTweakHandler INSTANCE = new ReTweakTweakHandler();

    private final HashMap<GameVersion, List<Tweak>> tweaks = new HashMap<>();

    private ReTweakTweakHandler() {
        for(GameVersion gameVersion : GameVersion.values()) {
            List<Tweak> tweaks = new ArrayList<>();
            tweaks.add(new SRGTweak(gameVersion));
            switch(ReTweakConfig.INSTANCE.getCompilationMode()) {
                case JIT:
                    //TODO
//                    tweaks.add(new JITTweak(gameVersion));
                    break;
                case INTERPRETER:
//                    tweaks.add(new InterpreterTweak(gameVersion));
                    break;
            }

            Collections.sort(
                    tweaks,
                    new Comparator<Tweak>() {

                        @Override
                        public int compare(final Tweak o1, final Tweak o2) {
                            if (o1.getSortIndex() < 0 || o2.getSortIndex() < 0) throw new IllegalStateException("Cannot have sort index less than zero!");
                            if (o1.getSortIndex() == o2.getSortIndex()) throw new IllegalStateException("Sort index of Tweak cannot be the same!");
                            if (o1.getSortIndex() < o2.getSortIndex()) return -1;
                            if (o1.getSortIndex() > o2.getSortIndex()) return 1;
                            return 0;
                        }

                    }
            );
            this.tweaks.put(
                    gameVersion,
                    tweaks
            );
        }
    }

    public void tweak(ClassNode classNode, GameVersion gameVersion) {
        List<Tweak> tweakList = tweaks.get(gameVersion);
        for(Tweak tweak : tweakList) {
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Start Tweak \"{}\"",
                        tweak.getName()
                );
            }
            tweak.tweak(classNode);
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "End Tweak\n\n",
                        tweak.getName()
                );
            }
        }
    }

}
