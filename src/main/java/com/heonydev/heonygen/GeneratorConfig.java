package com.heonydev.heonygen;

public class GeneratorConfig {

    String name;
    String type;
    OriginConfig origin;
    TargetConfig target;

    public static class OriginConfig {
        String packageName;
        String annotation;
    }

    public static class TargetConfig {
        String packageName;
        String directory;
    }

}
