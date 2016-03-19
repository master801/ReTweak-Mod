package org.slave.minecraft.retweak.tweaking;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.discovery.ITypeDiscoverer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.helpers.IOHelper;
import org.slave.lib.resources.EnumMap;
import org.slave.lib.resources.wrappingdata.WrappingDataT.WrappingDataT2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>
 *     {@link cpw.mods.fml.common.discovery.ModCandidate}
 * </p>
 *
 * Created by Master801 on 3/19/2016 at 9:17 AM.
 *
 * @author Master801
 */
public final class ReTweakModContainer {

    @SuppressWarnings("unchecked")
    private static final EnumMap<SupportedGameVersion, WrappingDataT2<ValidType, String>> MOD_CLASS_MAP = new EnumMap<>(
            SupportedGameVersion.class,
            new WrappingDataT2[] {
                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_2_5,
                            new WrappingDataT2<>(
                                    ValidType.EXTENDS,
                                    "forge.NetworkMod"
                            )
                    ),

                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_4_6,
                            new WrappingDataT2<>(
                                    ValidType.ANNOTATION,
                                    Mod.class.getCanonicalName()
                            )
                    ),

                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_4_7,
                            new WrappingDataT2<>(
                                    ValidType.ANNOTATION,
                                    Mod.class.getCanonicalName()
                            )
                    ),
            }
    );

    private final SupportedGameVersion supportedGameVersion;
    private final File modFile;
    private ZipFile zipFile = null;

    private final ArrayList<ZipEntry> packages = new ArrayList<>();
    private final ArrayList<ZipEntry> classes = new ArrayList<>();

    private final ArrayList<String> modClasses = new ArrayList<>();

    public ReTweakModContainer(SupportedGameVersion supportedGameVersion, File modFile) {
        this.supportedGameVersion = supportedGameVersion;
        this.modFile = modFile;
    }

    public void search() throws IOException {
        zipFile = new ZipFile(modFile);

        Enumeration<? extends ZipEntry> zipEntryEnumerator = zipFile.entries();
        while(zipEntryEnumerator.hasMoreElements()) {
            ZipEntry zipEntry = zipEntryEnumerator.nextElement();
            if (zipEntry.isDirectory()) {
                packages.add(zipEntry);
            } else if (ITypeDiscoverer.classFile.matcher(zipEntry.getName()).matches()) {
                classes.add(zipEntry);
            }
        }

        findModClass();
    }

    public void close() throws IOException {
        zipFile.close();
    }

    /**
     * Finds classes that have the {@link cpw.mods.fml.common.Mod} annotation
     */
    private void findModClass() throws IOException {
        if (zipFile == null) throw new IllegalStateException("Zip File field not found! -- \"findMainClass\" was invoked before \"search\"!");

        for(ZipEntry zipEntry : classes) {
            InputStream zipEntryInputStream = zipFile.getInputStream(zipEntry);

            ClassReader classReader = new ClassReader(IOHelper.toByteArray(zipEntryInputStream));
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            WrappingDataT2<ValidType, String> wrappingDataT2 = ReTweakModContainer.MOD_CLASS_MAP.get(supportedGameVersion);
            switch(wrappingDataT2.getObject1()) {
                case ANNOTATION:
                    if (classNode.visibleAnnotations != null && classNode.visibleAnnotations.size() > 0) {
                        for(AnnotationNode annotationNode : classNode.visibleAnnotations) {
                            if (annotationNode.desc.equals(Type.getObjectType(wrappingDataT2.getObject2()).getDescriptor())) {
                                modClasses.add(zipEntry.getName());
                                break;
                            }
                        }
                    }
                    break;
                case EXTENDS:
                    if (classNode.superName.equals(wrappingDataT2.getObject2())) modClasses.add(zipEntry.getName());
                    break;
            }

            zipEntryInputStream.close();
        }
    }

    public enum ValidType {

        ANNOTATION,

        EXTENDS;

        ValidType() {
        }

    }

}
