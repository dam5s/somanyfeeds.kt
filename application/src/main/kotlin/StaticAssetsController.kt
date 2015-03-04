package com.somanyfeeds.application

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.InputStream
import java.io.PrintWriter
import java.io.BufferedReader
import java.io.InputStreamReader
import com.somanyfeeds.kotlinextensions.getResourceAsStream

trait StaticAssetsController {
    fun serveStaticAsset(req: HttpServletRequest, resp: HttpServletResponse): Boolean
}

class DefaultStaticAssetsController : StaticAssetsController {

    override fun serveStaticAsset(req: HttpServletRequest, resp: HttpServletResponse): Boolean {
        val path = req.getPathInfo()
        val resourceName = if (path.equals("/")) "index.html" else path.substring(1)

        getResourceAsStream("/public/$resourceName")?.let {
            resp.setContentType(getContentType(resourceName))
            writeResource(resp.getWriter(), it)
            return true
        }

        return false
    }

    private fun writeResource(writer: PrintWriter, resourceStream: InputStream) {
        val reader = BufferedReader(InputStreamReader(resourceStream));
        var line: String? = reader.readLine();

        while (line != null) {
            writer.write("$line\n");
            line = reader.readLine();
        }
    }

    private fun getContentType(resourceName: String): String {
        val parts = resourceName.split('.')
        val extension = if (parts.isEmpty()) "" else parts.last()

        return when (extension) {
            "html" -> "text/html"
            "css" -> "text/css"
            else -> "application/octet-stream"
        }
    }
}
