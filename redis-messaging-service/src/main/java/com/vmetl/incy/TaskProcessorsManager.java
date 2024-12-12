package com.vmetl.incy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskProcessorsManager {

    private final ProcessorsRunningState processorsRunningState;

    @Autowired
    public TaskProcessorsManager(ProcessorsRunningState processorsRunningState) {
        this.processorsRunningState = processorsRunningState;
    }

    public void stopAllProcessors() {
        processorsRunningState.stop();
    }
}
