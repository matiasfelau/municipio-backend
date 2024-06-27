package ar.edu.uade.mappers.serializables

import kotlinx.serialization.Serializable

@Serializable
data class ComercioDenunciadoSerializable(
    val id: Int,
    val idComercio: Int,
    val idDenuncia: Int,
    val nombre: String,
    val direccion: String
)