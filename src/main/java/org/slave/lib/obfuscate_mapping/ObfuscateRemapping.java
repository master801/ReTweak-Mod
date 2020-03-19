package org.slave.lib.obfuscate_mapping;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMapping.DescMapping.DescMappingImpl;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMapping.NameMapping.NameMappingImpl;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMapping.SignatureMapping.SignatureMappingImpl;
import org.slave.lib.resources.Obfuscation;
import scala.actors.threadpool.Arrays;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Master on 9/12/2018 at 11:27 PM.
 *
 * @author Master
 */
public final class ObfuscateRemapping {

    public interface ObfuscationMapping {

        NameMapping getNameMapping();

        SignatureMapping getSignatureMapping();

        interface NameMapping {

            /**
             * @param obfuscation
             * @param name Must have slashes and not periods in package name if present.
             * Bad: "java.lang.Object"
             * Good: "java/lang/Object"
             *
             * @return
             */
            NameMapping setName(final Obfuscation obfuscation, final String name);

            /**
             * @param obfuscation
             * @return If package name is present, it must be returned as slashes and not periods.
             * Bad: "java.lang.Object"
             * Good: "java/lang/Object"
             */
            String getName(final Obfuscation obfuscation);

            /**
             * Copies from the specified {@link org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMapping.NameMapping} to this
             *
             * @param nameMapping
             * @return
             */
            NameMapping from(final NameMapping nameMapping);

            static FactoryNameMapping factory() {
                if (FactoryNameMapping.instance == null)  FactoryNameMapping.instance = new FactoryNameMapping();
                return FactoryNameMapping.instance;
            }

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            final class FactoryNameMapping {

                private static FactoryNameMapping instance = null;

                private final EnumMap<Obfuscation, String> names = new EnumMap<>(Obfuscation.class);

                public FactoryNameMapping setName(final String name) {
                    for(Obfuscation obfuscation : Obfuscation.values()) {
                        setName(obfuscation, name);
                    }
                    return this;
                }

                public FactoryNameMapping setName(final Obfuscation obfuscation, final String name) {
                    names.put(obfuscation, name);
                    return this;
                }

                public NameMapping create() {
                    //Do not create NameMapping if no names are set
                    if (names.isEmpty()) return null;

                    NameMapping nameMapping = new NameMappingImpl();

                    //Copy from our 'names' field into the NameMapping
                    for(Map.Entry<Obfuscation, String> entry : names.entrySet()) nameMapping.setName(entry.getKey(), entry.getValue());

                    //Cleanup
                    names.clear();

                    return nameMapping;
                }

            }

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            final class NameMappingImpl implements NameMapping {

                private final String[] names = new String[Obfuscation.values().length];

                @Override
                public NameMapping setName(final Obfuscation obfuscation, final String name) {
                    if (obfuscation == null) return this;
                    names[obfuscation.ordinal()] = name;
                    return this;
                }

                @Override
                public String getName(final Obfuscation obfuscation) {
                    if (obfuscation == null) return null;
                    return names[obfuscation.ordinal()];
                }

                @Override
                public NameMapping from(final NameMapping nameMapping) {
                    //Do not copy from given NameMapping if null
                    if (nameMapping == null) return this;

                    //Clear our 'names' array
                    Arrays.fill(names, null);

                    //Update our 'names' array from the given NameMapping
                    for(Obfuscation obfuscation : Obfuscation.values()) {
                        String name = nameMapping.getName(obfuscation);
                        names[obfuscation.ordinal()] = name;
                    }
                    return this;
                }

                @Override
                public boolean equals(final Object obj) {
                    if (obj instanceof NameMappingImpl) {
                        NameMappingImpl nameMapping = (NameMappingImpl)obj;
                        boolean equals = true;
                        for(Obfuscation obf : Obfuscation.VALUES) {
                            if (!nameMapping.getName(obf).equals(getName(obf))) {
                                equals = false;
                                break;
                            }
                        }
                        return equals;
                    }
                    return super.equals(obj);
                }

            }

        }

