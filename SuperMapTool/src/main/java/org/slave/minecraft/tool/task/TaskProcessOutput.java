package org.slave.minecraft.tool.task;

import lombok.NonNull;
import org.slave.lib.api.task.Task;
import org.slave.lib.api.task.TaskID;
import org.slave.lib.api.task.TaskRunner;
import org.slave.lib.util.WrappingDataT;
import org.slave.minecraft.tool.map.Mapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Master on 3/16/2020 at 2:58 PM
 *
 * @author Master
 */
public final class TaskProcessOutput implements Task<WrappingDataT.WrappingDataT2<File, Mapper>, Void> {

    public static final TaskID<WrappingDataT.WrappingDataT2<File, Mapper>, Void> TASK_ID_PROCESS_OUTPUT = new TaskID<>("taskProcessOutput", new TaskProcessOutput());

    @Override
    public Void run(@NonNull final TaskRunner taskRunner, final WrappingDataT.WrappingDataT2<File, Mapper> input) throws Exception {
        try(FileOutputStream fos = new FileOutputStream(input.getObject1())) {
            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos)) {
                input.getObject2().write(outputStreamWriter);
            }
        }
        return null;
    }

}
