package ies.sequeros.ejemplosktor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform