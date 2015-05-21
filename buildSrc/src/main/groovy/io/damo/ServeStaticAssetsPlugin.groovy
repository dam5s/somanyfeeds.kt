package io.damo

import org.gradle.api.Project
import org.gradle.api.Plugin

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.*

class ServeStaticAssetsPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task("serve") << {
            def handler = new ResourceHandler()
            handler.directoriesListed = true
            handler.resourceBase = "${project.projectDir}/src/main/resources/public"
            handler.welcomeFiles = ["index.html"]

            def server = new Server(8088)
            server.setHandler(handler);
            server.start()
            println("Serving static assets...")
            server.join()
        }
    }
}
