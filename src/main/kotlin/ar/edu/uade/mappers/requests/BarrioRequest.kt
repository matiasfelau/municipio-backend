package ar.edu.uade.mappers.requests

import kotlinx.serialization.Serializable

@Serializable
data class BarrioRequest(val idBarrio: Int, val nombre: String)
