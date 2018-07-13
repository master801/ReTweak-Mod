package org.slave.minecraft.retweak.load.mod;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ContainerType;
import cpw.mods.fml.common.discovery.ModCandidate;
import lombok.Getter;
import org.slave.lib.helpers.ReflectionHelper;
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

    @Getter
    private final GameVersion gameVersion;

    public ReTweakModCandidate(final GameVersion gameVersion, final File classPathRoot, final File modContainer, final ContainerType sourceType) {
        super(classPathRoot, modContainer, sourceType);
        this.gameVersion = gameVersion;
    }

    @Override
    public List<ModContainer> explore(final ASMDataTable table) {
        setMods(
                gameVersion
                        .getDiscoverer(gameVersion)
                        .discover(this, table)
        );
        return getContainedMods();
    }

    private void setMods(final List<ModContainer> list) {
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
                    this,
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
