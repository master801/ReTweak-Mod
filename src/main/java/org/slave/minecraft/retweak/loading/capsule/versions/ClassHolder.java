package org.slave.minecraft.retweak.loading.capsule.versions;

import java.util.Collections;
import java.util.List;

/**
 * Created by Master on 4/19/2017 at 7:34 PM.
 *
 * @author Master
 */
final class ClassHolder {

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

}
