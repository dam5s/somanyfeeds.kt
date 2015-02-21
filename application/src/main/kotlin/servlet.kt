package com.somanyfeeds.application

import javax.servlet.annotation.WebServlet
import javax.servlet.http.*
import java.io.InputStream
import java.io.BufferedReader
import java.io.Writer
import java.io.InputStreamReader

WebServlet(name = "SoManyFeeds", value = array("/*"))
public class SoManyFeedsServlet : HttpServlet() {
    private val resourceLoader = ResourceLoader()

    override public fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        val respWriter = resp!!.getWriter()
        val path = req!!.getPathInfo()
        val resourceName = if (path.equals("/")) "index.html" else path.substring(1)

        val resourceStream: InputStream? = resourceLoader.load("/public/$resourceName")
        if (resourceStream != null) {
            this.writeResource(respWriter, resourceStream)
            return
        }

        // TODO build JSON API from here.
        respWriter.write("Welcome to SoManyFeeds!")
    }

    fun writeResource(writer: Writer, resourceStream: InputStream) {
        val reader = BufferedReader(InputStreamReader(resourceStream));
        var line: String? = reader.readLine();

        while (line != null) {
            writer.write(line);
            line = reader.readLine();
        }
    }
}
