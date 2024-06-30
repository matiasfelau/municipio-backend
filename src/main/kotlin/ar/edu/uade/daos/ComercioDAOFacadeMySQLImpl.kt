package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Comercio
import ar.edu.uade.models.Comercio.Comercios
import ar.edu.uade.models.ComercioImagen
import ar.edu.uade.models.ComercioImagen.Comerciomagenes
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import java.math.BigDecimal
import java.time.LocalTime

class ComercioDAOFacadeMySQLImpl: ComercioDAOFacade {

    private fun resultRowToComercio(row: ResultRow) = Comercio(
        idComercio = row[Comercios.idComercio],
        nombre = row[Comercios.nombre],
        documento = row[Comercios.documento],
        direccion = row[Comercios.direccion],
        descripcion = row[Comercios.descripcion],
        telefono = row[Comercios.telefono],
        apertura = row[Comercios.apertura],
        cierre = row[Comercios.cierre],
        latitud = row[Comercios.latitud],
        longitud = row[Comercios.longitud]
    )

    private fun resultRowToComercioImagen(row: ResultRow) = ComercioImagen(
        idImagen = row[Comerciomagenes.idImagen],
        urlImagen = row[Comerciomagenes.urlImagen],
        idComercio = row[Comerciomagenes.idComercio]
    )
    override suspend fun getallCantidadPaginas(): Int = dbQuery {
        Comercio.Comercios.selectAll()
            .map(::resultRowToComercio)
            .count()
    }

    override suspend fun getComercioByID(id: Int): Comercio? = dbQuery {
        println("id")
        Comercio.Comercios.select{ Comercio.Comercios.idComercio eq id}
            .map(::resultRowToComercio)
            .singleOrNull()
    }

    override suspend fun get10Comercio(pagina: Int): List<Comercio> = dbQuery {
        val offset = (pagina -1)*10
        println("todos")
        Comercio.Comercios.selectAll()
            .limit(10, offset.toLong())
            .map(::resultRowToComercio)
    }

    override suspend fun addComercio(nombre:String,
                                     documento: String?,
                                     direccion: String?,
                                     descripcion:String?,
                                     telefono: Int?,
                                     apertura: LocalTime?,
                                     cierre: LocalTime?,
                                     latitud: BigDecimal?,
                                     longitud: BigDecimal?): Comercio? = dbQuery{


        val insertStatement = Comercios.insert {
            it[Comercios.nombre] = nombre
            it[Comercios.documento] = documento
            it[Comercios.direccion] = direccion
            it[Comercios.descripcion] = descripcion
            it[Comercios.telefono] = telefono
            it[Comercios.apertura] = apertura
            it[Comercios.cierre] = cierre
            it[Comercios.latitud] = latitud
            it[Comercios.longitud] = longitud
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToComercio)
    }

    override suspend fun addImagenToComercio(urlImagen: String, idComercio: Int): ComercioImagen? = dbQuery {
        val insertStatement = Comerciomagenes.insert {
            it[Comerciomagenes.urlImagen] = urlImagen
            it[Comerciomagenes.idComercio] = idComercio
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToComercioImagen)
    }

    override suspend fun getFotosById(id: Int): List<ComercioImagen> = dbQuery {
        ComercioImagen.Comerciomagenes.select{ ComercioImagen.Comerciomagenes.idComercio eq id }
            .map(::resultRowToComercioImagen)
    }
}