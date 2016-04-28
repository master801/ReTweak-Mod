package org.slave.minecraft.retweak.loading;

import org.objectweb.asm.tree.ClassNode;
import org.slave.minecraft.retweak.loading.capsule.CompilationMode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.loading.tweaks.SRGTweak;
import org.slave.minecraft.retweak.loading.tweaks.Tweak;
import org.slave.minecraft.retweak.loading.tweaks.compilation.InterpreterTweak;
import org.slave.minecraft.retweak.loading.tweaks.compilation.JITTweak;

import java.util.ArrayList;
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
            this.tweaks.put(
                    gameVersion,
                    tweaks
            );
        }
    }

    public void tweak(ClassNode classNode, GameVersion gameVersion, CompilationMode compilationMode) {
        switch(compilationMode) {
            case JIT:
                tweaks.get(gameVersion).add(new JITTweak(gameVersion));
                break;
            case INTERPRETER:
                tweaks.get(gameVersion).add(new InterpreterTweak(gameVersion));
                break;
        }

        for(Tweak tweak : tweaks.get(gameVersion)) tweak.tweak(classNode);
    }

}
