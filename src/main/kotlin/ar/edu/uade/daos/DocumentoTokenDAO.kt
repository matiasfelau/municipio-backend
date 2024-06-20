package ar.edu.uade.daos

import ar.edu.uade.models.DocumentoToken

interface DocumentoTokenDAO {
    suspend fun insertToken(documentoToken: DocumentoToken)
    suspend fun getToken(documento: String): DocumentoToken?
}