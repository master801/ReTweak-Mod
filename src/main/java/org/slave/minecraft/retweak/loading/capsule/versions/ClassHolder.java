package org.slave.minecraft.retweak.loading.capsule.versions;

import com.google.common.collect.Sets;
import org.objectweb.asm.Type;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.ClassEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.FieldEntryBuilder.FieldEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.MethodEntryBuilder.MethodEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassInfoBuilder.ClassInfo;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Master on 4/19/2017 at 7:34 PM.
 *
 * @author Master
 */
public final class ClassHolder {

    private final List<ClassInfo> override;

    ClassHolder(final List<ClassInfo> override) {
        this.override = override;
    }

    public List<ClassInfo> getOverrideClasses() {
        return override;
    }

    public static final class ClassEntryBuilder {

        private static ClassEntryBuilder instance;

        private String from;
        private Class<?> to;

        private Set<FieldEntry> fieldEntries;
        private Set<MethodEntry> methodEntries;

        private ClassEntryBuilder() {
        }

        public ClassEntryBuilder setFrom(final String from) {
            if (from == null) return this;
            this.from = from;
            return this;
        }

        public ClassEntryBuilder setTo(final Class<?> to) {
            if (to == null) return this;
            this.to = to;
            return this;
        }

        public ClassEntryBuilder addFieldMapping(final FieldEntry fieldEntry) {
            if (fieldEntry == null) return this;
            if (fieldEntries == null) fieldEntries = Sets.newHashSet();
            fieldEntries.add(fieldEntry);
            return this;
        }

        public ClassEntryBuilder addMethodMapping(final MethodEntry methodEntry) {
            if (methodEntry == null) return this;
            if (methodEntries == null) methodEntries = Sets.newHashSet();
            methodEntries.add(methodEntry);
            return this;
        }

        public ClassEntry build() {
            if (from == null) throw new NullPointerException("From not set!");
//            if (to == null) throw new NullPointerException("To not set!");
            ClassEntry classEntry = new ClassEntry(
                from,
                to
            );
            classEntry.setFields(fieldEntries);
            classEntry.setMethods(methodEntries);

            //<editor-fold desc="Cleanup">
            from = null;
            to = null;
            fieldEntries = null;
            methodEntries = null;
            //</editor-fold>

            return classEntry;
        }

        public static ClassEntryBuilder instance() {
            if (ClassEntryBuilder.instance == null) ClassEntryBuilder.instance = new ClassEntryBuilder();
            return ClassEntryBuilder.instance;
        }

        public static final class ClassEntry {

            private final String from;
            private final Class<?> to;

            private Set<FieldEntry> fieldEntries;
            private Set<MethodEntry> methodEntries;

            private ClassEntry(final String from, final Class<?> to) {
                this.from = from;
                this.to = to;
            }

            public String getFrom() {
                return from;
            }

            public Class<?> getTo() {
                return to;
            }

            public Set<FieldEntry> getFields() {
                return fieldEntries;
            }

            void setFields(final Set<FieldEntry> fieldEntries) {
                this.fieldEntries = fieldEntries;
            }

            public FieldEntry getField(final String name, final String desc) {
                if (fieldEntries == null) return null;
                Type descType = Type.getType(desc);

                if (descType == null) return null;

                for(FieldEntry fieldEntry : fieldEntries) {
                    if (fieldEntry.getObfuscatedName().equals(name) && descType.equals(fieldEntry.getFromDescType())) return fieldEntry;
                }
                return null;
            }

            public void setMethods(final Set<MethodEntry> methodEntries) {
                this.methodEntries = methodEntries;
            }

            public MethodEntry getMethod(final String name, final String desc) {
                if (methodEntries == null) return null;

                Type returnType = Type.getReturnType(desc);
                Type[] argTypes = Type.getArgumentTypes(desc);

                for(MethodEntry methodEntry : methodEntries) {
                    boolean descMatches = returnType.equals(methodEntry.getReturnDescType());
                    if (methodEntry.getArgumentsDescTypes() != null) {
                        descMatches = descMatches && argTypes.length == methodEntry.getArgumentsDescTypes().length;
                        descMatches = descMatches && Arrays.equals(argTypes, methodEntry.getArgumentsDescTypes());
                    }

                    if (methodEntry.getObfuscatedName().equals(name) && descMatches) return methodEntry;
                }
                return null;
            }
        }

