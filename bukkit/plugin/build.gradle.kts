plugins {
    id("apollo.paper-conventions")
}

dependencies {
    compileOnlyApi(project(":apollo-common"))

    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}
