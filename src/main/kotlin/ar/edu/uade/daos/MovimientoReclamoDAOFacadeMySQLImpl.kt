package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.MovimientoReclamo
import ar.edu.uade.models.Rubro.Rubros
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.select
import ar.edu.uade.models.MovimientoReclamo.MovimientosReclamo
import ar.edu.uade.models.Reclamo
import ar.edu.uade.models.Reclamo.Reclamos
import ar.edu.uade.utilities.Delay
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class MovimientoReclamoDAOFacadeMySQLImpl: MovimientoReclamoDAOFacade {

    private fun resultRowToMovimientoReclamo(row: ResultRow) = MovimientoReclamo(
        idMovimiento = row[MovimientosReclamo.idMovimiento],
        responsable = row[MovimientosReclamo.responsable],
        causa = row[MovimientosReclamo.causa],
        fecha = row[MovimientosReclamo.fecha],
        idReclamo = row[MovimientosReclamo.idReclamo]
    )

    override suspend fun getMovimientosByDelay(): List<MovimientoReclamo> = dbQuery {
        MovimientosReclamo.select { MovimientosReclamo.fecha greater LocalDateTime.now().minusMinutes(1) }
        .map(::resultRowToMovimientoReclamo)
    }

    override suspend fun getMovimientosReclamo(id: Int): List<MovimientoReclamo> = dbQuery {
        MovimientosReclamo.select(MovimientosReclamo.idReclamo eq id).map(::resultRowToMovimientoReclamo)
    }

}