        public static final class FieldEntryBuilder {

            private static FieldEntryBuilder instance;

            private String obfuscatedName;
            private String deobfuscatedName;
            private Type fromDescType;
            private Type toDescType;

            private FieldEntryBuilder() {
            }

            public FieldEntryBuilder setObfuscatedName(final String obfuscatedName) {
                this.obfuscatedName = obfuscatedName;
                return this;
            }

            public FieldEntryBuilder setDeobfuscatedName(final String deobfuscatedName) {
                this.deobfuscatedName = deobfuscatedName;
                return this;
            }

            public FieldEntryBuilder setFromDescType(final Type type) {
                this.fromDescType = type;
                return this;
            }

            public FieldEntryBuilder setToDescType(final Type type) {
                this.toDescType = type;
                return this;
            }

            public FieldEntryBuilder setDeobfuscatedNameThroughField(final Class<?> clazz, final Object fieldValue) {
                if (fieldValue == null) return this;
                for(Field field : clazz.getDeclaredFields()) {
                    Object reflectedFieldValue;
                    try {
                        reflectedFieldValue = ReflectionHelper.getFieldValue(
                            field,
                            fieldValue
                        );
                    } catch(IllegalAccessException e) {
                        ReTweakResources.RETWEAK_LOGGER.error(
                            String.format(
                                "Caught exception while getting deobfuscated name through class \"%s\" field \"%s\"!",
                                clazz.getName(),
                                field.getName()
                            ),
                            e
                        );
                        return this;
                    }
                    if (reflectedFieldValue == fieldValue) {
                        deobfuscatedName = field.getName();
                        break;
                    }
                }
                return this;
            }

            public FieldEntry build() {
                if (obfuscatedName == null) throw new NullPointerException("Obfuscated name not set!");
                if (deobfuscatedName == null) throw new NullPointerException("Deobfuscated name not set!");
                FieldEntry fieldEntry = new FieldEntry(
                    obfuscatedName,
                    deobfuscatedName,
                    fromDescType,
                    toDescType
                );

                //<editor-fold desc="Cleanup">
                this.obfuscatedName = null;
                this.deobfuscatedName = null;
                this.fromDescType = null;
                this.toDescType = null;
                //</editor-fold>

                return fieldEntry;
            }

            public static FieldEntryBuilder instance() {
                if (FieldEntryBuilder.instance == null) FieldEntryBuilder.instance = new FieldEntryBuilder();
                return FieldEntryBuilder.instance;
            }

            public static final class FieldEntry {

                private final String obfuscatedName;
                private final String deobfuscatedName;

                private final org.objectweb.asm.Type fromDescType;
                private final org.objectweb.asm.Type toDescType;

                FieldEntry(final String obfuscatedName, final String deobfuscatedName, final org.objectweb.asm.Type fromDescType, final org.objectweb.asm.Type toDescType) {
                    this.obfuscatedName = obfuscatedName;
                    this.deobfuscatedName = deobfuscatedName;
                    this.fromDescType = fromDescType;
                    this.toDescType = toDescType;
                }

                public String getObfuscatedName() {
                    return obfuscatedName;
                }

                public String getDeobfuscatedName() {
                    return deobfuscatedName;
                }

                public org.objectweb.asm.Type getFromDescType() {
                    return fromDescType;
                }

                public org.objectweb.asm.Type getToDescType() {
                    return toDescType;
                }

            }

        }

        public static final class MethodEntryBuilder {

            private static MethodEntryBuilder instance;

            private String obfuscatedName;
            private String deobfuscatedName;

            private Type descReturnType;
            private Type[] descArgumentTypes;

