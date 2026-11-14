package com.example.driverkarma.classes

import android.content.Context
import android.location.Location
import android.os.Environment
import android.util.Xml
import com.example.driverkarma.functions.mpsToKmh
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlSerializer
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class GpxFileManager {

    suspend fun createGpxFile(
        context: Context,
        locations: List<Location>
    ): File = withContext(Dispatchers.IO) {

        val directory = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                ?: context.filesDir,
            "gpx"
        )

        directory.mkdirs()

        val filename =
            "driving_session_" +
                    SimpleDateFormat(
                        "yyyyMMdd_HHmmss",
                        Locale.US
                    ).format(Date()) +
                    ".gpx"

        val file = File(directory, filename)

        val serializer: XmlSerializer = Xml.newSerializer()

        file.outputStream().buffered().use { output ->

            serializer.setOutput(output, "UTF-8")
            serializer.startDocument("UTF-8", true)

            serializer.setPrefix(
                "",
                "http://www.topografix.com/GPX/1/1"
            )

            serializer.setFeature(
                "http://xmlpull.org/v1/doc/features.html#indent-output",
                true
            )

            val namespace = "http://www.topografix.com/GPX/1/1"

            serializer.startTag(namespace, "gpx")
            serializer.attribute(null, "version", "1.1")
            serializer.attribute(null, "creator", "Driver Karma")

            serializer.startTag(namespace, "trk")

            serializer.startTag(namespace, "name")
            serializer.text("Driving Session")
            serializer.endTag(namespace, "name")

            serializer.startTag(namespace, "trkseg")

            locations.forEach { location ->

                serializer.startTag(namespace, "trkpt")

                serializer.attribute(
                    null,
                    "lat",
                    location.latitude.toString()
                )

                serializer.attribute(
                    null,
                    "lon",
                    location.longitude.toString()
                )

                if (location.hasAltitude()) {
                    serializer.startTag(namespace, "ele")
                    serializer.text(location.altitude.toString())
                    serializer.endTag(namespace, "ele")
                }

                serializer.startTag(namespace, "time")
                serializer.text(
                    SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss'Z'",
                        Locale.US
                    ).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }.format(Date(location.time))
                )
                serializer.endTag(namespace, "time")


                serializer.startTag(namespace, "extensions")
                if(location.hasSpeed()) {
                    serializer.startTag(namespace, "speed")
                    serializer.text(mpsToKmh(location.speed).toString())
                    serializer.endTag(namespace, "speed")
                }
                serializer.endTag(namespace, "extensions")

                serializer.endTag(namespace, "trkpt")
            }

            serializer.endTag(namespace, "trkseg")
            serializer.endTag(namespace, "trk")
            serializer.endTag(namespace, "gpx")

            serializer.endDocument()
        }

        file
    }
}