package com.mrcd.xrouter.gradle.plugin.core;

import java.io.File;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * 工作流程
 */
public interface IEngineWork {

    void startEngine(Project project, Task task, File rootDir);

}