            private MethodEntryBuilder() {
            }

            public MethodEntryBuilder setObfuscatedName(final String obfuscatedName) {
                this.obfuscatedName = obfuscatedName;
                return this;
            }

            public MethodEntryBuilder setDeobfuscatedName(final String deobfuscatedName) {
                this.deobfuscatedName = deobfuscatedName;
                return this;
            }

            public MethodEntryBuilder setDescReturnType(final Type descReturnType) {
                this.descReturnType = descReturnType;
                return this;
            }

            public MethodEntryBuilder setDescArgumentTypes(final Type[] descArgumentTypes) {
                this.descArgumentTypes = descArgumentTypes;
                return this;
            }

            public MethodEntry build() {
                if (obfuscatedName == null) throw new NullPointerException("Obfuscated name not set!");
                if (deobfuscatedName == null) throw new NullPointerException("Deobfuscated name not set!");
                if (descReturnType == null) throw new NullPointerException("Desc return type not set!");
                if (descArgumentTypes == null) throw new NullPointerException("Argument return types not set!");

                MethodEntry methodEntry = new MethodEntry(obfuscatedName, deobfuscatedName, descReturnType, descArgumentTypes);

                //<editor-fold desc="Cleanup">
                obfuscatedName = null;
                deobfuscatedName = null;
                descReturnType = null;
                descArgumentTypes = null;
                //</editor-fold>

                return methodEntry;
            }

            public static MethodEntryBuilder instance() {
                if (MethodEntryBuilder.instance == null) MethodEntryBuilder.instance = new MethodEntryBuilder();
                return MethodEntryBuilder.instance;
            }

            public static final class MethodEntry {


                private String obfuscatedName;
                private String deobfuscatedName;

                private Type descReturnType;
                private Type[] descArgumentsType;

                private MethodEntry(final String obfuscatedName, final String deobfuscatedName, final Type descReturnType, final Type[] descArgumentsType) {
                    this.obfuscatedName = obfuscatedName;
                    this.deobfuscatedName = deobfuscatedName;
                    this.descReturnType = descReturnType;
                    this.descArgumentsType = descArgumentsType;
                }

                public String getObfuscatedName() {
                    return obfuscatedName;
                }

                public String getDeobfuscatedName() {
                    return deobfuscatedName;
                }

                public Type getReturnDescType() {
                    return descReturnType;
                }

                public Type[] getArgumentsDescTypes() {
                    return descArgumentsType;
                }

            }

        }

    }

    public static final class ClassInfoBuilder {

        private static ClassInfoBuilder instance;

        private ClassEntry classEntry;

        private ClassInfoBuilder() {
        }

        public ClassInfoBuilder setClassEntry(final ClassEntry classEntry) {
            this.classEntry = classEntry;
            return this;
        }

        public ClassInfo build() {
            if (classEntry == null) throw new NullPointerException("Class entry not set!");

            ClassInfo classInfo = new ClassInfo(classEntry);

            //<editor-fold desc="Cleanup">
            classEntry = null;
            //</editor-fold>

            return classInfo;
        }

        public static ClassInfoBuilder instance() {
            if (ClassInfoBuilder.instance == null) ClassInfoBuilder.instance = new ClassInfoBuilder();
            return ClassInfoBuilder.instance;
        }

        public static final class ClassInfo {

            private final ClassEntry classEntry;

            private ClassInfo(final ClassEntry classEntry) {
                this.classEntry = classEntry;
            }

            public ClassEntry getClassEntry() {
                return classEntry;
            }

        }

    }

    /*
    private final List<String> classes;
    private final List<Class<?>> overrideClassList;

    ClassHolder(final List<String> classes, final List<Class<?>> overrideClassList) {
        this.classes = Collections.unmodifiableList(
                classes
        );
        this.overrideClassList = Collections.unmodifiableList(
                overrideClassList
        );
    }

    public List<String> getClasses() {
        return classes;
    }

    public List<Class<?>> getOverrideClassList() {
        return overrideClassList;
    }
    */

}
