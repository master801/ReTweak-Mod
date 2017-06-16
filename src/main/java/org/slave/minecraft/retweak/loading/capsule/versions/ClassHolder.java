package org.slave.minecraft.retweak.loading.capsule.versions;

import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.ClassEntry;

import java.util.List;

/**
 * Created by Master on 4/19/2017 at 7:34 PM.
 *
 * @author Master
 */
final class ClassHolder {

    private final List<ClassEntry> override;

    ClassHolder(final List<ClassEntry> override) {
        this.override = override;
    }

    public List<ClassEntry> getOverrideClasses() {
        return override;
    }

    public static final class ClassEntryBuilder {

        private String from;
        private Class<?> to;

        public ClassEntryBuilder() {
        }

        public ClassEntryBuilder setFrom(final String from) {
            this.from = from;
            return this;
        }

        public ClassEntryBuilder setTo(final Class<?> to) {
            this.to = to;
            return this;
        }

        public ClassEntry build() {
            if (from == null) throw new NullPointerException("From not set!");
            if (to == null) throw new NullPointerException("To not set!");
            return new ClassEntry(
                from,
                to
            );
        }

        public static final class ClassEntry {

            private final String from;
            private final Class<?> to;

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
