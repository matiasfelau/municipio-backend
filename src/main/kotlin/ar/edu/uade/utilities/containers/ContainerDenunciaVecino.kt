package ar.edu.uade.utilities.containers

import ar.edu.uade.mappers.requests.DenunciaRequest
import ar.edu.uade.mappers.requests.VecinoDenunciadoRequest
import kotlinx.serialization.Serializable

@Serializable
data class ContainerDenunciaVecino(
    val denuncia: DenunciaRequest,
    val vecinoDenunciado: VecinoDenunciadoRequest
)