package ar.edu.uade.mappers
import kotlinx.serialization.Serializable
@Serializable
data class CredencialRequest(val documento:String, val password:String)