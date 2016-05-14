package org.slave.minecraft.retweak.loading;

import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.exceptions.IncorrectSortException;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.loading.tweaks.Tweak;
import org.slave.minecraft.retweak.loading.tweaks.Tweak.TweakException;
import org.slave.minecraft.retweak.loading.tweaks.compilation.InterpreterTweak;
import org.slave.minecraft.retweak.loading.tweaks.compilation.JITTweak;
import org.slave.minecraft.retweak.resources.ReTweakConfig;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;

/**
 * Created by Master on 4/27/2016 at 7:19 AM.
 *
 * @author Master
 */
public final class ReTweakTweakHandler {

    public static final ReTweakTweakHandler INSTANCE = new ReTweakTweakHandler();

    private final EnumMap<GameVersion, List<Tweak>> tweaks;

    private ReTweakTweakHandler() {
        tweaks = new EnumMap<>(GameVersion.class);
        addTweaks();
        sortTweaks();
    }

    private void addTweaks() {
        for(GameVersion gameVersion : GameVersion.values()) {
            List<Tweak> tweaks = new ArrayList<>();
            switch(ReTweakConfig.INSTANCE.getCompilationMode()) {
                case JIT:
                    tweaks.add(new JITTweak(gameVersion));
                    break;
                case INTERPRETER:
                    tweaks.add(new InterpreterTweak(gameVersion));
                    break;
            }
            this.tweaks.put(
                    gameVersion,
                    tweaks
            );
        }
    }

    private void sortTweaks() {
        for(List<Tweak> list : tweaks.values()) {
            Collections.sort(
                    list,
                    new Comparator<Tweak>() {

                        @Override
                        public int compare(final Tweak o1, final Tweak o2) {
                            if (o1.getWantedSortIndex() < 0 || o2.getWantedSortIndex() < 0) throw new IncorrectSortException("Cannot have sort index less than zero!");
                            if (o1.getWantedSortIndex() == o2.getWantedSortIndex()) throw new IncorrectSortException("Sort index of Tweak cannot be the same!");
                            if (o1.getWantedSortIndex() < o2.getWantedSortIndex()) return -1;
                            if (o1.getWantedSortIndex() > o2.getWantedSortIndex()) return 1;
                            return 0;
                        }

                    }
            );
        }
    }

    public void tweak(ClassNode classNode, GameVersion gameVersion, ReTweakModContainer reTweakModContainer) throws TweakException {
        List<Tweak> tweakList = tweaks.get(gameVersion);
        for(Tweak tweak : tweakList) {
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Start Tweak \"{}\"",
                        tweak.getName()
                );
            }
            tweak.tweak(
                    reTweakModContainer,
                    classNode
            );
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "End Tweak\n\n",
                        tweak.getName()
                );
            }
        }
    }

}
