package org.slave.minecraft.retweak.asm.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.slave.lib.helpers.ASMHelper;
import org.slave.lib.resources.Bulk;
import org.slave.lib.resources.EnumMap;
import org.slave.lib.resources.wrappingdata.WrappingDataT.WrappingDataT2;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.Map.Entry;

/**
 * Created by Master on 4/26/2016 at 7:23 PM.
 *
 * @author Master
 */
public final class ModClassVisitor extends ClassVisitor {

    @SuppressWarnings("unchecked")
    private static final EnumMap<GameVersion, Entry<Type, String>> TYPES = new EnumMap<>(
            GameVersion.class,
            new WrappingDataT2[] {
                    new WrappingDataT2(
                            GameVersion.V_1_4_7,
                            new Bulk<>(
                                    Type.ANNOTATION,
                                    "L" + "cpw/mods/fml/common/Mod" + ";"
                            )
                    ),
                    new WrappingDataT2(
                            GameVersion.V_1_5_2,
                            new Bulk<>(
                                    Type.ANNOTATION,
                                    "L" + "cpw/mods/fml/common/Mod" + ";"
                            )
                    )
            }
    );

    private boolean isMod = false;

    private final GameVersion gameVersion;
    private final Type type;

    public ModClassVisitor(ClassVisitor cv, GameVersion gameVersion) {
        super(
                ASMHelper.ASM_VERSION,
                cv
        );
        switch(this.gameVersion = gameVersion) {
            //Versions less than 1.2.5 extend classes to be mods
            default:
                type = Type.ANNOTATION;
                break;
        }
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        if (type == Type.EXTENDS) {
            if (superName.equals(ModClassVisitor.TYPES.get(gameVersion).getValue())) {
                isMod = true;
                ReTweakResources.RETWEAK_LOGGER.info("!!!!! EXTENDS !!!!!\nNot yet implemented!!!!!!");
            }
        }

        super.visit(
                version,
                access,
                name,
                signature,
                superName,
                interfaces
        );
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        if (type == Type.ANNOTATION) {
            if (desc.equals(ModClassVisitor.TYPES.get(gameVersion).getValue())) {
                isMod = true;
            }
        }
        return super.visitAnnotation(
                desc,
                visible
        );
    }

    public boolean isMod() {
        return isMod;
    }

    public static String getDesc(GameVersion gameVersion) {
        if (gameVersion == null) return null;
        Entry<Type, String> entry = TYPES.get(gameVersion);
        if (entry == null) return null;
        return entry.getValue();
    }

    private enum Type {

        EXTENDS,

        ANNOTATION;

        Type() {
        }

    }

}
