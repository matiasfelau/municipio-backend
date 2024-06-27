package ar.edu.uade.utilities.containers

import ar.edu.uade.mappers.serializables.ComercioDenunciadoSerializable
import ar.edu.uade.mappers.serializables.DenunciaSerializable
import ar.edu.uade.models.ComercioDenunciado
import ar.edu.uade.models.Denuncia
import kotlinx.serialization.Serializable

@Serializable
data class ContainerDenunciaComercio(
    val denuncia: DenunciaSerializable,
    val comercioDenunciado: ComercioDenunciadoSerializable
)