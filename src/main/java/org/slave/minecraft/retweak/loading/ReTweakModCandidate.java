package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.discovery.ITypeDiscoverer;
import cpw.mods.fml.common.discovery.asm.ASMModParser;
import cpw.mods.fml.common.discovery.asm.ModAnnotation;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.resources.EnumMap;
import org.slave.lib.resources.wrappingdata.WrappingDataT.WrappingDataT2;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
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
public final class ReTweakModCandidate {

    @SuppressWarnings("unchecked")
    private static final EnumMap<SupportedGameVersion, WrappingDataT2<Type, String>> MOD_CLASS_MAP = new EnumMap<>(
            SupportedGameVersion.class,
            new WrappingDataT2[] {
                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_2_5,
                            new WrappingDataT2<>(
                                    Type.EXTENDS,
                                    "forge.NetworkMod"
                            )
                    ),

                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_4_6,
                            new WrappingDataT2<>(
                                    Type.ANNOTATION,
                                    Mod.class.getCanonicalName()
                            )
                    ),

                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_4_7,
                            new WrappingDataT2<>(
                                    Type.ANNOTATION,
                                    Mod.class.getCanonicalName()
                            )
                    ),

                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_5_2,
                            new WrappingDataT2<>(
                                    Type.ANNOTATION,
                                    Mod.class.getCanonicalName()
                            )
                    )
            }
    );

    private final SupportedGameVersion supportedGameVersion;
    private final File modFile;
    private ZipFile zipFile = null;

    private final ArrayList<ZipEntry> packages = new ArrayList<>();
    private final ArrayList<ZipEntry> classes = new ArrayList<>();

    private final EnumMap<Type, ArrayList<String>> modClasses;

    public ReTweakModCandidate(SupportedGameVersion supportedGameVersion, File modFile) {
        this.supportedGameVersion = supportedGameVersion;
        this.modFile = modFile;

        //noinspection unchecked
        WrappingDataT2<Type, ArrayList<String>>[] wt2 = new WrappingDataT2[Type.values().length];
        for(Type type : Type.values()) wt2[type.ordinal()] = new WrappingDataT2<>(type, new ArrayList<String>());

        modClasses = new EnumMap<>(Type.class, wt2);
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

    public void close() throws IOException {//We should close this... right?
        zipFile.close();
    }

    public ReTweakModContainer[] getModContainers() {
        int size = 0;
        Type[] values = Type.values();

        for(Type type : values) size += modClasses.get(type).size();

        ReTweakModContainer[] reTweakModContainers = new ReTweakModContainer[size];

        for(int i = 0, valuesLength = values.length; i < valuesLength; ++i) {
            Type type = values[i];
            ArrayList<String> actualModClasses = modClasses.get(type);

            for(String modClassPath : actualModClasses) {
                if (type == Type.EXTENDS) {//TODO
                    ReTweakResources.RETWEAK_LOGGER.info("\"EXTENDS\" is not supported (yet?)! {}", modClassPath);
                    continue;
                }
                try {
                    InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(modClassPath));

                    ASMModParser asmModParser = new ASMModParser(inputStream);
                    List<ModAnnotation> modAnnotations = new ArrayList<>(asmModParser.getAnnotations());

                    for(ModAnnotation modAnnotation : modAnnotations) {
                        if (!((Enum)modAnnotation.getType()).name().equals("CLASS")) continue;//I don't even care
                        if (modAnnotation.getASMType().getClassName().equals(ReTweakModCandidate.MOD_CLASS_MAP.get(supportedGameVersion).getObject2())) {
                            reTweakModContainers[i] = new ReTweakModContainer(
                                    (String)modAnnotation.getValues().get("modid"),
                                    (String)modAnnotation.getValues().get("name"),
                                    (String)modAnnotation.getValues().get("version")
                            );
                        }
                    }

                    inputStream.close();
                } catch(IOException e) {
                    ReTweakResources.RETWEAK_LOGGER.warn(
                            "Failed to get the mod container due to catching an exception! Exception: {}",
                            e
                    );
                }
            }
        }
        return reTweakModContainers;
    }

    File getModFile() {
        return modFile;
    }

    List<String> getClasses() {
        List<String> classesAsString = new ArrayList<>();
        for(ZipEntry zipEntry : classes) classesAsString.add(zipEntry.getName());
        return classesAsString;
    }

    List<String> getModClasses() {
        ArrayList<String> modClasses = new ArrayList<>();
        Enumeration<Entry<Type, ArrayList<String>>> enumeration = this.modClasses.enumeration();
        while(enumeration.hasMoreElements()) {
            List<String> classes =  enumeration.nextElement().getValue();
            for(int i = 0; i < classes.size(); ++i) {
                String _class = classes.get(i);
                final int index = _class.indexOf('.');
                if (index != -1) {
                    _class = _class.substring(
                            0,
                            index
                    );
                }
                _class = _class.replace(
                        '/',
                        '.'
                );
                classes.set(
                        i,
                        _class
                );
                modClasses.addAll(classes);
            }
        }
        return modClasses;
    }

    /**
     * Finds classes that have the {@link cpw.mods.fml.common.Mod} annotation
     */
    private void findModClass() throws IOException {
        if (zipFile == null) throw new IllegalStateException("Zip File field not found! -- \"findModClass\" was invoked before \"search\"!");

        for(ZipEntry zipEntry : classes) {
            InputStream zipEntryInputStream = zipFile.getInputStream(zipEntry);

            ClassReader classReader = new ClassReader(zipEntryInputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            WrappingDataT2<Type, String> wrappingDataT2 = ReTweakModCandidate.MOD_CLASS_MAP.get(supportedGameVersion);
            switch(wrappingDataT2.getObject1()) {
                case ANNOTATION:
                    if (classNode.visibleAnnotations != null && classNode.visibleAnnotations.size() > 0) {
                        for(AnnotationNode annotationNode : classNode.visibleAnnotations) {
                            if (annotationNode.desc.equals(
                                    org.objectweb.asm.Type.getObjectType(wrappingDataT2.getObject2().replace('.', '/')).getDescriptor()
                            )) {
                                modClasses.get(wrappingDataT2.getObject1()).add(zipEntry.getName());
                                break;
                            }
                        }
                    }
                    break;
                case EXTENDS:
                    if (classNode.superName.equals(wrappingDataT2.getObject2())) modClasses.get(wrappingDataT2.getObject1()).add(zipEntry.getName());
                    break;
            }

            zipEntryInputStream.close();
        }
    }

    enum Type {

        ANNOTATION,

        EXTENDS;

        Type() {
        }

    }

}
