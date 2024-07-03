package ar.edu.uade.mappers.responses

import ar.edu.uade.utilities.BigDecimalComponentSerializer
import ar.edu.uade.utilities.LocalTimeComponentSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalTime

@Serializable
class PublicacionResponse(
    val id: Int?,
    val titulo: String,
    val descripcion: String,
    val autor: String,
    val fechaPublicacion: String,
    val imagenes: List<String>?
)
