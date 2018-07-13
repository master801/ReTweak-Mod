package org.slave.minecraft.retweak.asm;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import org.slave.minecraft.retweak.asm.transformers.TransformerLoader;
import org.slave.minecraft.retweak.asm.transformers.TransformerSimpleReloadableResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by master on 2/25/18 at 9:24 PM
 *
 * @author master
 */
@Name("ReTweak")
@MCVersion("1.7.10")
@TransformerExclusions({"org.slave.minecraft.retweak.asm."})
public final class ReTweakASM implements IFMLLoadingPlugin {

    public static final Logger LOGGER_RETWEAK_ASM = LoggerFactory.getLogger("ReTweak-ASM");

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                TransformerLoader.class.getName(),
                TransformerSimpleReloadableResourceManager.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        return ReTweakInternalModContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return ReTweakSetup.class.getName();
    }

    @Override
    public void injectData(final Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    public static final class ReTweakInternalModContainer implements ModContainer {

        private ModMetadata modMetadata;

        @Override
        public String getModId() {
            return "ReTweak";
        }

        @Override
        public String getName() {
            return "ReTweak";
        }

        @Override
        public String getVersion() {
            return "@VERSION@";
        }

        @Override
        public File getSource() {
            return null;
        }

        @Override
        public ModMetadata getMetadata() {
            if (modMetadata == null) {
                modMetadata = new ModMetadata();
                modMetadata.modId = getModId();
                modMetadata.name = getName();
                modMetadata.version = getVersion();
                modMetadata.authorList.add("Master801");
            }
            return modMetadata;
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
            return getVersion();
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
            return Disableable.NEVER;
        }

        @Override
        public String getGuiClassName() {
            return null;
        }

        @Override
        public List<String> getOwnedPackages() {
            return Lists.asList(
                    "org.slave.minecraft.retweak",
                    new String[] {
                            "org.slave.minecraft.retweak.asm",
                            "org.slave.minecraft.retweak.asm.transformers",
                            "org.slave.minecraft.retweak.asm.util",
                            "org.slave.minecraft.retweak.handlers",
                            "org.slave.minecraft.retweak.load",
                            "org.slave.minecraft.retweak.load.asm",
                            "org.slave.minecraft.retweak.load.mod",
                            "org.slave.minecraft.retweak.load.mod.asm",
                            "org.slave.minecraft.retweak.load.mod.discoverer",
                            "org.slave.minecraft.retweak.load.util",
                    }
            );
        }

    }

}
