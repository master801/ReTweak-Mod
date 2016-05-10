package org.slave.minecraft.retweak.loading;

/**
 * Created by Master on 5/7/2016 at 6:36 PM.
 *
 * @author Master
 */
public final class ReTweakModContainer {

    private final String modid;
    private String name;
    private String version;
    private final ReTweakModCandidate reTweakModCandidate;
    private boolean enabled = true;

    public ReTweakModContainer(final String modid, final String name, final String version, final ReTweakModCandidate reTweakModCandidate) {
        this.modid = modid;
        this.name = name;
        this.version = version;
        this.reTweakModCandidate = reTweakModCandidate;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    ReTweakModCandidate getReTweakModCandidate() {
        return reTweakModCandidate;
    }

}
