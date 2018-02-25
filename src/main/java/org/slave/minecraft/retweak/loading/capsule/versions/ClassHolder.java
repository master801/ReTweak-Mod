package org.slave.minecraft.retweak.loading.capsule.versions;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ClassHolder {

    @Getter
    private final List<ClassInfo> overrideClasses;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ClassEntryBuilder {

        private static ClassEntryBuilder instance;

        private String from;
        private Class<?> to;

        private Set<FieldEntry> fieldEntries;
        private Set<MethodEntry> methodEntries;

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

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class ClassEntry {

            @Getter
            private final String from;

            @Getter
            private final Class<?> to;

            private Set<FieldEntry> fieldEntries;
            private Set<MethodEntry> methodEntries;

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
                    boolean descMatches = returnType.equals(methodEntry.getReturnTypeDesc());
                    if (methodEntry.getArgumentDescTypes() != null) {
                        descMatches = descMatches && argTypes.length == methodEntry.getArgumentDescTypes().length;
                        descMatches = descMatches && Arrays.equals(argTypes, methodEntry.getArgumentDescTypes());
                    }

                    if (methodEntry.getObfuscatedName().equals(name) && descMatches) return methodEntry;
                }
                return null;
            }
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class FieldEntryBuilder {

            private static FieldEntryBuilder instance;

            private String obfuscatedName;
            private String deobfuscatedName;
            private Type fromDescType;
            private Type toDescType;

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
                        setDeobfuscatedName(field.getName());
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

            @AllArgsConstructor(access = AccessLevel.PRIVATE)
            public static final class FieldEntry {

                @Getter
                private final String obfuscatedName;

                @Getter
                private final String deobfuscatedName;

                @Getter
                private final org.objectweb.asm.Type fromDescType;

                @Getter
                private final org.objectweb.asm.Type toDescType;

            }

        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class MethodEntryBuilder {

            private static MethodEntryBuilder instance;

            private String obfuscatedName;
            private String deobfuscatedName;

            private Type descReturnType;
            private Type[] descArgumentTypes;

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

            @AllArgsConstructor(access = AccessLevel.PRIVATE)
            public static final class MethodEntry {

                @Getter
                private String obfuscatedName;

                @Getter
                private String deobfuscatedName;

                @Getter
                private Type returnTypeDesc;

                @Getter
                private Type[] argumentDescTypes;

            }

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ClassInfoBuilder {

        private static ClassInfoBuilder instance;

        private ClassEntry classEntry;

        public ClassInfoBuilder setClassEntry(final ClassEntry classEntry) {
            this.classEntry = classEntry;
            return this;
        }

        public ClassInfo build() {
            if (classEntry == null) throw new NullPointerException("Class entry not set!");
            ClassInfo classInfo = new ClassInfo(classEntry);
            cleanup();
            return classInfo;
        }

        private void cleanup() {
            classEntry = null;
        }

        public static ClassInfoBuilder instance() {
            if (ClassInfoBuilder.instance == null) ClassInfoBuilder.instance = new ClassInfoBuilder();
            return ClassInfoBuilder.instance;
        }

        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class ClassInfo {

            @Getter
            private final ClassEntry classEntry;

        }

    }

}
