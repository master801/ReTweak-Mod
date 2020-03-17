package org.slave.minecraft.tool.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.slave.lib.api.task.Task;
import org.slave.lib.api.task.TaskID;
import org.slave.lib.api.task.TaskRunner;
import org.slave.lib.resources.ASMTable;
import org.slave.lib.util.WrappingDataT;
import org.slave.minecraft.tool.map.Mapper;

import java.io.File;

/**
 * Created by Master on 3/16/2020 at 12:24 PM
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskProcess implements Task<WrappingDataT.WrappingDataT2<File, File>, Void> {

    public static final TaskID<WrappingDataT.WrappingDataT2<File, File>, Void> TASK_ID_PROCESS = new TaskID<>("taskProcess", new TaskProcess());

    @Override
    public Void run(@NonNull final TaskRunner taskRunner, final WrappingDataT.WrappingDataT2<File, File> input) throws Exception {
        ASMTable processInput = taskRunner.runTask(TaskProcessInput.TASK_ID_PROCESS_INPUT, input.getObject1());

        taskRunner.runTask(
                TaskProcessOutput.TASK_ID_PROCESS_OUTPUT,
                new WrappingDataT.WrappingDataT2<>(input.getObject2(), new Mapper(processInput))
        );
        return null;
    }

}
