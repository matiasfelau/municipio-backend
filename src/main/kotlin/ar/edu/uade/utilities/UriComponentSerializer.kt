package ar.edu.uade.utilities

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URI
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = URI::class)
object UriComponentSerializer : KSerializer<URI> {
    private val formatter = UriFormatter()

    override fun serialize(encoder: Encoder, value: URI) {
        val string = formatter.format(value)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): URI {
        val string = decoder.decodeString()
        return formatter.parse(string)
    }

    private class UriFormatter {
        fun format(uri: URI): String {
            return uri.toString()
        }

        fun parse(string: String): URI {
            return URI.create(string)
        }
    }
}