package org.slave.minecraft.retweak.loading.mappings;

/**
 * Created by Master on 4/27/2016 at 3:48 PM.
 *
 * @author Master
 */
final class Stub {

    private final String className;
    private String name;
    private String desc;

    private boolean isClass = false;

    Stub(final String className, final String name, final String desc) {
        this(
                className,
                name
        );
        this.desc = desc;
    }

    Stub(final String className, final String name) {
        this(className);
        this.name = name;
        isClass = false;
    }

    Stub(final String className) {
        this.className = className;
        isClass = true;
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isClass() {
        return isClass;
    }

}
