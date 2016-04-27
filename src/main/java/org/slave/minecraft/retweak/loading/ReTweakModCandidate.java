package org.slave.minecraft.retweak.loading;

import org.objectweb.asm.ClassReader;
import org.slave.lib.helpers.IOHelper;
import org.slave.minecraft.retweak.asm.visitors.ModClassVisitor;
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
                null,//We're only reading that it is a mod, no need for a functioning class visitor
                gameVersion
        );
        classReader.accept(
                modVisitor,
                0
        );

        if (modVisitor.isMod()) modClasses.add(classReader.getClassName() + ".class");
    }

    public boolean isValid() throws IOException {
        if (!modClasses.isEmpty()) return false;//Has previously invoked method???
        ZipFile zipFile = new ZipFile(file);
        for(String _class : classes) {
            InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(_class));
            findModClasses(IOHelper.toByteArray(inputStream));
            inputStream.close();
        }
        zipFile.close();
        return !modClasses.isEmpty();
    }

    GameVersion getGameVersion() {
        return gameVersion;
    }

    File getFile() {
        return file;
    }

    String getPath() {
        return file.getPath();
    }

    List<String> getModClasses() {
        return modClasses;
    }

}
