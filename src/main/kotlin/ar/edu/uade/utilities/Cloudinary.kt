package ar.edu.uade.utilities

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils

object CloudinaryConfig {
    val cloudinary = Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dcmjauzp7",
        "api_key", "your_api_key",
        "api_secret", "your_api_secret"
    )) //todo agregar elementos guardados del ENV...
}