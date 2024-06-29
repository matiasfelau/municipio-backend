package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.*
import ar.edu.uade.models.ComercioDenunciado.*
import ar.edu.uade.models.Denuncia.Denuncias
import ar.edu.uade.models.VecinoDenunciado.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class DenunciaDAOFacadeMySQLImpl : DenunciaDAOFacade{

    private fun resultRowToDenuncia(row: ResultRow) = Denuncia(
        idDenuncia = row[Denuncias.idDenuncia],
        descripcion = row[Denuncias.descripcion],
        estado = row[Denuncias.estado],
        aceptarResponsabilidad = row[Denuncias.aceptarResponsabilidad],
        documento = row[Denuncias.documento]
    )

    private fun resultRowToDenunciaImagen(row: ResultRow) = DenunciaImagen(
        idDenuncia = row[DenunciaImagen.DenunciaImagenes.idDenuncia],
        urlImagen = row[DenunciaImagen.DenunciaImagenes.urlImagen]
    )

    override suspend fun get10Denuncias(pagina: Int): List<Denuncia> = dbQuery {
        val offset = (pagina - 1) * 10
        println("todos")
        Denuncias.selectAll()
            .limit(10,offset.toLong())
            .map(::resultRowToDenuncia)
    }

    override suspend fun getDenunciaById(id: Int): Denuncia? = dbQuery {
        println("id")
        Denuncias.select { Denuncias.idDenuncia eq id }
            .map(::resultRowToDenuncia)
            .singleOrNull()
    }

    override suspend fun addDenuncia(denuncia: Denuncia): Denuncia? = dbQuery {
        val insertStatement = Denuncias.insert{
            it[Denuncias.descripcion] = denuncia.descripcion
            it[Denuncias.estado] = denuncia.estado
            it[Denuncias.aceptarResponsabilidad] = denuncia.aceptarResponsabilidad
            it[Denuncias.documento] = denuncia.documento
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToDenuncia)

    }

    override suspend fun addComercioDenunciado(id: Int, comercioDenunciado: ComercioDenunciado){
        val insertStatement = ComerciosDenunciados.insert {
            it[ComerciosDenunciados.idComercio] = comercioDenunciado.idComercio
            it[ComerciosDenunciados.idDenuncia] = id
            it[ComerciosDenunciados.nombre] = comercioDenunciado.nombre
            it[ComerciosDenunciados.direccion] = comercioDenunciado.direccion
        }
    }

    override suspend fun addVecinoDenunciado(id: Int, vecinoDenunciado: VecinoDenunciado) = dbQuery {
        val insertStatement = VecinosDenunciados.insert {
            it[VecinosDenunciados.idDenuncia] = id
            it[VecinosDenunciados.documento] =  vecinoDenunciado.documento
            it[VecinosDenunciados.nombre] = vecinoDenunciado.nombre
            it[VecinosDenunciados.apellido] = vecinoDenunciado.apellido
            it[VecinosDenunciados.direccion] = vecinoDenunciado.direccion
        }
    }

    override suspend fun addImagenToDenuncia(idDenuncia: Int, urlImagen: String) = dbQuery {
        val insertStatement = DenunciaImagen.DenunciaImagenes.insert {
            it[DenunciaImagen.DenunciaImagenes.idDenuncia] = idDenuncia
            it[DenunciaImagen.DenunciaImagenes.urlImagen] = urlImagen
        }
    }

    override suspend fun getAllCantidadPaginas(): Int = dbQuery {
        Denuncias.selectAll()
            .map(::resultRowToDenuncia)
            .count()
    }

    override suspend fun getFotosById(id: Int): List<DenunciaImagen> = dbQuery {
        DenunciaImagen.DenunciaImagenes.select{ DenunciaImagen.DenunciaImagenes.idDenuncia eq id }
            .map(::resultRowToDenunciaImagen)
    }

}