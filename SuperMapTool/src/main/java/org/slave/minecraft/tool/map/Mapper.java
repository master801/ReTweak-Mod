package org.slave.minecraft.tool.map;

import lombok.RequiredArgsConstructor;
import org.slave.lib.resources.ASMTable;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by Master on 3/16/2020 at 1:23 PM
 *
 * @author Master
 */
@RequiredArgsConstructor
public final class Mapper {

    private final ASMTable asmTable;

    public void write(final Writer writer) throws IOException {
        for(ASMTable.TableClass tableClass : asmTable.getTableClasses()) {
            writer.write(String.format("%s: %s %s\n", "SUPER", tableClass.getName(), tableClass.getSuperName()));
        }
    }

}
