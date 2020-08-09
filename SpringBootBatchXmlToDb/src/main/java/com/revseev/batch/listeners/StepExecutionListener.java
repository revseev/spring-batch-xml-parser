package com.revseev.batch.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

@Slf4j
@Component
public class StepExecutionListener extends StepExecutionListenerSupport {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("--> Wrote: {} items in step #{}", stepExecution.getWriteCount(), stepExecution.getStepName());
        return super.afterStep(stepExecution);
    }
}
