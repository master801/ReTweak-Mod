package org.slave.minecraft.retweak.loading.mod;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ContainerType;
import cpw.mods.fml.common.discovery.ModCandidate;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.lib.resources.ASMAnnotation;
import org.slave.lib.resources.ASMTable;
import org.slave.lib.resources.ASMTable.TableClass;
import org.slave.lib.resources.ASMTable.TableClass.TableField;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Created by Master on 6/1/2017 at 8:42 AM.
 *
 * @author Master
 */
public final class ReTweakModCandidate extends ModCandidate {

    private final GameVersion gameVersion;
    private ASMTable asmTable;

    public ReTweakModCandidate(final GameVersion gameVersion, final File classPathRoot, final File modContainer, final ContainerType sourceType) {
        super(classPathRoot, modContainer, sourceType);
        this.gameVersion = gameVersion;
    }

    @Override
    public List<ModContainer> explore(final ASMDataTable table) {
        setASMDataTable(table);

        List<ModContainer> mods = findMods(table);
        setMods(mods);
        return mods;
    }

    private List<ModContainer> findMods(final ASMDataTable asmDataTable) {
        MetadataCollection metadataCollection;

        try {
            JarFile jarFile = new JarFile(getModContainer());

            ZipEntry zipEntry = jarFile.getEntry("mcmod.info");
            if (zipEntry != null) {
                InputStream inputStream = jarFile.getInputStream(zipEntry);
                metadataCollection = MetadataCollection.from(
                    inputStream,
                    getModContainer().getName()
                );
                inputStream.close();
            } else {
                metadataCollection = MetadataCollection.from(
                    null,
                    ""
                );
            }

            asmTable = new ASMTable();
            asmTable.load(jarFile);

            jarFile.close();
        } catch(IOException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                "Caught exception while finding mods!",
                e
            );
            return null;
        }

        List<ModContainer> modContainers = new ArrayList<>();

        for(TableClass tableClass : asmTable.getTableClasses()) {
            ModContainer modContainer = null;

            addClassEntry(tableClass.getName() + ".class");//Add ".class" for ModCandidate to cut it out (so no errors will occur)

            switch(gameVersion.getModType().getKey()) {
                case EXTENDS:
                    if (tableClass.getSuperName().equals(gameVersion.getModType().getValue())) {
                        modContainer = new ReTweakModContainer(
                            gameVersion,
                            tableClass.getName().replace('/', '.'),
                            this,
                            Maps.newHashMap()//TODO
                        );
                    }
                    break;
                case ANNOTATION:
                    if (tableClass.getAnnotations() != null) {
                        for(ASMAnnotation asmAnnotation : tableClass.getAnnotations()) {
                            String className = gameVersion.getModType().getValue().toString();
                            className = className.substring(
                                className.indexOf(' ') + 1
                            );
                            className = "L" + className.replace('.', '/') + ";";
                            if (asmAnnotation.getDesc().equals(className)) {
                                modContainer = new ReTweakModContainer(
                                    gameVersion,
                                    tableClass.getName().replace('/', '.'),
                                    this,
                                    asmAnnotation.getValues()
                                );
                            }
                        }
                    }
                    break;
            }

            if (tableClass.getAnnotations() != null) {
                for(ASMAnnotation asmAnnotation : tableClass.getAnnotations()) {
                    asmDataTable.addASMData(//I don't understand the point of adding the asm data of annotations, but we'll do it anyway
                        this,
                        asmAnnotation.getDesc().substring(1, asmAnnotation.getDesc().length() - 1).replace('/', '.'),
                        tableClass.getName().replace('/', '.'),
                        tableClass.getName().replace('/', '.'),//Same as class name
                        asmAnnotation.getValues()
                    );
                }
            }

            if (tableClass.getFields() != null) {
                for(TableField tableField : tableClass.getFields()) {
                    if (tableField.getAnnotations() != null) {
                        for(ASMAnnotation asmAnnotation : tableField.getAnnotations()) {
                            asmDataTable.addASMData(//I don't know what I'm doing
                                this,
                                asmAnnotation.getDesc().substring(1, asmAnnotation.getDesc().length() - 1).replace('/', '.'),
                                tableClass.getName().replace('/', '.'),
                                tableField.getName(),
                                asmAnnotation.getValues()
                            );
                        }
                    }
                }
            }

            if (modContainer != null) {
                modContainer.bindMetadata(metadataCollection);
                asmDataTable.addContainer(modContainer);
                modContainers.add(modContainer);
            }
        }

        return modContainers;
    }

    /**
     * {@link cpw.mods.fml.common.discovery.ModCandidate#table}
     */
    private ASMDataTable getASMDataTable() {
        try {
            Field field = ReflectionHelper.getField(
                ModCandidate.class,
                "table"
            );
            return ReflectionHelper.getFieldValue(
                field,
                this
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                "Failed to get \"table\" field!",
                e
            );
            return null;
        }
    }

    /**
     * {@link cpw.mods.fml.common.discovery.ModCandidate#table}
     */
    private void setASMDataTable(final ASMDataTable asmDataTable) {
        try {
            Field field = ReflectionHelper.getField(
                ModCandidate.class,
                "table"
            );
            ReflectionHelper.setFieldValue(
                field,
                this,
                asmDataTable
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                "Failed to set \"table\" field!",
                e
            );
        }
    }

    /**
     * {@link cpw.mods.fml.common.discovery.ModCandidate#mods}
     */
    private List<ModContainer> getMods() {
        try {
            Field field = ReflectionHelper.getField(
                ModCandidate.class,
                "mods"
            );
            return ReflectionHelper.getFieldValue(
                field,
                this
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                "Failed to get \"mods\" field!",
                e
            );
            return null;
        }
    }

    /**
     * {@link cpw.mods.fml.common.discovery.ModCandidate#mods}
     */
    private void setMods(final List<ModContainer> mods) {
        try {
            Field field = ReflectionHelper.getField(
                ModCandidate.class,
                "mods"
            );
            ReflectionHelper.setFieldValue(
                field,
                this,
                mods
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                "Failed to set \"mods\" field!",
                e
            );
        }
    }

    public ASMTable getASMTable() {
        return asmTable;
    }

}
