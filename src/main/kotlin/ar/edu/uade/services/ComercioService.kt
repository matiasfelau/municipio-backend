package ar.edu.uade.services

import ar.edu.uade.daos.ComercioDAOFacade
import ar.edu.uade.daos.ComercioDAOFacadeMySQLImpl
import ar.edu.uade.mappers.MapComercio
import ar.edu.uade.mappers.MapProfesional
import ar.edu.uade.models.Comercio
import ar.edu.uade.models.Denuncia
import ar.edu.uade.models.Profesional
import ar.edu.uade.utilities.CloudinaryConfig
import com.cloudinary.utils.ObjectUtils
import io.ktor.server.config.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.collections.ArrayList
import kotlin.math.ceil

class ComercioService(config: ApplicationConfig) {
    private val dao: ComercioDAOFacade = ComercioDAOFacadeMySQLImpl()

    suspend fun getComercios(pagina: Int): List<Comercio>{
        return dao.get10Comercio(pagina)
    }

    suspend fun getComercioById(id: Int): Comercio? {
        return  dao.getComercioByID(id)
    }

    suspend fun get10Comercios(pagina: Int): List<Comercio> {
        return dao.get10Comercio(pagina)
    }

    suspend fun getCantidadPaginas(): Int {
        val cantpaginas = dao.getallCantidadPaginas()
        val resultado = cantpaginas.toDouble()/10
        return ceil(resultado).toInt()
    }

    suspend fun addComercio(mapComercio: MapComercio, cloudinaryConfig: CloudinaryConfig) : Comercio? {
        val comercio: Comercio? = dao.addComercio(
            mapComercio.nombre,
            mapComercio.documento,
            mapComercio.direccion,
            mapComercio.descripcion,
            mapComercio.telefono,
            mapComercio.apertura,
            mapComercio.cierre,
            mapComercio.latitud,
            mapComercio.longitud
        )
        if (comercio != null){
            val idComercio: Int = comercio.idComercio
            println(mapComercio.images.get(0))
            for (image in mapComercio.images){
                val imageBytes = Base64.getDecoder().decode(image)
                val uploadResult = cloudinaryConfig.cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap())
                val imageUrl = uploadResult["url"] as String
                dao.addImagenToComercio(
                    imageUrl,
                    idComercio
                )
            }
        }
        return comercio
    }

    suspend fun getFotos(id: Int): MutableList<String> {
        val resultado: MutableList<String> = ArrayList()
        for (foto in dao.getFotosById(id)) {
            resultado.add(foto.urlImagen)
        }
        return resultado
    }

    suspend fun getComercioByNomYDir(nombre: String, direccion: String): Int {
        val result = dao.getComercioByNomYDir(nombre, direccion)
        if (result != null){
            return result.idComercio
        }
        return Int.MAX_VALUE

    }

    suspend fun getComerciosByVecino(documentoVecino: String): List<Comercio> {
        return dao.getComerciosByVecino(documentoVecino)
    }
    suspend fun habilitarComercio(idComercio: Int): Boolean {
        val credencialService = CredencialService()
        val documento = dao.getComercioByID(idComercio)?.documento
        val credencial = documento?.let { credencialService.getDAO().findCredencialByDocumento(it) }
        if (credencial != null) {
            sendEmail(credencial.email, "src/main/resources/email_template3.html")
        }
        return dao.habilitarComercio(idComercio)
    }

    private fun sendEmail(email: String, path: String) {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.mailersend.net")  // Cambia esto a tu servidor SMTP
            put("mail.smtp.port", "587")
        }


        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("MS_ad3RME@trial-jy7zpl9xwxpl5vx6.mlsender.net", "wBaAJtWflHV43KOl")
            }
        })

        try {
            var htmlContent = Files.readString(Paths.get(path))
            val message = MimeMessage(session)
            message.setFrom(InternetAddress("MS_ad3RME@trial-jy7zpl9xwxpl5vx6.mlsender.net"))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            message.subject = "Su negocio/servicio en el municipio de Palmas de Mallorca fue habilitado"
            message.setContent(htmlContent,"text/html; charset=utf-8"); //TODO REEMPLAZAR NOMBRE Y PASSWORD O AL MENOS PASSWORD
            //message.setText("Su contraseña es: $password")
            Transport.send(message)
            println("Email sent successfully")
        } catch (e: MessagingException) {
            e.printStackTrace()
        }

    }

}