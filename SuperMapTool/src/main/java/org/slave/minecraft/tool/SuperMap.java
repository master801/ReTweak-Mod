package org.slave.minecraft.tool;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slave.lib.api.Clean;
import org.slave.lib.api.hook.Exit;
import org.slave.lib.api.task.TaskRunner;
import org.slave.lib.util.WrappingDataT;
import org.slave.minecraft.tool.task.TaskProcess;
import org.slave.minecraft.tool.task.TaskRunnerSuperMap;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Master on 3/16/2020 at 12:01 PM
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class SuperMap implements Clean, Exit {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(
            1,
            (threadFactory) -> {
                Thread thread = Executors.defaultThreadFactory().newThread(threadFactory);
                thread.setDaemon(true);//Alows for the program to exit even if the thread is running?
                return thread;
            }
    );

    private TaskRunner taskRunner;

    @Setter(value = AccessLevel.PACKAGE)
    @Getter
    private File fileInput;

    @Setter(value = AccessLevel.PACKAGE)
    @Getter
    private File fileOutput;

    private boolean isBusy = false;

    @Override
    public void clean() {
    }

    @Override
    public void exit() {
    }

    void init() {
        taskRunner = new TaskRunnerSuperMap();
    }

    void process() {
        if (fileInput == null) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Input file was not specified!");
                alert.show();
            });
            return;
        } else if (fileOutput == null) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Output file was not specified!");
                alert.show();
            });
            return;
        }
        executorService.submit(() -> {
            if (!isBusy) {
                isBusy = true;
                taskRunner.runTask(TaskProcess.TASK_ID_PROCESS, new WrappingDataT.WrappingDataT2<>(fileInput, fileOutput));
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Done processing!");
                    alert.show();
                });
                isBusy = false;
            } else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Already busy!");
                    alert.show();
                });
            }
        });
    }

}
