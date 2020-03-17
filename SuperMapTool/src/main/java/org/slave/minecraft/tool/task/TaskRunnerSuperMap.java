package org.slave.minecraft.tool.task;

import org.slave.lib.task.AbstractTaskRunner;

/**
 * Created by Master on 3/16/2020 at 12:20 PM
 *
 * @author Master
 */
public class TaskRunnerSuperMap extends AbstractTaskRunner {

    @Override
    protected void addTasks() {
        super.addTask(TaskProcess.TASK_ID_PROCESS);
        super.addTask(TaskProcessInput.TASK_ID_PROCESS_INPUT);
        super.addTask(TaskProcessOutput.TASK_ID_PROCESS_OUTPUT);
    }

    @Override
    protected void pre() {
    }

    @Override
    protected void post() {
    }

}
