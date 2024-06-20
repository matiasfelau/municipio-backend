package ar.edu.uade.mappers.requests

import kotlinx.serialization.Serializable

@Serializable
data class DocumentoTokenRequest(val documento: String, val token: String)
