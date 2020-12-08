package vn.prostylee.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;

public class ProStyleePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {

        project.getPlugins().apply(JavaPlugin.class);
        project.getPlugins().apply(GreetingPlugin.class);
    }
}
