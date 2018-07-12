package org.slave.minecraft.retweak.asm;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;

import java.io.File;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by master on 2/25/18 at 9:28 PM
 *
 * @author master
 */
public final class ReTweakModContainer implements ModContainer  {

    @Override
    public String getModId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public File getSource() {
        return null;
    }

    @Override
    public ModMetadata getMetadata() {
        return null;
    }

    @Override
    public void bindMetadata(final MetadataCollection mc) {

    }

    @Override
    public void setEnabledState(final boolean enabled) {

    }

    @Override
    public Set<ArtifactVersion> getRequirements() {
        return null;
    }

    @Override
    public List<ArtifactVersion> getDependencies() {
        return null;
    }

    @Override
    public List<ArtifactVersion> getDependants() {
        return null;
    }

    @Override
    public String getSortingRules() {
        return null;
    }

    @Override
    public boolean registerBus(final EventBus bus, final LoadController controller) {
        return false;
    }

    @Override
    public boolean matches(final Object mod) {
        return false;
    }

    @Override
    public Object getMod() {
        return null;
    }

    @Override
    public ArtifactVersion getProcessedVersion() {
        return null;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public String getDisplayVersion() {
        return null;
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange() {
        return null;
    }

    @Override
    public Certificate getSigningCertificate() {
        return null;
    }

    @Override
    public Map<String, String> getCustomModProperties() {
        return null;
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return null;
    }

    @Override
    public Map<String, String> getSharedModDescriptor() {
        return null;
    }

    @Override
    public Disableable canBeDisabled() {
        return null;
    }

    @Override
    public String getGuiClassName() {
        return null;
    }

    @Override
    public List<String> getOwnedPackages() {
        return null;
    }

}
