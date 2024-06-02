package ar.edu.uade.mappers.requests

import kotlinx.serialization.Serializable

@Serializable
data class ReclamoRequest(
    val descripcion: String,
    val estado: String,
    val documento: String,
    val idSitio: Int,
    val idDesperfecto: Int
)
