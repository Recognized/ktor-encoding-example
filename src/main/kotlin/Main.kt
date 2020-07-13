import io.ktor.client.HttpClient
import io.ktor.client.features.compression.ContentEncoding
import io.ktor.client.request.get
import io.ktor.client.statement.HttpStatement
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val client = HttpClient {
            ContentEncoding {
                gzip()
            }
        }

        val url = "https://meduza.io"
        // string is decoded correctly
        println(client.get<String>(url).split("\n").first())

        val bytes = ByteArray(128)

        client.get<HttpStatement>(url).execute { response ->
            response.content.readFully(bytes, 0, bytes.size)
            // gzip format header and compressed content (1f 8b 08 00 00 ...)
            println(bytes.joinToString(separator = " ") { it.toUByte().toString(16) })
        }
    }
}