package ar.edu.uade.utilities.containers

import ar.edu.uade.utilities.Autenticacion

data class AutenticacionDenunciaVecino(
    val autenticacion: Autenticacion,
    val denunciaVecino: ContainerDenunciaVecino

    )