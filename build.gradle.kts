plugins {
    //trick: for the same plugin versions in all sub-modules
    id(Deps.androidApplication).version(Deps.gradleVersion).apply(false)
    id(Deps.androidLibrary).version(Deps.gradleVersion).apply(false)
    id(Deps.kotlinGradlePlugin).version(Deps.kotlinVersion).apply(false)
    id(Deps.hiltGradlePlugin).version(Deps.hiltVersion).apply(false)
    id(Deps.sqlDelightGradlePlugin).version(Deps.sqlDelightGradleVersion).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
