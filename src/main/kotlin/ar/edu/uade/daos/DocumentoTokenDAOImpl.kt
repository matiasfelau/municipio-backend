package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.DocumentoToken
import ar.edu.uade.models.Reclamo
import ar.edu.uade.models.Reclamo.Reclamos
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class DocumentoTokenDAOImpl: DocumentoTokenDAO {
    private fun resultRowToDocumentoToken(row: ResultRow) = DocumentoToken(
        documento = row[DocumentoToken.TDocumentoToken.documento],
        token = row[DocumentoToken.TDocumentoToken.token]
    )

    override suspend fun insertToken(documentoToken: DocumentoToken) = dbQuery {
        val insertStatement = DocumentoToken.TDocumentoToken.insert{
            it[DocumentoToken.TDocumentoToken.documento] = documentoToken.documento
            it[DocumentoToken.TDocumentoToken.token] = documentoToken.token
        }
    }

    override suspend fun getToken(documento: String): DocumentoToken? = dbQuery {
        DocumentoToken.TDocumentoToken.select { DocumentoToken.TDocumentoToken.documento like documento }
            .map(::resultRowToDocumentoToken)
            .singleOrNull()
    }
}