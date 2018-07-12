package org.slave.minecraft.retweak.load.util;

import cpw.mods.fml.common.discovery.ITypeDiscoverer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.util.discoverer.JarAnnotationDiscoverer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Master on 7/11/2018 at 8:45 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor
public enum GameVersion {

    V_1_4_7(
            "1.4.7",
            JarAnnotationDiscoverer.class,
            GameVersionModIdentifier.IDENTIFIER_ANNOTATION_MOD
    );

    @Getter
    private final String version;

    private final Class<? extends ITypeDiscoverer> discovererClass;

    @Getter
    private final GameVersionModIdentifier gameVersionModIdentifier;

    public ITypeDiscoverer getDiscoverer(final GameVersion gameVersion) {
        if (gameVersion == null || gameVersion != this) return null;
        try {
            Constructor<? extends ITypeDiscoverer> constructor = ReflectionHelper.getConstructor(
                    discovererClass,
                    new Class<?>[] {
                            GameVersion.class
                    }
            );
            return ReflectionHelper.createFromConstructor(
                    constructor,
                    new Object[] {
                            gameVersion
                    }
            );
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to create Discoverer for GameVersion!",
                    e
            );
            ReTweak.LOGGER_RETWEAK.debug(
                    "GameVersion: {}/{}",
                    gameVersion.name(),
                    gameVersion.getVersion()
            );
        }
        return null;
    }

    @RequiredArgsConstructor
    public static final class GameVersionModIdentifier {

        static final GameVersionModIdentifier IDENTIFIER_ANNOTATION_MOD = new GameVersionModIdentifier(
                Identifier.ANNOTATION,
                "cpw/mods/fml/common/Mod"
        );

        @Getter
        private final Identifier identifier;

        @Getter
        private final String name;

        public enum Identifier {

            ANNOTATION;

        }

    }

}