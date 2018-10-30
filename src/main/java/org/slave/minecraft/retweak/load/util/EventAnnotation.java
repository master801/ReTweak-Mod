package org.slave.minecraft.retweak.load.util;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.InstanceFactory;
import cpw.mods.fml.common.SidedProxy;
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

    static final EventAnnotation EVENT_ANNOTATION_EVENT_HANDLER = new EventAnnotation(Identifier.ANNOTATION, EventHandler.class);
    static final EventAnnotation EVENT_ANNOTATION_SIDED_PROXY = new EventAnnotation(Identifier.ANNOTATION, SidedProxy.class);
    static final EventAnnotation EVENT_ANNOTATION_INSTANCE_FACTORY = new EventAnnotation(Identifier.ANNOTATION, InstanceFactory.class);

    @Getter
    private final Identifier identifier;

    @Getter
    private final String name;

    public EventAnnotation(final Identifier identifier, final Class<?> clazz) {
        this(identifier, clazz.getName().replace('.', '/'));
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof EventAnnotation) {
            EventAnnotation other = (EventAnnotation) obj;
            return other.identifier == identifier && other.name.equals(name);
        }
        if (obj instanceof Annotation) {
            Annotation annotation = (Annotation)obj;

            Class<?> annotationType = annotation.annotationType();
            String annotationTypeName = annotationType.getName().replace('.', '/');
            return annotationTypeName.equals(name);
        }
        return super.equals(obj);
    }

}
