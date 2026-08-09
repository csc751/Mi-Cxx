// Copyright 2026, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

// For standalone Kotlin/JVM projects
plugins.withId("org.jetbrains.kotlin.jvm") {
    project.extensions.findByType(KotlinJvmProjectExtension::class.java)?.apply {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}
