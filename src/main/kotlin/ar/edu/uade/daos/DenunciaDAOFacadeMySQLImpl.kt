package ar.edu.uade.daos

import ar.edu.uade.databases.MySQLSingleton.dbQuery
import ar.edu.uade.models.*
import ar.edu.uade.models.ComercioDenunciado.*
import ar.edu.uade.models.Denuncia.Denuncias
import ar.edu.uade.models.Desperfecto.Desperfectos
import ar.edu.uade.models.Reclamo.Reclamos
import ar.edu.uade.models.VecinoDenunciado.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DenunciaDAOFacadeMySQLImpl : DenunciaDAOFacade{

    private fun resultRowToDenuncia(row: ResultRow) = Denuncia(
        idDenuncia = row[Denuncias.idDenuncia],
        descripcion = row[Denuncias.descripcion],
        estado = row[Denuncias.estado],
        aceptarResponsabilidad = row[Denuncias.aceptarResponsabilidad],
        documento = row[Denuncias.documento]
    )

    private fun resultRowToVecinoDenunciado(row: ResultRow) = VecinoDenunciado(
        idDenuncia = row[VecinosDenunciados.idDenuncia],
        nombre = row[VecinosDenunciados.nombre],
        apellido = row[VecinosDenunciados.apellido],
        direccion = row[VecinosDenunciados.direccion],
        documento = row[VecinosDenunciados.documento]
    )

    private fun resultRowToComercioDenunciado(row: ResultRow) = ComercioDenunciado(
        idDenuncia = row[ComerciosDenunciados.idDenuncia],
        idComercio = row[ComerciosDenunciados.idComercio],
        nombre = row[ComerciosDenunciados.nombre],
        direccion = row[ComerciosDenunciados.direccion]
    )

    private fun resultRowToDenunciaImagen(row: ResultRow) = DenunciaImagen(
        idDenuncia = row[DenunciaImagen.DenunciaImagenes.idDenuncia],
        urlImagen = row[DenunciaImagen.DenunciaImagenes.urlImagen]
    )

    override suspend fun get10Denuncias(pagina: Int, documento: String): List<Denuncia> = dbQuery {
        val offset = (pagina - 1) * 10
        Denuncias.join(VecinosDenunciados, JoinType.FULL, additionalConstraint = { Denuncias.idDenuncia eq VecinosDenunciados.idDenuncia })
            .join(ComerciosDenunciados, JoinType.FULL, additionalConstraint = {Denuncias.idDenuncia eq ComerciosDenunciados.idDenuncia})
            .join(Comercio.Comercios, JoinType.FULL, additionalConstraint = {ComerciosDenunciados.idComercio eq Comercio.Comercios.idComercio})
            .select { Denuncias.documento like documento or(VecinosDenunciados.documento like documento) or(ComerciosDenunciados.idComercio eq Comercio.Comercios.idComercio)}
            .limit(10,offset.toLong())
            .map(::resultRowToDenuncia)
    }

    override suspend fun getDenunciaById(id: Int): Denuncia? = dbQuery {
        println("id")
        Denuncias.select { Denuncias.idDenuncia eq id }
            .map(::resultRowToDenuncia)
            .singleOrNull()
    }

    override suspend fun getVecinoDenunciado(id: Int): VecinoDenunciado? = dbQuery{
        VecinosDenunciados.select {
            VecinosDenunciados.idDenuncia eq id
        }.map(::resultRowToVecinoDenunciado).singleOrNull()
    }

    override suspend fun getComercioDenunciado(id: Int): ComercioDenunciado? = dbQuery{
        ComerciosDenunciados.select {
            ComerciosDenunciados.idDenuncia eq id
        }.map(::resultRowToComercioDenunciado).singleOrNull()
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