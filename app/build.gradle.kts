plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "ma.projet.restclient"

    // Niveau d'API utilisé pour compiler
    compileSdk = 35

    defaultConfig {
        applicationId = "ma.projet.restclient"

        // Version minimale supportée (Android 7.0+ ici)
        minSdk = 24

        // Niveau d'API ciblé (comportement moderne)
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // On force Java 17 (tu es déjà en JavaVersion.VERSION_17)
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    /* --- UI de base --- */

    // AppCompat (barre d'action, compatibilité thèmes)
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Material Design (FloatingActionButton, TextInputLayout, Card, etc.)
    implementation("com.google.android.material:material:1.12.0")

    // ConstraintLayout (si tu l'utilises dans tes layouts)
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    // RecyclerView pour la liste des comptes
    implementation("androidx.recyclerview:recyclerview:1.3.2")


    /* --- Retrofit + convertisseurs --- */

    // REST client
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Conversion JSON <-> objets Java
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Conversion XML <-> objets Java
    implementation("com.squareup.retrofit2:converter-simplexml:2.9.0")
    implementation("org.simpleframework:simple-xml:2.7.1")


    /* --- Tests --- */

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
