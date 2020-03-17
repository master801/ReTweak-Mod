package org.slave.minecraft.retweak.load.util;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.discovery.ITypeDiscoverer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.ReTweakClassLoader;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass_1_2_5;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass_1_4_7;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass_1_5_2;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass_1_6_2;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass_1_6_4;
import org.slave.minecraft.retweak.load.mapping.SrgMap;
import org.slave.minecraft.retweak.load.mod.discoverer._JarDiscoverer;
import org.slave.minecraft.retweak.load.util.GameVersion.GameVersionModIdentifier.Identifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Master on 7/11/2018 at 8:45 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum GameVersion {

    V_1_2_5(
            "1.2.5",
            GameVersionModIdentifier.IDENTIFIER_EXTENDS_MOD,
            TweakClass_1_2_5.INSTANCE,
            _JarDiscoverer.class,
            null,//No event annotations
            null,//No proxy annotation
            true
    ),

    V_1_4_7(
            "1.4.7",
            GameVersionModIdentifier.IDENTIFIER_ANNOTATION_MOD,
            TweakClass_1_4_7.INSTANCE,
            _JarDiscoverer.class,
            Lists.newArrayList(
                    new EventAnnotation(Identifier.ANNOTATION, "cpw/mods/fml/common/Mod$PreInit"),
                    new EventAnnotation(Identifier.ANNOTATION, "cpw/mods/fml/common/Mod$Init"),
                    new EventAnnotation(Identifier.ANNOTATION, "cpw/mods/fml/common/Mod$PostInit"),

                    EventAnnotation.EVENT_ANNOTATION_EVENT_HANDLER
            ),
            EventAnnotation.EVENT_ANNOTATION_SIDED_PROXY,
            true
    ),

    V_1_5_2(
            "1.5.2",
            GameVersionModIdentifier.IDENTIFIER_ANNOTATION_MOD,
            TweakClass_1_5_2.INSTANCE,
            _JarDiscoverer.class,
            Lists.newArrayList(
                    new EventAnnotation(Identifier.ANNOTATION, "cpw/mods/fml/common/Mod$PreInit"),
                    new EventAnnotation(Identifier.ANNOTATION, "cpw/mods/fml/common/Mod$Init"),
                    new EventAnnotation(Identifier.ANNOTATION, "cpw/mods/fml/common/Mod$PostInit"),

                    EventAnnotation.EVENT_ANNOTATION_EVENT_HANDLER
            ),
            EventAnnotation.EVENT_ANNOTATION_SIDED_PROXY,
            false
    ),

    V_1_6_2(
            "1.6.2",
            GameVersionModIdentifier.IDENTIFIER_ANNOTATION_MOD,
            TweakClass_1_6_2.INSTANCE,
            _JarDiscoverer.class,
            Lists.newArrayList(
                    EventAnnotation.EVENT_ANNOTATION_EVENT_HANDLER
            ),
            EventAnnotation.EVENT_ANNOTATION_INSTANCE_FACTORY,
            EventAnnotation.EVENT_ANNOTATION_SIDED_PROXY,
            true
    ),

    V_1_6_4(
            "1.6.4",
            GameVersionModIdentifier.IDENTIFIER_ANNOTATION_MOD,
            TweakClass_1_6_4.INSTANCE,
            _JarDiscoverer.class,
            Lists.newArrayList(
                    EventAnnotation.EVENT_ANNOTATION_EVENT_HANDLER
            ),
            EventAnnotation.EVENT_ANNOTATION_INSTANCE_FACTORY,
            EventAnnotation.EVENT_ANNOTATION_SIDED_PROXY,
            true
    );

    public static final GameVersion[] VALUES = GameVersion.values();

    @NonNull
    @Getter
    private final String version;

    @NonNull
    @Getter
    private final GameVersionModIdentifier gameVersionModIdentifier;

    @Getter
    private final TweakClass tweakClass;

    @NonNull
    private final Class<? extends ITypeDiscoverer> discovererClass;

    @Getter
    private final List<EventAnnotation> eventAnnotations;

    @Getter
    private EventAnnotation instanceFactoryAnnotation;

    @Getter
    private final EventAnnotation sidedProxyAnnotation;

    @Getter
    private final boolean isDisabled;

    @Getter
    private SrgMap srgMap;

    GameVersion(final String version, final GameVersionModIdentifier gameVersionModIdentifier, final TweakClass tweakClass, final Class<? extends ITypeDiscoverer> discovererClass, final List<EventAnnotation> eventAnnotations, final EventAnnotation instanceFactoryAnnotation, final EventAnnotation sidedProxyAnnotation, final boolean isDisabled) {
        this.version = version;
        this.gameVersionModIdentifier = gameVersionModIdentifier;
        this.tweakClass = tweakClass;
        this.discovererClass = discovererClass;
        this.eventAnnotations = eventAnnotations;
        this.instanceFactoryAnnotation = instanceFactoryAnnotation;
        this.sidedProxyAnnotation = sidedProxyAnnotation;
        this.isDisabled = isDisabled;
    }

    @Override
    public String toString() {
        return getVersion();
    }

    public ITypeDiscoverer getDiscoverer() {
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
                            this
                    }
            );
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            ReTweak.LOGGER_RETWEAK.error("Failed to create Discoverer for GameVersion!", e);
            ReTweak.LOGGER_RETWEAK.debug("GameVersion: {}/{}", name(), getVersion());
        }
        return null;
    }

    public ReTweakClassLoader getClassLoader() {
        return ReTweakClassLoader.getInstance(this);
    }

//    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    //FIXME Lombok is stupid...
    public static final class GameVersionModIdentifier {

        static final GameVersionModIdentifier IDENTIFIER_ANNOTATION_MOD = new GameVersionModIdentifier(
                Identifier.ANNOTATION,
                Mod.class.getName().replace('.', '/')
        );

        static final GameVersionModIdentifier IDENTIFIER_EXTENDS_MOD = new GameVersionModIdentifier(
                Identifier.EXTENDS,
                "forge/NetworkMod"
        );

        @Getter
        private final Identifier identifier;

        @Getter
        private final String name;

        private GameVersionModIdentifier(final Identifier identifier, final String name) {
            this.identifier = identifier;
            this.name = name;
        }

        public enum Identifier {

                EXTENDS,

                ANNOTATION;

            }

        }

}
