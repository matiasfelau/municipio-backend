package ar.edu.uade.utilities.containers

import ar.edu.uade.mappers.serializables.DenunciaSerializable
import ar.edu.uade.mappers.serializables.VecinoDenunciadoSerializable
import ar.edu.uade.models.ComercioDenunciado
import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.VecinoDenunciado
import kotlinx.serialization.Serializable

@Serializable
data class ContainerDenunciaVecino(
    val denuncia: DenunciaSerializable,
    val vecinoDenunciado: VecinoDenunciadoSerializable
)