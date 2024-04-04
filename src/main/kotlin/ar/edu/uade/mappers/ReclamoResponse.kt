package ar.edu.uade.mappers

import kotlinx.serialization.Serializable

@Serializable
data class ReclamoResponse (val idReclamo: Int,val documento: String, val idSitio: Int, val idDesperfecto: Int, val descripcion: String, val estado: String, val idReclamoUnificado: Int)