plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.20.1-forge"

stonecutter parameters {
    val loader = current.project.substringAfterLast('-')
    constants {
        match(loader, "forge", "neoforge")
    }
}