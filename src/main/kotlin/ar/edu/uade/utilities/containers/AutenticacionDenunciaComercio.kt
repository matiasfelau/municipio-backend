package ar.edu.uade.utilities.containers

import ar.edu.uade.utilities.Autenticacion

data class AutenticacionDenunciaComercio(
    val autenticacion: Autenticacion,
    val denunciaComercio: ContainerDenunciaComercio
)