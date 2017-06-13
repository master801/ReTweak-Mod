package org.slave.minecraft.retweak.loading.capsule.versions;

import org.slave.lib.util.Bulk;

import java.util.List;

/**
 * Created by Master on 4/19/2017 at 7:34 PM.
 *
 * @author Master
 */
final class ClassHolder {

    private final List<Bulk<String, Class<?>>> override;

    ClassHolder(final List<Bulk<String, Class<?>>> override) {
        this.override = override;
    }

    public List<Bulk<String, Class<?>>> getOverrideClasses() {
        return override;
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
