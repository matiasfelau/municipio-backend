package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.Publicacion
import ar.edu.uade.models.Publicacion.Publicaciones
import ar.edu.uade.models.PublicacionImagen
import ar.edu.uade.utilities.Autenticacion
import org.jetbrains.exposed.sql.*
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
        Publicaciones.selectAll().count().toInt()
    }

    override suspend fun nuevaPublicacion(titulo: String, descripcion: String, autor: String, fecha: String, autenticacion: Autenticacion): Publicacion? = dbQuery {
        val insertStatement = Publicaciones.insert {
            it[Publicaciones.titulo] = titulo
            it[Publicaciones.descripcion] = descripcion
            it[Publicaciones.autor] = autor
            it[Publicaciones.fecha] = fecha
        }
        insertStatement.resultedValues?.singleOrNull()?.let { rowToPublicacion(it) }
    }

    override suspend fun subirFotos(foto: PublicacionImagen, id: Int) {
        val insertStatement = PublicacionImagen.PublicacionImagenes.insert {
            it[PublicacionImagen.PublicacionImagenes.url] = foto.url
            it[PublicacionImagen.PublicacionImagenes.idPublicacion] = id
        }
    }

    private fun rowToPublicacion(row: ResultRow): Publicacion =
        Publicacion(
            id = row[Publicaciones.id],
            titulo = row[Publicaciones.titulo],
            descripcion = row[Publicaciones.descripcion],
            autor = row[Publicaciones.autor],
            fecha = row[Publicaciones.fecha]
        )

    private fun rowToImagen(row: ResultRow): PublicacionImagen =
        PublicacionImagen(
            id = row[PublicacionImagen.PublicacionImagenes.id],
            url = row[PublicacionImagen.PublicacionImagenes.url],
            idPublicacion = row[PublicacionImagen.PublicacionImagenes.idPublicacion],

        )

    override suspend fun getFotos(idPublicacion: Int): List<PublicacionImagen> = dbQuery{
        PublicacionImagen.PublicacionImagenes.select { PublicacionImagen.PublicacionImagenes.idPublicacion eq idPublicacion }.map(::rowToImagen)
    }
}