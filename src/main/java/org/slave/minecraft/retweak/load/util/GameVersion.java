package org.slave.minecraft.retweak.load.util;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.discovery.ITypeDiscoverer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.ReTweakClassLoader;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass_1_4_7;
import org.slave.minecraft.retweak.load.mod.ReTweakModContainer;
import org.slave.minecraft.retweak.load.mod.ReTweakModContainer.ReTweakModContainerEventHandler;
import org.slave.minecraft.retweak.load.mod.discoverer.JarAnnotationDiscoverer;
import org.slave.minecraft.retweak.load.util.GameVersion.GameVersionModIdentifier.Identifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Master on 7/11/2018 at 8:45 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor
public enum GameVersion {

    V_1_4_7(
            "1.4.7",
            GameVersionModIdentifier.IDENTIFIER_ANNOTATION_MOD,
            TweakClass_1_4_7.INSTANCE,
            JarAnnotationDiscoverer.class,
            Lists.newArrayList(
                    new EventAnnotation(Identifier.ANNOTATION, "cpw/mods/fml/common/Mod$PreInit"),
                    new EventAnnotation(Identifier.ANNOTATION, "cpw/mods/fml/common/Mod$Init"),
                    new EventAnnotation(Identifier.ANNOTATION, "cpw/mods/fml/common/Mod$PostInit")
            ),
            null
    );

    @Getter
    private final String version;

    @Getter
    private final GameVersionModIdentifier gameVersionModIdentifier;

    @Getter
    private final TweakClass tweakClass;

    private final Class<? extends ITypeDiscoverer> discovererClass;

    @Getter
    private final List<EventAnnotation> eventAnnotations;

    @Getter
    private final EventAnnotation instanceFactoryAnnotation;

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

    public ReTweakClassLoader getClassLoader() {
        return ReTweakClassLoader.getInstance(this);
    }

    @RequiredArgsConstructor
    public static final class GameVersionModIdentifier {

        static final GameVersionModIdentifier IDENTIFIER_ANNOTATION_MOD = new GameVersionModIdentifier(
                Identifier.ANNOTATION,
                Mod.class.getName().replace('.', '/')
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
