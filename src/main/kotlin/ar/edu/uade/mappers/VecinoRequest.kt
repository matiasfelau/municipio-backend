package ar.edu.uade.mappers
import kotlinx.serialization.Serializable
@Serializable
data class VecinoRequest(val documento:String, val password:String)