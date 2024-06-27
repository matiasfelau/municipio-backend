package ar.edu.uade.mappers.serializables

import kotlinx.serialization.Serializable

@Serializable
data class VecinoDenunciadoSerializable (
    val id: Int,
    val idDenuncia: Int,
    val documento: String?,
    val direccion: String,
    val nombre: String,
    val apellido: String
)