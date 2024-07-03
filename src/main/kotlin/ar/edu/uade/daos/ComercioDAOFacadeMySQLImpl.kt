package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Comercio
import ar.edu.uade.models.Comercio.Comercios
import ar.edu.uade.models.ComercioImagen
import ar.edu.uade.models.ComercioImagen.Comerciomagenes
import ar.edu.uade.models.Profesional
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
        longitud = row[Comercios.longitud],
        autorizado = row[Comercios.autorizado]
    )

    private fun resultRowToComercioImagen(row: ResultRow) = ComercioImagen(
        idImagen = row[Comerciomagenes.idImagen],
        urlImagen = row[Comerciomagenes.urlImagen],
        idComercio = row[Comerciomagenes.idComercio]
    )
    override suspend fun getallCantidadPaginas(): Int = dbQuery {
        Comercios.select { Comercios.autorizado eq true }
            .map(::resultRowToComercio)
            .count()
    }

    override suspend fun getComercioByID(id: Int): Comercio? = dbQuery {
        Comercios.select{ Comercios.idComercio eq id}
            .map(::resultRowToComercio)
            .singleOrNull()
    }

    override suspend fun get10Comercio(pagina: Int): List<Comercio> = dbQuery {
        val offset = (pagina -1)*10
        Comercios.select { Comercios.autorizado eq true }
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
        Comerciomagenes.select{ Comerciomagenes.idComercio eq id }
            .map(::resultRowToComercioImagen)
    }

    override suspend fun getComercioByNomYDir(nombre: String, direccion: String): Comercio? = dbQuery{
        Comercios.select{ Comercios.nombre like nombre and(Comercios.direccion like direccion)}
            .map(::resultRowToComercio).singleOrNull()
    }

    override suspend fun habilitarComercio(idComercio: Int): Boolean = dbQuery {
        Comercios.update({ Comercios.idComercio eq idComercio }) {
            it[Comercios.autorizado] = true
        } > 0
    }
    override suspend fun getComerciosByVecino(documentoVecino: String): List<Comercio> = dbQuery {
        Comercios.select { Comercios.documento eq documentoVecino and (Comercios.autorizado eq true) }
            .map(::resultRowToComercio)
    }
}