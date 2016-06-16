package org.slave.minecraft.retweak.loading.tweaks;

import org.objectweb.asm.tree.ClassNode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;

/**
 * Created by Master on 4/27/2016 at 7:21 AM.
 *
 * @author Master
 */
public interface Tweak {

    String getName();

    void tweak(final ClassNode classNode, final GameVersion gameVersion) throws TweakException;

    int getWantedSortIndex();

    class TweakException extends RuntimeException {

        private static final long serialVersionUID = 1990489569439481334L;

        public TweakException() {
            super();
        }

        public TweakException(final String message) {
            super(message);
        }

        public TweakException(final Throwable cause) {
            super(cause);
        }

    }

    class StopTweakException extends TweakException {

        private static final long serialVersionUID = 5779122186044917493L;

        public StopTweakException() {
            super();
        }

        public StopTweakException(final String message) {
            super(message);
        }

        public StopTweakException(final Throwable cause) {
            super(cause);
        }

    }

}
