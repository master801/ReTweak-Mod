package org.slave.minecraft.retweak.load.mod;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ContainerType;
import cpw.mods.fml.common.discovery.ModCandidate;
import lombok.Getter;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.lib.resources.ASMTable;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Master on 7/11/2018 at 11:16 PM.
 *
 * @author Master
 */
public final class ReTweakModCandidate extends ModCandidate {

    /**
     * {@link cpw.mods.fml.common.discovery.ModCandidate#mods}
     */
    private static Field fieldMods;
    private static Field fieldTable;

    @Getter
    private final GameVersion gameVersion;

    private ASMTable asmTable;

    public ReTweakModCandidate(final GameVersion gameVersion, final File classPathRoot, final File modContainer, final ContainerType sourceType) {
        super(classPathRoot, modContainer, sourceType);
        this.gameVersion = gameVersion;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public List<ModContainer> explore(final ASMDataTable table) {
        ReTweakModCandidate.setTable(this, table);

        List<ModContainer> modContainers = gameVersion
                .getDiscoverer()
                .discover(this, table);

        ReTweakModCandidate.setMods(this, modContainers);
        return getContainedMods();
    }

    public ASMTable getASMTable() {
        return asmTable;
    }

    void setASMTable(final ASMTable asmTable) {
        this.asmTable = asmTable;
    }

    private static void setTable(final ReTweakModCandidate reTweakModCandidate, final ASMDataTable asmDataTable) {
        if (ReTweakModCandidate.fieldTable == null) {
            try {
                ReTweakModCandidate.fieldTable = ReflectionHelper.getField(
                        ModCandidate.class,
                        "table"
                );
            } catch (NoSuchFieldException e) {
                ReTweak.LOGGER_RETWEAK.error("Failed to get field \"table\"!", e);
            }
        }
        try {
            ReTweakModCandidate.fieldTable.setAccessible(true);
            ReTweakModCandidate.fieldTable.set(reTweakModCandidate, asmDataTable);
            ReTweakModCandidate.fieldTable.setAccessible(false);
        } catch (IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error("Failed to set field \"table\"!", e);
        }
    }

    private static ASMDataTable getTable(final ReTweakModCandidate reTweakModCandidate) {
        if (ReTweakModCandidate.fieldTable == null) {
            try {
                ReTweakModCandidate.fieldTable = ReflectionHelper.getField(
                        ModCandidate.class,
                        "table"
                );
            } catch (NoSuchFieldException e) {
                ReTweak.LOGGER_RETWEAK.error("Failed to get field \"table\"!", e);
            }
        }
        try {
            ReTweakModCandidate.fieldTable.setAccessible(true);
            ASMDataTable table = (ASMDataTable)ReTweakModCandidate.fieldTable.get(reTweakModCandidate);
            ReTweakModCandidate.fieldTable.setAccessible(false);
            return table;
        } catch (IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error("Failed to set field \"table\"!", e);
        }
        return null;
    }

    private static void setMods(final ModCandidate modCandidate, final List<ModContainer> list) {
        if (list == null) return;
        try {
            if (ReTweakModCandidate.fieldMods == null) {
                ReTweakModCandidate.fieldMods = ReflectionHelper.getField(
                        ModCandidate.class,
                        "mods"
                );
            }
            ReflectionHelper.setFieldValue(
                    ReTweakModCandidate.fieldMods,
                    modCandidate,
                    list
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to set field \"mods\" in ReTweakModCandidate!",
                    e
            );
        }
    }

}
