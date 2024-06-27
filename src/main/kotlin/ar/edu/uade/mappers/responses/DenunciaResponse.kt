package ar.edu.uade.mappers.responses

data class DenunciaResponse(
    val idDenuncia: Int,
    val descripcion: String?,
    val estado: String?,
    val aceptarResponsabilidad: Boolean,
    val documento: String
)