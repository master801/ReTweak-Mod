package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.Type;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.BuilderMigrationField.MigrationField;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.BuilderMigrationMethod.MigrationMethod;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by Master on 7/12/2018 at 9:41 PM.
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MigrationClassBuilder {

    private static MigrationClassBuilder instance;

    private String from;
    private Class<?> to;

    private Set<MigrationField> fieldEntries;
    private Set<MigrationMethod> methodEntries;

    public MigrationClassBuilder from(final String from) {
        if (from == null) return this;
        this.from = from;
        return this;
    }

    public MigrationClassBuilder from(final Class<?> from) {
        if (from == null) return this;
        this.from = from.getName();
        return this;
    }

    public MigrationClassBuilder to(final Class<?> to) {
        if (to == null) return this;
        this.to = to;
        return this;
    }

    public MigrationClassBuilder addFieldMapping(final MigrationField fieldEntry) {
        if (fieldEntry == null) return this;
        if (fieldEntries == null) fieldEntries = Sets.newHashSet();
        fieldEntries.add(fieldEntry);
        return this;
    }

    public MigrationClassBuilder addMethodMapping(final MigrationMethod methodEntry) {
        if (methodEntry == null) return this;
        if (methodEntries == null) methodEntries = Sets.newHashSet();
        methodEntries.add(methodEntry);
        return this;
    }

    public MigrationClass build() {
        if (from == null) throw new NullPointerException("From not set!");
//            if (to == null) throw new NullPointerException("To not set!");
        MigrationClass migrationClass = new MigrationClass(from, to);
        migrationClass.setFields(fieldEntries);
        migrationClass.setMethods(methodEntries);

        //<editor-fold desc="Cleanup">
        from = null;
        to = null;
        fieldEntries = null;
        methodEntries = null;
        //</editor-fold>

        return migrationClass;
    }

    public static MigrationClassBuilder instance() {
        if (MigrationClassBuilder.instance == null) MigrationClassBuilder.instance = new MigrationClassBuilder();
        return MigrationClassBuilder.instance;
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class MigrationClass {

        @Getter
        private final String from;

        @Getter
        private final Class<?> to;

        private Set<MigrationField> fieldMigrations;
        private Set<MigrationMethod> methodMigrations;

        public static MigrationClassBuilder builder() {
            return MigrationClassBuilder.instance();
        }

        public Set<MigrationField> getFields() {
            return fieldMigrations;
        }

        void setFields(final Set<MigrationField> fieldEntries) {
            this.fieldMigrations = fieldEntries;
        }

        public MigrationField getField(final String name, final String desc) {
            if (fieldMigrations == null) return null;
            Type descType = Type.getType(desc);

            if (descType == null) return null;

            for (MigrationField fieldEntry: fieldMigrations) {
                if (fieldEntry.getObfuscatedName().equals(name) && descType.equals(fieldEntry.getFromDescType())) return fieldEntry;
            }
            return null;
        }

        public void setMethods(final Set<MigrationMethod> methodEntries) {
            this.methodMigrations = methodEntries;
        }

        public MigrationMethod getMethod(final String name, final String desc) {
            if (methodMigrations == null) return null;

            Type returnType = Type.getReturnType(desc);
            Type[] argTypes = Type.getArgumentTypes(desc);

            for (MigrationMethod methodEntry: methodMigrations) {
                boolean descMatches = returnType.equals(methodEntry.getObfuscatedReturnTypeDesc());
                if (methodEntry.getObfuscatedArgumentDescTypes() != null) {
                    descMatches = descMatches && argTypes.length == methodEntry.getObfuscatedArgumentDescTypes().length;
                    descMatches = descMatches && Arrays.equals(argTypes, methodEntry.getObfuscatedArgumentDescTypes());
                }

                if (methodEntry.getObfuscatedName().equals(name) && descMatches) return methodEntry;
            }
            return null;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BuilderMigrationField {

        private static BuilderMigrationField instance;

        private String obfuscatedName;
        private String deobfuscatedName;
        private Type fromDescType;
        private Type toDescType;

        public BuilderMigrationField setObfuscatedName(final String obfuscatedName) {
            this.obfuscatedName = obfuscatedName;
            return this;
        }

        public BuilderMigrationField setDeobfuscatedName(final String deobfuscatedName) {
            this.deobfuscatedName = deobfuscatedName;
            return this;
        }

        public BuilderMigrationField setFromDescType(final Type type) {
            this.fromDescType = type;
            return this;
        }

        public BuilderMigrationField setToDescType(final Type type) {
            this.toDescType = type;
            return this;
        }

        public BuilderMigrationField setDeobfuscatedNameThroughField(final Class<?> clazz, final Object fieldValue) {
            if (fieldValue == null) return this;
            for (Field field: clazz.getDeclaredFields()) {
                Object reflectedFieldValue;
                try {
                    reflectedFieldValue = ReflectionHelper.getFieldValue(
                            field,
                            fieldValue
                    );
                } catch (IllegalAccessException e) {
                    ReTweak.LOGGER_RETWEAK.error(
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

        public MigrationField build() {
            if (obfuscatedName == null) throw new NullPointerException("Obfuscated name not set!");
            if (deobfuscatedName == null) throw new NullPointerException("Deobfuscated name not set!");
            MigrationField fieldEntry = new MigrationField(
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

        public static BuilderMigrationField instance() {
            if (BuilderMigrationField.instance == null) BuilderMigrationField.instance = new BuilderMigrationField();
            return BuilderMigrationField.instance;
        }

        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class MigrationField {

            @Getter
            private final String obfuscatedName;

            @Getter
            private final String deobfuscatedName;

            @Getter
            private final Type fromDescType;

            @Getter
            private final Type toDescType;

            public static BuilderMigrationField builder() {
                return BuilderMigrationField.instance();
            }

        }

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BuilderMigrationMethod {

        private static BuilderMigrationMethod instance;

        private String obfuscatedName;
        private String deobfuscatedName;

        private Type obfuscatedDescReturnType;
        private Type[] obfuscatedDescArgumentTypes;

        private Type deobfuscatedDescReturnType;
        private Type[] deobfuscatedDescArgumentTypes;

        public BuilderMigrationMethod setObfuscatedName(final String obfuscatedName) {
            this.obfuscatedName = obfuscatedName;
            return this;
        }

        public BuilderMigrationMethod setDeobfuscatedName(final String deobfuscatedName) {
            this.deobfuscatedName = deobfuscatedName;
            return this;
        }

        public BuilderMigrationMethod setObfuscatedDescReturnType(final Type descReturnType) {
            this.obfuscatedDescReturnType = descReturnType;
            return this;
        }

        public BuilderMigrationMethod setObfuscatedDescArgumentTypes(final Type... descArgumentTypes) {
            this.obfuscatedDescArgumentTypes = descArgumentTypes;
            return this;
        }

        public BuilderMigrationMethod setDeobfuscatedDescReturnType(final Type descReturnType) {
            this.deobfuscatedDescReturnType = descReturnType;
            return this;
        }

        public BuilderMigrationMethod setDeobfuscatedDescArgumentTypes(final Type... descArgumentTypes) {
            this.deobfuscatedDescArgumentTypes = descArgumentTypes;
            return this;
        }

        public MigrationMethod build() {
            if (obfuscatedName == null) throw new NullPointerException("Obfuscated name not set!");
            if (deobfuscatedName == null) throw new NullPointerException("Deobfuscated name not set!");

            //Check if obfuscated types are null
            if (obfuscatedDescReturnType == null) throw new NullPointerException("Obfuscated desc return type not set!");
            if (obfuscatedDescArgumentTypes == null) obfuscatedDescArgumentTypes = new Type[0];

            //Check if deobfuscated types are null
            if (deobfuscatedDescReturnType == null) throw new NullPointerException("Deobfuscated desc return type not set!");
            if (deobfuscatedDescArgumentTypes == null) deobfuscatedDescArgumentTypes = new Type[0];

            MigrationMethod methodEntry = new MigrationMethod(
                    //Names
                    obfuscatedName,
                    deobfuscatedName,

                    //Obfuscated types
                    obfuscatedDescReturnType,
                    obfuscatedDescArgumentTypes,

                    //Deobfuscated types
                    deobfuscatedDescReturnType,
                    deobfuscatedDescArgumentTypes
            );

            //<editor-fold desc="Cleanup">
            obfuscatedName = null;
            deobfuscatedName = null;

            obfuscatedDescReturnType = null;
            obfuscatedDescArgumentTypes = null;

            deobfuscatedDescReturnType = null;
            deobfuscatedDescArgumentTypes = null;
            //</editor-fold>

            return methodEntry;
        }

        public static BuilderMigrationMethod instance() {
            if (BuilderMigrationMethod.instance == null) BuilderMigrationMethod.instance = new BuilderMigrationMethod();
            return BuilderMigrationMethod.instance;
        }

        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class MigrationMethod {

            @Getter
            private String obfuscatedName;

            @Getter
            private String deobfuscatedName;

            @Getter
            private Type obfuscatedReturnTypeDesc;

            @Getter
            private Type[] obfuscatedArgumentDescTypes;

            @Getter
            private Type deobfuscatedReturnTypeDesc;

            @Getter
            private Type[] deobfuscatedArgumentDescTypes;

            public static BuilderMigrationMethod builder() {
                return BuilderMigrationMethod.instance();
            }

        }

    }

}
