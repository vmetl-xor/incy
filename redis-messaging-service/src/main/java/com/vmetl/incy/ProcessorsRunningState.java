package com.vmetl.incy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessorsRunningState {
    Logger log = LoggerFactory.getLogger(ProcessorsRunningState.class);

    private volatile boolean running;

    public ProcessorsRunningState() {
        this.running = true;
    }

    public boolean stop() {
        this.running = false;
        log.debug("STOPPED, isRunning {} ", running);

        return running;
    }

    public boolean isRunning() {
        log.debug("isRunning {}", running);
        return running;
    }
}
