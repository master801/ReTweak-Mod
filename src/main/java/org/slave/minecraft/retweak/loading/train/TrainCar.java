package org.slave.minecraft.retweak.loading.train;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 4/29/2016 at 7:56 PM.
 *
 * @author Master
 */
public final class TrainCar {

    private final Identifier identifier;
    private final ClassName className;
    private Name name;
    private Desc desc;

    private TrainCar superClass;//Used only for classes //TODO
    private List<TrainCar> fields;//Used only for classes
    private List<TrainCar> methods;//Used only for classes

    TrainCar(final ClassName className, final Name name, final Desc desc) {
        identifier = Identifier.METHOD;
        this.className = className;
        this.name = name;
        this.desc = desc;
    }

    TrainCar(final ClassName className, final Name name) {
        identifier = Identifier.FIELD;
        this.className = className;
        this.name = name;
    }

    TrainCar(final ClassName className) {
        identifier = Identifier.CLASS;
        this.className = className;
        fields = new ArrayList<>();
        methods = new ArrayList<>();
    }

    Identifier getIdentifier() {
        return identifier;
    }

    ClassName getClassName() {
        return className;
    }

    Name getName() {
        return name;
    }

    Desc getDesc() {
        return desc;
    }

    void addField(TrainCar field) {
        if (field == null) return;
        if (field.getIdentifier() != Identifier.FIELD) throw new IllegalStateException();
        fields.add(field);
    }

    void addMethod(TrainCar method) {
        if (method == null) return;
        if (method.getIdentifier() != Identifier.METHOD) throw new IllegalStateException();
        methods.add(method);
    }

    public static final class ClassName {

        private final String[] classNames;

        public ClassName(final String obfuscated, final String deobfuscated) {
            classNames = new String[] {
                    obfuscated,
                    deobfuscated
            };
        }

        String getClassNames(Type type) {
            return classNames[type.ordinal()];
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof ClassName) {
                ClassName className = (ClassName)obj;
                boolean equals = true;
                for(int i = 0; i < classNames.length; ++i) {
                    if (!className.classNames[i].equals(classNames[i])) {
                        equals = false;
                        break;
                    }
                }
                return equals;
            }
            return super.equals(obj);
        }

    }

    public static final class Name {

        private final String[] names;

        Name(final String obfuscated, final String deobfuscated) {
            names = new String[] {
                    obfuscated,
                    deobfuscated
            };
        }

        String getName(Type type){
            return names[type.ordinal()];
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Name) {
                Name name = (Name)obj;
                boolean equals = true;
                for(int i = 0; i < names.length; ++i) {
                    if (!name.names[i].equals(names[i])) {
                        equals = false;
                        break;
                    }
                }
                return equals;
            }
            return super.equals(obj);
        }

    }

    public static final class Desc {

        private final String[] descs;

        Desc(final String obfuscated, final String deobfuscated) {
            descs = new String[] {
                    obfuscated,
                    deobfuscated
            };
        }

        String getDesc(Type type) {
            return descs[type.ordinal()];
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Desc) {
                Desc desc = (Desc)obj;
                boolean equals = true;
                for(int i = 0; i < descs.length; ++i) {
                    if (!desc.descs[i].equals(descs[i])) {
                        equals = false;
                        break;
                    }
                }
                return equals;
            }
            return super.equals(obj);
        }

    }

    public enum Type {

        OBFUSCATED,

        DEOBUFSCATED;

        Type() {
        }

    }

    public enum Identifier {

        CLASS,

        FIELD,

        METHOD;

        Identifier() {
        }

    }

}
