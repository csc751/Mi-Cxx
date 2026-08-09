// Workaround: aggressively override JVM toolchain to NOT require JetBrains vendor or vendor-specific impl.
// We have JBR 21 (JetBrains) installed locally; this ensures Gradle picks it up without auto-download.
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.jvm.toolchain.JvmImplementation

// 1. Disable foojay resolver completely; we rely on local JDK installations.
beforeSettings { settings ->
    settings.plugins.withId("org.gradle.toolchains.foojay-resolver-convention") {
        // Block the convention plugin from wiring auto-provisioning via foojay
        try {
            it.javaClass.methods.find { m -> m.name == "disable" || m.name.contains("disable", true) }?.invoke(it)
        } catch (_: Throwable) {}
    }
}

// 2. After each project is evaluated, strip vendor/implementation constraints from java.toolchain
//    so Gradle will accept ANY JDK 21 (we registered JBR 21 via gradle.properties installations.paths).
allprojects {
    afterEvaluate {
        try {
            val javaExt = extensions.findByName("java") as? org.gradle.api.plugins.JavaPluginExtension
            if (javaExt != null) {
                val tc = javaExt.toolchain
                tc.languageVersion.set(JavaLanguageVersion.of(21))
                // Reset vendor to ANY (don't require JetBrains) - JBR matches ANY too.
                try { tc.vendor.set(JvmVendorSpec.ANY) } catch (_: Throwable) {}
                // Reset implementation to VENDOR_SPECIFIC or ANY - JBR JCEF satisfies VENDOR_SPECIFIC.
                try { tc.implementation.set(JvmImplementation.VENDOR_SPECIFIC) } catch (_: Throwable) {}
            }
        } catch (_: Throwable) {}

        // Also handle Kotlin extension (Kotlin/JVM or KMP) which may set its own toolchain.
        try {
            val kotlinExt = extensions.findByName("kotlin")
            if (kotlinExt != null) {
                // For KMP: find jvmToolchain { } calls and relax constraints
                val jvmToolchainMethod = kotlinExt.javaClass.methods.find { m ->
                    m.name == "jvmToolchain" && m.parameterTypes.size == 1
                }
                if (jvmToolchainMethod != null) {
                    // Best effort: set a relaxed spec via project java extension above; it applies to kotlin too.
                }
            }
        } catch (_: Throwable) {}
    }

    // 3. Force all JavaCompile/KotlinCompile tasks to use the Gradle daemon's JDK directly
    //    as a last-resort fallback (the daemon runs with JDK 21 via gradle.properties).
    tasks.withType(JavaCompile::class.java).configureEach {
        doFirst {
            try { options.isFork = false } catch (_: Throwable) {}
        }
    }
}
