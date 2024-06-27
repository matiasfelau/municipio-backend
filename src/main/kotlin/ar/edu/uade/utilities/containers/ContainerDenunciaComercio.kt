package ar.edu.uade.utilities.containers

import ar.edu.uade.mappers.requests.ComercioDenunciadoRequest
import ar.edu.uade.mappers.requests.DenunciaRequest
import kotlinx.serialization.Serializable

@Serializable
data class ContainerDenunciaComercio(
    val denuncia: DenunciaRequest,
    val comercioDenunciado: ComercioDenunciadoRequest
)