package org.slave.minecraft.retweak.loading.mod;

import org.slave.lib.helpers.IterableHelper;
import org.slave.lib.resources.ASMAnnotation;
import org.slave.lib.resources.ASMTable;
import org.slave.lib.resources.ASMTable.TableClass;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.loading.capsule.Type;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.ZipFile;

/**
 * Created by Master on 4/26/2016 at 3:41 PM.
 *
 * @author Master
 */
public final class ReTweakModCandidate {

    private final GameVersion gameVersion;
    private final File file;

    private ASMTable asmTable;

    private final ArrayList<String> classes = new ArrayList<>();
    private final ArrayList<TableClass> modClasses = new ArrayList<>();

    private String[] modids;
    private boolean enabled = true;

    ReTweakModCandidate(final GameVersion gameVersion, final File file) {
        this.gameVersion = gameVersion;
        this.file = file;
        try {
            ZipFile zipFile = new ZipFile(file);
            asmTable = new ASMTable();
            asmTable.load(zipFile);
            zipFile.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        if (asmTable == null) throw new NullPointerException("Read ASM Table incorrectly?");
    }

    public void find() {
        for(TableClass tableClass : asmTable.getTableClasses()) classes.add(tableClass.getName());
    }

    private void findModClasses() {
        for(TableClass tableClass : asmTable.getTableClasses()) {
            Entry<Type, String> entry = gameVersion.getModType();
            switch(entry.getKey()) {
                case EXTENDS:
                    if (tableClass.getSuperName().equals(entry.getValue())) modClasses.add(tableClass);
                    break;
                case ANNOTATION:
                    if (tableClass.getAnnotations() != null) {
                        for(ASMAnnotation asmAnnotation : tableClass.getAnnotations()) {
                            if (asmAnnotation.getDesc().equals(entry.getValue())) {
                                modClasses.add(tableClass);
                                break;
                            }
                        }
                    }
                    break;
            }
        }
    }

    public boolean isValid() throws IOException {
        if (!modClasses.isEmpty()) return false;//Has previously invoked method?
        findModClasses();
        return !modClasses.isEmpty();
    }

    public String[] getModIds() {
        if (modids == null && !modClasses.isEmpty()) {
            ArrayList<String> modids = new ArrayList<>();
            for(TableClass tableClass : asmTable.getTableClasses()) {
                Entry<Type, String> entry = gameVersion.getModType();
                switch(entry.getKey()) {
                    case EXTENDS:
                        if (tableClass.getSuperName().equals(entry.getValue())) {
                            modids.add(tableClass.getName());//Modids were literally the name of the class...?
                        }
                        break;
                    case ANNOTATION:
                        if (tableClass.getAnnotations() != null) {
                            for(ASMAnnotation asmAnnotation : tableClass.getAnnotations()) {
                                if (asmAnnotation.getDesc().equals(entry.getValue())) {
                                    modids.add(
                                            (String)asmAnnotation.getValues().get("modid")
                                    );
                                }
                            }
                        }
                        break;
                }
                if (!IterableHelper.isNullOrEmpty(modids)) this.modids = modids.toArray(new String[modids.size()]);
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

    public GameVersion getGameVersion() {
        return gameVersion;
    }

    public File getSource() {
        return file;
    }

    List<String> getClasses() {
        return classes;
    }

    List<TableClass> getModClasses() {
        return modClasses;
    }

    ASMTable getASMTable() {
        return asmTable;
    }

}
