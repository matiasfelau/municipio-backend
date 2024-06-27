package ar.edu.uade.mappers.responses

data class VecinoDenunciadoResponse(
    val idDenuncia: Int,
    val documento: String?,
    val direccion: String,
    val nombre: String,
    val apellido: String
)