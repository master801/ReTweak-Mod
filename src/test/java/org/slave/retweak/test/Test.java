package org.slave.retweak.test;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import org.slave.minecraft.retweak.handlers.TextureHandler;

/**
 * Created by Master on 6/14/2016 at 10:33 PM.
 *
 * @author Master
 */
public final class Test {

    private Test() {
        throw new IllegalStateException();
    }

    public void itemTest() {
        final Item webItem = Item.getItemFromBlock(
                Blocks.web
        );

        TextureHandler.bindTexture(
                this.getClass(),
                "xx"
        );
    }

}
