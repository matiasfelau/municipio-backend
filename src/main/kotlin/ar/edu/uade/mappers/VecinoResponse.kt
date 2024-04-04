package ar.edu.uade.mappers

import kotlinx.serialization.Serializable

@Serializable
data class VecinoResponse(val documento:String, val nombre:String, val apellido:String, val direccion:String?, val codigoDeBarrio:Int?)