package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.MovimientoDenuncia
import org.jetbrains.exposed.sql.select
import java.time.LocalDateTime
import ar.edu.uade.models.MovimientoDenuncia.MovimientosDenuncia
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class MovimientoDenunciaDAOFacadeMySQL: MovimientoDenunciaDAOFacade {
    private fun resultRowToMovimientoDenuncia(row: ResultRow) = MovimientoDenuncia(
        idMovimiento = row[MovimientosDenuncia.idMovimiento],
        responsable = row[MovimientosDenuncia.responsable],
        causa = row[MovimientosDenuncia.causa],
        fecha = row[MovimientosDenuncia.fecha],
        idDenuncia = row[MovimientosDenuncia.idDenuncia]
    )

    override suspend fun getMovimientosByDelay(): List<MovimientoDenuncia> = dbQuery{
        MovimientosDenuncia.select { MovimientosDenuncia.fecha greater LocalDateTime.now().minusMinutes(1) }
            .map(::resultRowToMovimientoDenuncia)
    }

    override suspend fun getMovimientosReclamo(id: Int): List<MovimientoDenuncia> = dbQuery{
        MovimientosDenuncia.select(MovimientosDenuncia.idDenuncia eq id).map(::resultRowToMovimientoDenuncia)
    }
}