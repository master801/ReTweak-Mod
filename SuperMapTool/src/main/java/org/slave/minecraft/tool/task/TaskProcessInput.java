package org.slave.minecraft.tool.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.slave.lib.api.task.Task;
import org.slave.lib.api.task.TaskID;
import org.slave.lib.api.task.TaskRunner;
import org.slave.lib.resources.ASMTable;

import java.io.File;
import java.util.zip.ZipFile;

/**
 * Created by Master on 3/16/2020 at 12:40 PM
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskProcessInput implements Task<File, ASMTable> {

    public static final TaskID<File, ASMTable> TASK_ID_PROCESS_INPUT = new TaskID<>("taskProcessInput", new TaskProcessInput());

    @Override
    public ASMTable run(@NonNull final TaskRunner taskRunner, final File input) throws Exception {
        ASMTable asmTable = new ASMTable();
        try(ZipFile zipFile = new ZipFile(input)) {
            asmTable.load(zipFile, true);
        }
        return asmTable;
    }

}
