package org.slave.minecraft.retweak.load.mod;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.discovery.asm.ModAnnotation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slave.lib.resources.ASMTable;
import org.slave.minecraft.retweak.load.mod.asm.ReTweakASMModParser;
import org.slave.minecraft.retweak.load.util.GameVersion.GameVersionModIdentifier;

import java.io.File;
import java.util.Map;

/**
 * Created by Master on 7/12/2018 at 12:05 AM.
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReTweakModContainerFactory {

    private static ReTweakModContainerFactory instance;

    public ReTweakModContainer build(final ReTweakASMModParser reTweakASMModParser, final ASMTable asmTable, final File modSource, final ReTweakModCandidate reTweakModCandidate) {
        if (reTweakASMModParser == null || modSource == null || reTweakModCandidate == null) return null;

        String className = reTweakASMModParser.getASMType().getClassName();
        GameVersionModIdentifier gameVersionModIdentifier = reTweakModCandidate.getGameVersion().getGameVersionModIdentifier();

        switch(gameVersionModIdentifier.getIdentifier()) {
            case EXTENDS:
                if (reTweakASMModParser.getASMSuperType().getClassName().replace('.', '/').equals(gameVersionModIdentifier.getName())) {
                    Map<String, Object> descriptor = Maps.newHashMap();

                    descriptor.put("modid", reTweakASMModParser.getASMType().getClassName());
                    descriptor.put("name", modSource.getName().indexOf('.') != -1 ?  modSource.getName().substring(0, modSource.getName().indexOf('.')) : modSource.getName());

                    return new ReTweakModContainer(
                            reTweakModCandidate.getGameVersion(),
                            className,
                            reTweakModCandidate,
                            asmTable,
                            descriptor
                    );
                }
                break;
            case ANNOTATION:
                for(ModAnnotation annotation : reTweakASMModParser.getAnnotations()) {
                    if (annotation.getASMType().getClassName().replace('.', '/').equals(gameVersionModIdentifier.getName())) {
                        return new ReTweakModContainer(
                                reTweakModCandidate.getGameVersion(),
                                className,
                                reTweakModCandidate,
                                asmTable,
                                annotation.getValues()
                        );
                    }
                }
                break;
        }

        return null;
    }

    public static ReTweakModContainerFactory instance() {
        if (ReTweakModContainerFactory.instance == null) ReTweakModContainerFactory.instance = new ReTweakModContainerFactory();
        return ReTweakModContainerFactory.instance;
    }

}
