package vn.prostylee.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * This is a first simple gradle plugin
 *
 * Reference:
 *
 * https://docs.gradle.org/current/userguide/tutorial_using_tasks.html
 *
 * https://docs.gradle.org/current/userguide/custom_tasks.html
 *
 * https://www.baeldung.com/gradle-create-plugin
 *
 * https://www.baeldung.com/gradle-custom-task
 */
public class GreetingPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {

        GreetingPluginExtension extension = project.getExtensions()
                .create("greeting", GreetingPluginExtension.class);

        project.task("hello").doLast(task -> {
            System.out.println("Hello, " + extension.getGreeter());
            System.out.println("Java version: " + System.getProperty("java.version"));
        });
    }
}