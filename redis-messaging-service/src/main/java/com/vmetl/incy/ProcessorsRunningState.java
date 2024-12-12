package com.vmetl.incy;

import org.springframework.stereotype.Component;

@Component
public class ProcessorsRunningState {
    private volatile boolean running;

    public ProcessorsRunningState() {
        this.running = true;
    }

    public boolean stop() {
        this.running = false;

        return running;
    }

    public boolean isRunning() {
        return running;
    }
}