        interface DescMapping {

            DescMapping setDesc(final Obfuscation obfuscation, final String desc);

            String getDesc(final Obfuscation obfuscation);

            /**
             * Copies from the given {@link org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMapping.DescMapping} to this class
             *
             * @param descMapping
             * @return
             */
            DescMapping from(final DescMapping descMapping);

            static FactoryDescMapping factory() {
                if (FactoryDescMapping.instance == null) FactoryDescMapping.instance = new FactoryDescMapping();
                return FactoryDescMapping.instance;
            }

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            final class FactoryDescMapping {

                private static FactoryDescMapping instance = null;

                private final EnumMap<Obfuscation, String> desc = new EnumMap<>(Obfuscation.class);

                public FactoryDescMapping setDesc(final Obfuscation obfuscation, final String desc) {
                    this.desc.put(obfuscation, desc);
                    return this;
                }

                public FactoryDescMapping setDesc(final String desc) {
                    for(Obfuscation obfuscation : Obfuscation.values()) setDesc(obfuscation, desc);
                    return this;
                }

                public DescMapping create() {
                    //Do not create DescMapping if no desc is set
                    if (desc.isEmpty()) return null;

                    DescMapping descMapping = new DescMappingImpl();

                    //Copy from our 'desc' field into the DescMapping
                    for(Entry<Obfuscation, String> entry : desc.entrySet()) {
                        descMapping.setDesc(entry.getKey(), entry.getValue());
                    }

                    //Clear the 'desc' field for next creation
                    desc.clear();

                    return descMapping;
                }
            }

            @NoArgsConstructor(access = AccessLevel.PRIVATE)
            final class DescMappingImpl implements DescMapping {

                private final String[] desc = new String[Obfuscation.values().length];

                @Override
                public DescMapping setDesc(final Obfuscation obfuscation, final String desc) {
                    this.desc[obfuscation.ordinal()] = desc;
                    return this;
                }

                @Override
                public String getDesc(final Obfuscation obfuscation) {
                    return desc[obfuscation.ordinal()];
                }

                @Override
                public DescMapping from(final DescMapping descMapping) {
                    //Do not copy from given SignatureMapping if null
                    if (descMapping == null) return this;

                    //Clear our 'desc' array
                    Arrays.fill(desc, null);

                    //Update our 'desc' array from the given DescMapping
                    for(Obfuscation obfuscation : Obfuscation.values()) {
                        String toDesc = descMapping.getDesc(obfuscation);
                        setDesc(obfuscation, toDesc);
                    }
                    return this;
                }

                @Override
                public boolean equals(final Object obj) {
                    if (obj instanceof DescMappingImpl) {
                        DescMappingImpl descMapping = (DescMappingImpl)obj;
                        boolean equals = true;
                        for(Obfuscation obf : Obfuscation.VALUES) {
                            if (!descMapping.getDesc(obf).equals(getDesc(obf))) {
                                equals = false;
                                break;
                            }
                        }
                        return equals;
                    }
                    return super.equals(obj);
                }

            }

        }

        interface SignatureMapping {

            void setSignature(final Obfuscation obfuscation, final String signature);

            String getSignature(final Obfuscation obfuscation);

            /**
             * Copies from the given {@link org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMapping.SignatureMapping} to this class
             *
             * @param signatureMapping
             * @return
             */
            SignatureMapping from(final SignatureMapping signatureMapping);

            @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
            final class SignatureMappingImpl implements SignatureMapping {

                private final String[] signatures = new String[Obfuscation.values().length];

                @Override
                public void setSignature(final Obfuscation obfuscation, final String signature) {
                    signatures[obfuscation.ordinal()] = signature;
                }

                @Override
                public String getSignature(final Obfuscation obfuscation) {
                    return signatures[obfuscation.ordinal()];
                }

