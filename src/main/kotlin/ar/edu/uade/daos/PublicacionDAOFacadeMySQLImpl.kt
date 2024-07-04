package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.ComercioImagen.Comerciomagenes
import ar.edu.uade.models.Profesional.Profesionales
import ar.edu.uade.models.Publicacion
import ar.edu.uade.models.Publicacion.Publicaciones
import ar.edu.uade.models.PublicacionImagen
import ar.edu.uade.utilities.Autenticacion
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PublicacionDAOFacadeMySQLImpl : PublicacionDAOFacade {
    override suspend fun get10Publicaciones(pagina: Int): List<Publicacion> = dbQuery {
        Publicaciones.selectAll()
            .limit(10, offset = ((pagina - 1) * 10).toLong())
            .map { rowToPublicacion(it) }
    }

    override suspend fun getPublicacionByID(id: Int): Publicacion? = dbQuery {
        Publicaciones.select { Publicaciones.id eq id }
            .mapNotNull { rowToPublicacion(it) }
            .singleOrNull()
    }

    override suspend fun getAllCantidadPaginas(): Int = dbQuery {
        Publicaciones.select{Publicaciones.aprobado eq true }
            .map(::rowToPublicacion)
            .count()

    }

    override suspend fun nuevaPublicacion(titulo: String,
                                          descripcion: String,
                                          autor: String,
                                          fecha: String): Publicacion? = dbQuery {
        val insertStatement = Publicaciones.insert {
            it[Publicaciones.titulo] = titulo
            it[Publicaciones.descripcion] = descripcion
            it[Publicaciones.autor] = autor
            it[Publicaciones.fecha] = fecha
            it[Publicaciones.aprobado]=false
        }
        insertStatement.resultedValues?.singleOrNull()?.let (::rowToPublicacion)
    }

    override suspend fun subirFotos(foto: PublicacionImagen, id: Int) {
        val insertStatement = PublicacionImagen.PublicacionImagenes.insert {
            it[PublicacionImagen.PublicacionImagenes.url] = foto.url
            it[PublicacionImagen.PublicacionImagenes.idPublicacion] = id
        }
    }

    private fun rowToPublicacion(row: ResultRow) = Publicacion(
            id = row[Publicaciones.id],
            titulo = row[Publicaciones.titulo],
            descripcion = row[Publicaciones.descripcion],
            autor = row[Publicaciones.autor],
            fecha = row[Publicaciones.fecha],
            aprobado = row[Publicaciones.aprobado]
        )

    private fun rowToImagen(row: ResultRow): PublicacionImagen = PublicacionImagen(
            id = row[PublicacionImagen.PublicacionImagenes.id],
            url = row[PublicacionImagen.PublicacionImagenes.url],
            idPublicacion = row[PublicacionImagen.PublicacionImagenes.idPublicacion],

        )

    override suspend fun getFotos(idPublicacion: Int): List<PublicacionImagen> = dbQuery{
        PublicacionImagen.PublicacionImagenes.select { PublicacionImagen.PublicacionImagenes.idPublicacion eq idPublicacion }
            .map(::rowToImagen)
    }

    override suspend fun aprobarPublicacion(idPublicacion: Int): Boolean = dbQuery {
        Publicaciones.update({ Publicaciones.id eq idPublicacion }) {
            it[Publicaciones.aprobado] = true
        } > 0
    }

    override suspend fun addFotoPublicacion(urlImagen: String, id: Int): PublicacionImagen? = dbQuery {
        val insertStatement = PublicacionImagen.PublicacionImagenes.insert {
            it[PublicacionImagen.PublicacionImagenes.url] = urlImagen
            it[PublicacionImagen.PublicacionImagenes.idPublicacion] = id
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::rowToImagen)
    }
}