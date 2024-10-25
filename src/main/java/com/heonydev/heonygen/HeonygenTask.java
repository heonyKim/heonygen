package com.heonydev.heonygen;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class HeonygenTask extends DefaultTask {
    private HeonygenExtension extension;

    @TaskAction
    public void generateFields() {
        extension.getConfiguration().forEach(config -> {
            if ("mongodb".equals(config.type)) {
                new MongodbFieldGenerator().generateFields(config);
            }
        });
    }

    public void setExtension(HeonygenExtension extension) {
        this.extension = extension;
    }
}