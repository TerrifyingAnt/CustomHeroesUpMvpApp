
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id ("kotlin-parcelize")


}

android {
    namespace = "jg.coursework.customheroesapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "jg.coursework.customheroesapp"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


}



dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // di
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // extended icons
    implementation("androidx.compose.material:material-icons-extended:1.5.0-alpha04")

    // compose navigation
    implementation("androidx.navigation:navigation-compose:2.6.0-rc01")

    // test
    testImplementation("com.google.truth:truth:1.1.3")

    // full-screen
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.31.2-alpha")
    implementation ("com.google.accompanist:accompanist-insets:0.31.2-alpha")
    implementation ("com.google.accompanist:accompanist-insets-ui:0.31.2-alpha")

    // retorfit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")

    // coil
    implementation("io.coil-kt:coil-compose:1.3.2")

    // minio
    implementation ("io.minio:minio:8.4.3")

    // https://mvnrepository.com/artifact/javax.xml.parsers/jaxp-api
    implementation("javax.xml.parsers:jaxp-api:1.4.5")

// https://mvnrepository.com/artifact/com.fasterxml.woodstox/woodstox-core
    implementation("com.fasterxml.woodstox:woodstox-core:6.5.1")

// https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-parcelize-runtime
    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:1.9.0-Beta")











}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
