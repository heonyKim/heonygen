package com.heonydev.heonygen;

import org.gradle.api.model.ObjectFactory;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class HeonygenExtension {
    private final String version;
    private final List<GeneratorConfig> configuration = new ArrayList<>();

    @Inject
    public HeonygenExtension(ObjectFactory objects) {
        this.version = "0.0.1";
    }

    public String getVersion() {
        return version;
    }

    public List<GeneratorConfig> getConfiguration() {
        return configuration;
    }

    public void generator(GeneratorConfig config) {
        configuration.add(config);
    }
}
