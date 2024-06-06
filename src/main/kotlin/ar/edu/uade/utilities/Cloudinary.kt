package ar.edu.uade.utilities

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import io.ktor.server.application.*

class CloudinaryConfig(application: Application) {
    private val apiKey = application.environment.config.property("cloudinary.api_key").getString()
    private val secretKey = application.environment.config.property("cloudinary.secret_key").getString()
    val cloudinary = Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dcmjauzp7",
        "api_key", apiKey,
        "api_secret", secretKey
    )) //todo agregar elementos guardados del ENV...
}