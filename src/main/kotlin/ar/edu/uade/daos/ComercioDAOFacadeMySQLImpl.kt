package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Comercio
import ar.edu.uade.models.ComercioImagen
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class ComercioDAOFacadeMySQLImpl: ComercioDAOFacade {

    private fun resultRowToComercio(row: ResultRow) = Comercio(
        idComercio = row[Comercio.Comercios.idComercio],
        nombre = row[Comercio.Comercios.nombre],
        apertura = row[Comercio.Comercios.apertura],
        cierre = row[Comercio.Comercios.cierre],
        direccion = row[Comercio.Comercios.direccion],
        telefono = row[Comercio.Comercios.telefono],
        descripcion = row[Comercio.Comercios.descripcion],
        latitud = row[Comercio.Comercios.latitud],
        longitud = row[Comercio.Comercios.longitud],
        documento = row[Comercio.Comercios.documento]
    )

    private fun resultRowToComercioImagen(row: ResultRow) = ComercioImagen(
        idComercio = row[ComercioImagen.Comerciomagenes.idComercio],
        urlImagen = row[ComercioImagen.Comerciomagenes.urlImagen]
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

    override suspend fun addComercio(comercio: Comercio): Comercio? = dbQuery{
        val insertStatement = Comercio.Comercios.insert {
            it[Comercio.Comercios.idComercio] = comercio.idComercio
            it[Comercio.Comercios.nombre] = comercio.nombre
            it[Comercio.Comercios.apertura] = comercio.apertura
            it[Comercio.Comercios.cierre] = comercio.cierre
            it[Comercio.Comercios.direccion] = comercio.direccion
            it[Comercio.Comercios.telefono] = comercio.telefono
            it[Comercio.Comercios.descripcion] = comercio.descripcion
            it[Comercio.Comercios.latitud] = comercio.latitud
            it[Comercio.Comercios.longitud] = comercio.longitud
            it[Comercio.Comercios.documento] = comercio.documento
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToComercio)
    }

    override suspend fun addImagenToComercio(idComercio: Int, urlImagen: String) = dbQuery {
        val insertStatement = ComercioImagen.Comerciomagenes.insert {
            it[ComercioImagen.Comerciomagenes.idComercio] = idComercio
            it[ComercioImagen.Comerciomagenes.urlImagen] = urlImagen
        }
    }

    override suspend fun getFotosById(id: Int): List<ComercioImagen> = dbQuery {
        ComercioImagen.Comerciomagenes.select{ ComercioImagen.Comerciomagenes.idComercio eq id }
            .map(::resultRowToComercioImagen)
    }
}