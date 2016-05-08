package org.slave.minecraft.retweak.loading;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.helpers.IOHelper;
import org.slave.lib.resources.ASMAnnotation;
import org.slave.minecraft.retweak.asm.visitors.ModClassVisitor;
import org.slave.minecraft.retweak.asm.visitors.ModClassVisitor.Type;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Master on 4/26/2016 at 3:41 PM.
 *
 * @author Master
 */
public final class ReTweakModCandidate {

    private static final Pattern CLASS_PATTERN = Pattern.compile(
            "(.+)(\\.class$)",
            Pattern.MULTILINE
    );

    private final GameVersion gameVersion;
    private final File file;

    private final ArrayList<String> classes = new ArrayList<>(), packages = new ArrayList<>();
    private final ArrayList<String> modClasses = new ArrayList<>();

    private String[] modids;
    private boolean enabled = true;
    private Type type;

    ReTweakModCandidate(GameVersion gameVersion, File file) {
        this.gameVersion = gameVersion;
        this.file = file;
    }

    public void find() throws IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> zipEntryEnumeration = zipFile.entries();
        while(zipEntryEnumeration.hasMoreElements()) {
            ZipEntry zipEntry = zipEntryEnumeration.nextElement();
            if (zipEntry.isDirectory()) {
                packages.add(zipEntry.getName());
            }
            if (ReTweakModCandidate.CLASS_PATTERN.matcher(zipEntry.getName()).matches()) {
                InputStream zipEntryInputStream = zipFile.getInputStream(zipEntry);
                classes.add(zipEntry.getName());
                zipEntryInputStream.close();
            }
        }
        zipFile.close();
    }

    private void findModClasses(final byte[] classBytes) {
        ClassReader classReader = new ClassReader(classBytes);
        ModClassVisitor modVisitor = new ModClassVisitor(
                null,//No need for actual class visitor
                gameVersion
        );
        classReader.accept(
                modVisitor,
                0
        );
        type = modVisitor.getType();

        if (modVisitor.isMod()) modClasses.add(classReader.getClassName() + ".class");
    }

    public boolean isValid() throws IOException {
        if (!modClasses.isEmpty()) return false;//Has previously invoked method?
        ZipFile zipFile = new ZipFile(file);
        for(String _class : classes) {
            InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(_class));
            findModClasses(IOHelper.toByteArray(inputStream));
            inputStream.close();
        }
        zipFile.close();
        return !modClasses.isEmpty();
    }

    public String[] getModIds() {
        if (modids == null && !modClasses.isEmpty()) {
            try {
                ZipFile zipFile = new ZipFile(file);
                for(String modClass : modClasses) {
                    ZipEntry modZipEntry = zipFile.getEntry(modClass);
                    ClassReader classReader = new ClassReader(zipFile.getInputStream(modZipEntry));
                    ClassNode classNode = new ClassNode();
                    classReader.accept(
                            classNode,
                            0
                    );
                    switch(type) {
                        case EXTENDS:
                            //Yes, modids were literally the name of the class...
                            modids = new String[] {
                                    classNode.name
                            };
                            break;
                        case ANNOTATION:
                            if (classNode.visibleAnnotations != null) {
                                List<String> modidList = new ArrayList<>();
                                for(AnnotationNode annotationNode : classNode.visibleAnnotations) {
                                    if (annotationNode.desc.equals(ModClassVisitor.getDesc(getGameVersion()))) {//TODO Check if annotation or extends
                                        modidList.add((String)new ASMAnnotation(annotationNode).get().get("modid"));
                                    }
                                }
                                modids = modidList.toArray(new String[modidList.size()]);
                            }
                            break;
                    }
                }
                zipFile.close();
            } catch(IOException e) {
                //Ignore
            }
        }
        return modids;
    }

    public boolean isEnabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    GameVersion getGameVersion() {
        return gameVersion;
    }

    File getFile() {
        return file;
    }

    List<String> getModClasses() {
        return modClasses;
    }

}
