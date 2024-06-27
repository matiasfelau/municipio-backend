package ar.edu.uade.mappers.responses

data class ComercioDenunciadoResponse(
    val idComercio: Int,
    val idDenuncia: Int,
    val nombre: String,
    val direccion: String
)