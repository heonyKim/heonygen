package com.heonydev.heonygen;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class HeonygenPlugin implements Plugin<Project> {


    @Override
    public void apply(Project project) {
        // heonygen 확장 등록
        HeonygenExtension extension = project.getExtensions().create("heonygen", HeonygenExtension.class);

        // heonygen task 등록
        project.getTasks().register("heonygen", HeonygenTask.class, task -> {
            task.setDescription("Generates MongoDB field constants");
            task.setGroup("heonygen");
            task.setExtension(extension);
        });

        // build task가 실행될 때 heonygen task 선행 실행
        project.getTasks().named("build").configure(buildTask -> buildTask.dependsOn("heonygen"));
    }
}
