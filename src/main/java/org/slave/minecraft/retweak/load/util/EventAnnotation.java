package org.slave.minecraft.retweak.load.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slave.minecraft.retweak.load.util.GameVersion.GameVersionModIdentifier.Identifier;

import java.lang.annotation.Annotation;

/**
 * Created by Master on 7/18/2018 at 10:00 AM.
 *
 * @author Master
 */
@RequiredArgsConstructor
public final class EventAnnotation {

    @Getter
    private final Identifier identifier;

    @Getter
    private final String name;

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof EventAnnotation) {
            EventAnnotation other = (EventAnnotation) obj;
            return other.identifier.equals(identifier) && other.name.equals(name);
        }
        if (obj instanceof Annotation) {
            Annotation annotation = (Annotation)obj;

            Class<?> xx = annotation.annotationType();
            String x = xx.getName();
            return x.equals(name);
        }
        return super.equals(obj);
    }

}
