package com.jj.routingservice.http

import android.content.Context
import com.jj.routingservice.config.Config
import com.jj.routingservice.util.JLog
import fi.iki.elonen.NanoHTTPD
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class HttpWebServer(private val mainContext:Context) : NanoHTTPD(Config.HTTP_WEB_PORT) {
    fun startServer() {
        start(SOCKET_READ_TIMEOUT, false)
        JLog.r("HttpWebServer","Start HttpWebServer")
    }

    override fun serve(session: IHTTPSession?): Response {
        val uri = session!!.uri
        println("####MyWebServer:$uri")
        var filename = uri.substring(1)
        if (uri == "/") filename = "index.html"
        val mimetype: String
        if (filename.contains(".html") || filename.contains(".htm")) {
            mimetype = "text/html"
        } else if (filename.contains(".js")) {
            mimetype = "text/javascript"
        } else if (filename.contains(".css")) {
            mimetype = "text/css"
        } else {
//            filename = "index.html"
//            mimetype = "text/html"
            val inputStream = mainContext.assets.open("dist/$filename")
            return newFixedLengthResponse(Response.Status.OK,"application/octet-stream",inputStream, inputStream.available().toLong())
        }

        var response: String? = ""
        var line: String?
        val reader: BufferedReader?
        try {
            reader = BufferedReader(InputStreamReader(mainContext.assets.open("dist/$filename")))
            while ((reader.readLine().also { line = it }) != null) {
                response += line
            }
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return newFixedLengthResponse(Response.Status.OK, mimetype, response)
    }
}