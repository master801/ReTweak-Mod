package org.slave.minecraft.retweak.load.mod;

import cpw.mods.fml.common.discovery.asm.ModAnnotation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slave.minecraft.retweak.load.mod.asm.ReTweakASMModParser;
import org.slave.minecraft.retweak.load.util.GameVersion.GameVersionModIdentifier;
import org.slave.minecraft.retweak.load.util.GameVersion.GameVersionModIdentifier.Identifier;

import java.io.File;

/**
 * Created by Master on 7/12/2018 at 12:05 AM.
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReTweakModContainerFactory {

    private static ReTweakModContainerFactory instance;

    public ReTweakModContainer build(final ReTweakASMModParser reTweakASMModParser, final File modSource, final ReTweakModCandidate reTweakModCandidate) {
        if (reTweakASMModParser == null || modSource == null || reTweakModCandidate == null) return null;

        String className = reTweakASMModParser.getASMType().getClassName();
        GameVersionModIdentifier gameVersionModIdentifier = reTweakModCandidate.getGameVersion().getGameVersionModIdentifier();

        if (gameVersionModIdentifier.getIdentifier() == Identifier.ANNOTATION) {
            for(ModAnnotation annotation : reTweakASMModParser.getAnnotations()) {
                if (annotation.getASMType().getClassName().equals(gameVersionModIdentifier.getName())) {
                    return new ReTweakModContainer(
                            reTweakModCandidate.getGameVersion(),
                            className,
                            reTweakModCandidate,
                            annotation.getValues()
                    );
                }
            }
        }
        return null;
    }

    public static ReTweakModContainerFactory instance() {
        if (ReTweakModContainerFactory.instance == null) ReTweakModContainerFactory.instance = new ReTweakModContainerFactory();
        return ReTweakModContainerFactory.instance;
    }

}
