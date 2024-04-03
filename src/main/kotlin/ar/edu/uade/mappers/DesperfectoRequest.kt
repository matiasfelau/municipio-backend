package ar.edu.uade.mappers

import kotlinx.serialization.Serializable

@Serializable
data class DesperfectoRequest(val idDesperfecto: Int, val descripcion: String, val idRubro: Int)
