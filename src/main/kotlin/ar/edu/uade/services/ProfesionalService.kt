package ar.edu.uade.services

import ar.edu.uade.daos.ProfesionalDAOFacade
import ar.edu.uade.daos.ProfesionalDAOFacadeMySQLImpl
import ar.edu.uade.mappers.MapImagenProfesional
import ar.edu.uade.mappers.MapProfesional
import ar.edu.uade.models.Profesional
import ar.edu.uade.utilities.CloudinaryConfig
import com.cloudinary.utils.ObjectUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.collections.ArrayList
import kotlin.math.ceil

class ProfesionalService {
    private val dao: ProfesionalDAOFacade = ProfesionalDAOFacadeMySQLImpl()

    suspend fun getCantidadPaginas(): Int {
        return ceil(dao.getCantidadElementos().toDouble() / 10).toInt()
    }

    suspend fun get10Profesionales(pagina: Int): MutableList<MapProfesional> {
        val profesionales = dao.get10Profesionales(pagina)
        val mapsProfesionales: MutableList<MapProfesional> = ArrayList<MapProfesional>();
        for (profesional in profesionales) {
            val mapsImagenes: MutableList<String> = ArrayList<String>();
            val imagenes = dao.getFotos(profesional.idProfesional)
            for (imagen in imagenes) {
                mapsImagenes.add(imagen.urlImagen)
            }
            mapsProfesionales.add(MapProfesional(
                profesional.nombre,
                profesional.rubro,
                profesional.descripcion,
                profesional.direccion,
                profesional.telefono,
                profesional.email,
                profesional.latitud,
                profesional.longitud,
                profesional.inicioJornada,
                profesional.finJornada,
                profesional.documento,
                mapsImagenes
            ))
        }
        return mapsProfesionales
    }

    suspend fun addProfesional(mapProfesional: MapProfesional, cloudinaryConfig: CloudinaryConfig): Profesional? {
        val profesional: Profesional? = dao.addProfesional(
            mapProfesional.nombre,
            mapProfesional.rubro,
            mapProfesional.descripcion,
            mapProfesional.direccion,
            mapProfesional.telefono,
            mapProfesional.email,
            mapProfesional.latitud,
            mapProfesional.longitud,
            mapProfesional.inicioJornada,
            mapProfesional.finJornada,
            mapProfesional.documento
        )
        if (profesional != null) {
            val idProfesional: Int = profesional.idProfesional
            for (image in mapProfesional.images) {
                val imageBytes = Base64.getDecoder().decode(image)
                val uploadResult = cloudinaryConfig.cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap())
                val imageUrl = uploadResult["url"] as String
                dao.addImagenToProfesional(
                    imageUrl,
                    idProfesional
                )
            }
        }
        return profesional
    }

    suspend fun habilitarProfesional(idProfesional: Int): Boolean {
        val credencialService = CredencialService()
        val documento = dao.getProfesional(idProfesional)?.documento
        val credencial = documento?.let { credencialService.getDAO().findCredencialByDocumento(it) }
        if (credencial != null) {
            sendEmail(credencial.email, "src/main/resources/email_template.html")
        }
        return dao.habilitarProfesional(idProfesional)
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