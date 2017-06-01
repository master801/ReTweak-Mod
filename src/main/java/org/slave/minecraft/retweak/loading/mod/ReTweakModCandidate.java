package org.slave.minecraft.retweak.loading.mod;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ContainerType;
import cpw.mods.fml.common.discovery.ModCandidate;

import java.io.File;
import java.util.List;

/**
 * Created by Master on 6/1/2017 at 8:42 AM.
 *
 * @author Master
 */
public final class ReTweakModCandidate extends ModCandidate {

    public ReTweakModCandidate(final File classPathRoot, final File modContainer, final ContainerType sourceType) {
        super(classPathRoot, modContainer, sourceType);
    }

    @Override
    public List<ModContainer> explore(final ASMDataTable table) {
        //TODO
        return null;
    }

}