                @Override
                public SignatureMapping from(final SignatureMapping signatureMapping) {
                    //Do not copy from given SignatureMapping if null
                    if (signatureMapping == null) return this;

                    //Clear our 'signatures' array
                    Arrays.fill(signatures, null);

                    //Update our 'signatures' array from the given SignatureMapping
                    for(Obfuscation obfuscation : Obfuscation.values()) {
                        String signature = signatureMapping.getSignature(obfuscation);
                        setSignature(obfuscation, signature);
                    }
                    return this;
                }

            }

        }

    }

    public interface ObfuscationMappingEx extends ObfuscationMapping {

        DescMapping getDescMapping();

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ObfuscationClass implements ObfuscationMapping {

        @Getter
        private final NameMapping nameMapping = new NameMappingImpl();

        @Getter
        private final NameMapping superNameMapping = new NameMappingImpl();

        @Getter
        private final SignatureMapping signatureMapping = new SignatureMappingImpl();

        private ObfuscationClass _super;

        private final List<ObfuscationField> fields = Lists.newArrayList();
        private final List<ObfuscationMethod> methods = Lists.newArrayList();

        /**
         * Get super class mapping from this class.
         *
         * @return {@link org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationClass}
         */
        public ObfuscationClass getSuper() {
            return _super;
        }

        /**
         * Get field mapping from this class.
         *
         * @param obfuscation Obfuscation to look for
         * @param name Name of field
         * @param desc Desc type - Ex: Ljava/lang/Object;
         * @param lookAtSuper If should look at super class for field
         *
         * @return Field Mapping {@link org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationField}
         */
        public ObfuscationField getField(final Obfuscation obfuscation, final String name, final String desc, final boolean lookAtSuper) {
            ObfuscationField obfuscationField = null;
            if (lookAtSuper) {
                //TODO
            } else {
                for(ObfuscationField iteratingObfuscationField : fields) {
                    if (iteratingObfuscationField.getNameMapping().getName(obfuscation).equals(name) && iteratingObfuscationField.getDescMapping().getDesc(obfuscation).equals(desc)) {
                        obfuscationField = iteratingObfuscationField;
                        break;
                    }
                }
            }
            return obfuscationField;
        }

        /**
         * Get method mapping from this class.
         *
         * @param obfuscation Obfuscation to look for
         * @param name Name of method
         * @param desc Desc types - Ex: (Ljava/lang/String;)Ljava/lang/Object;
         * @param lookAtSuper If should look at super class for method
         *
         * @return Field Mapping {@link org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationField}
         */
        public ObfuscationMethod getMethod(final Obfuscation obfuscation, final String name, final String desc, final boolean lookAtSuper) {
            ObfuscationMethod obfuscationMethod = null;
            if (lookAtSuper) {
            } else {
            }
            return obfuscationMethod;
        }

        /**
         * Reloads super class.
         *
         * This should be invoked if the super name has been changed
         */
        public void reloadSuper() {
            //TODO
        }

        public static FactoryObfuscationClass factory() {
            if (FactoryObfuscationClass.instance == null) FactoryObfuscationClass.instance = new FactoryObfuscationClass();
            return FactoryObfuscationClass.instance;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class FactoryObfuscationClass {

            private static FactoryObfuscationClass instance = null;

            private NameMapping name = null;
            private NameMapping superName = null;
            private SignatureMapping signature = null;

            private final List<ObfuscationField> fields = Lists.newArrayList();
            private final List<ObfuscationMethod> methods = Lists.newArrayList();

            public FactoryObfuscationClass setName(final NameMapping name) {
                this.name = name;
                return this;
            }

            public FactoryObfuscationClass setSuperName(final NameMapping superName) {
                this.superName = superName;
                return this;
            }

            public FactoryObfuscationClass setSignature(final SignatureMapping signature) {
                this.signature = signature;
                return this;
            }

            public FactoryObfuscationClass addField(final ObfuscationField obfuscationField) {
                if (fields.contains(obfuscationField)) return this;
                fields.add(obfuscationField);
                return this;
            }

            public FactoryObfuscationClass addMethod(final ObfuscationMethod obfuscationMethod) {
                if (methods.contains(obfuscationMethod)) return this;
                methods.add(obfuscationMethod);
                return this;
            }

            public ObfuscationClass create() {
                //Do not create ObfuscationClass if no 'name' or 'superName' is provided
                if (name == null || superName == null) return null;

                ObfuscationClass obfuscationClass = new ObfuscationClass();
                obfuscationClass.getNameMapping().from(name);
                obfuscationClass.getSuperNameMapping().from(superName);
                obfuscationClass.getSignatureMapping().from(signature);

                return obfuscationClass;
            }

        }

    }

    public static final class ObfuscationField implements ObfuscationMappingEx {

        @Getter
        private final NameMapping nameMapping = new NameMappingImpl();

        @Getter
        private final DescMapping descMapping = new DescMappingImpl();

        @Getter
        private final SignatureMapping signatureMapping = new SignatureMappingImpl();

        public static FactoryObfuscationField factory() {
            if (FactoryObfuscationField.instance == null) FactoryObfuscationField.instance = new FactoryObfuscationField();
            return FactoryObfuscationField.instance;
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static final class FactoryObfuscationField {

            private static FactoryObfuscationField instance = null;

            private NameMapping name = null;
            private DescMapping desc = null;
            private SignatureMapping signature = null;

            public FactoryObfuscationField setName(final NameMapping name) {
                this.name = name;
                return this;
            }

            public FactoryObfuscationField setDesc(final DescMapping desc) {
                this.desc = desc;
                return this;
            }

            public FactoryObfuscationField setSignature(final SignatureMapping signature) {
                this.signature = signature;
                return this;
            }

            public ObfuscationField create() {
                //Do not create ObfuscationField if no 'name' is provided
                //Does not check for signature, it is not required for the field unless specified
                if (name == null) return null;

                ObfuscationField obfuscationField = new ObfuscationField();
                obfuscationField.getNameMapping().from(name);
                obfuscationField.getDescMapping().from(desc);
                obfuscationField.getSignatureMapping().from(signature);
                return obfuscationField;
            }

        }

    }

    public static final class ObfuscationMethod implements ObfuscationMappingEx {

        @Getter
        private final NameMapping nameMapping = new NameMappingImpl();

        @Getter
        private final DescMapping descMapping = new DescMappingImpl();

        @Getter
        private final SignatureMapping signatureMapping = new SignatureMappingImpl();

        public static FactoryObfuscationMethod factory() {
            if (FactoryObfuscationMethod.instance == null) FactoryObfuscationMethod.instance = new FactoryObfuscationMethod();
            return FactoryObfuscationMethod.instance;
        }

        public static final class FactoryObfuscationMethod {

            private static FactoryObfuscationMethod instance = null;

            private NameMapping name = null;
            private DescMapping desc = null;
            private SignatureMapping signature = null;

            public FactoryObfuscationMethod setName(final NameMapping name) {
                this.name = name;
                return this;
            }

            public FactoryObfuscationMethod setDesc(final DescMapping desc) {
                this.desc = desc;
                return this;
            }

            public FactoryObfuscationMethod setSignature(final SignatureMapping signature) {
                this.signature = signature;
                return this;
            }

            public ObfuscationMethod create() {
                //Do not create ObfuscationMethod if no 'name' or 'desc' is provided
                //Does not check for signature, it is not required for the method unless specified
                if (name == null || desc == null) return null;

                ObfuscationMethod obfuscationMethod = new ObfuscationMethod();
                obfuscationMethod.getNameMapping().from(name);
                obfuscationMethod.getDescMapping().from(desc);
                obfuscationMethod.getSignatureMapping().from(signature);
                return obfuscationMethod;
            }

        }

    }

}
