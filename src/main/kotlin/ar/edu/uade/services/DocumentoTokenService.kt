package ar.edu.uade.services

import ar.edu.uade.daos.DocumentoTokenDAO
import ar.edu.uade.daos.DocumentoTokenDAOImpl
import ar.edu.uade.models.DocumentoToken
import ar.edu.uade.models.Empleado

class DocumentoTokenService {
    private val dao: DocumentoTokenDAO = DocumentoTokenDAOImpl()

    suspend fun newToken(documentoToken: DocumentoToken) = dao.insertToken(documentoToken)

    suspend fun getToken(documento: String) = dao.getToken(documento)
}