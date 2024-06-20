package ar.edu.uade.models

import java.io.Serializable as JavaSerializable
import org.jetbrains.exposed.sql.Table

data class DocumentoToken(
    val documento: String,
    val token: String
): JavaSerializable {
    public object TDocumentoToken: Table() {
        val documento = varchar("documento", 40)
        val token = text("token")
        override val primaryKey = PrimaryKey(documento)
    }
}
