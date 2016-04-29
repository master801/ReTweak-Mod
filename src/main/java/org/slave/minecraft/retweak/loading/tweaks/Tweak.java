package org.slave.minecraft.retweak.loading.tweaks;

import org.objectweb.asm.tree.ClassNode;

/**
 * Created by Master on 4/27/2016 at 7:21 AM.
 *
 * @author Master
 */
public interface Tweak {

    String getName();

    void tweak(ClassNode classNode);

    int getSortIndex();

}
