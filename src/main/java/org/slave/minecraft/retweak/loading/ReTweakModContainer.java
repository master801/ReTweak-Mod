package org.slave.minecraft.retweak.loading;

/**
 * <p>
 *     {@link cpw.mods.fml.common.ModContainer}
 * </p>
 *
 * Created by Master801 on 3/21/2016 at 9:54 AM.
 *
 * @author Master801
 */
public final class ReTweakModContainer {

    private final String modid, name, version;

    public ReTweakModContainer(final String modid, final String name, final String version) {
        this.modid = modid;
        this.name = name;
        this.version = version;
    }

    public String getModid() {
        return modid;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

}
