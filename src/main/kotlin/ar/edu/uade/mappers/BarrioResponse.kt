package ar.edu.uade.mappers

import kotlinx.serialization.Serializable

@Serializable
data class BarrioResponse(val idBarrio: Int, val nombre:String)
