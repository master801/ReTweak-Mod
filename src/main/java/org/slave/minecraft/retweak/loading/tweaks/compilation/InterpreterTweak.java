package org.slave.minecraft.retweak.loading.tweaks.compilation;

import org.objectweb.asm.tree.ClassNode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.loading.tweaks.Tweak;

/**
 * Created by Master on 4/27/2016 at 7:26 AM.
 *
 * @author Master
 */
public final class InterpreterTweak implements Tweak {

    private final GameVersion gameVersion;

    public InterpreterTweak(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
    }

    @Override
    public String getName() {
        return "Interpreter";
    }

    @Override
    public void tweak(final ClassNode classNode) {
        if (gameVersion == null) return;
        //TODO?
    }

    @Override
    public int getWantedSortIndex() {
        return 0;
    }

}
