@file:Suppress("MemberVisibilityCanBePrivate")

object Libs {
    object Plugins {
        const val scoverage = "org.scoverage"
        const val shadow = "com.github.johnrengelman.shadow"
        const val sem_vers_pianini = "org.danilopianini.git-sensitive-semantic-versioning"
        const val javafx_plugin = "org.openjfx.javafxplugin"
    }

    object JavaFx {
        const val jfoenix = "com.jfoenix:jfoenix:" + Versions.jfoenix
        const val ikonli_core = "org.kordamp.ikonli:ikonli-core:" + Versions.ikonli
        const val ikonli_javafx = "org.kordamp.ikonli:ikonli-javafx:" + Versions.ikonli
        const val ikonli_material = "org.kordamp.ikonli:ikonli-material-pack:" + Versions.ikonli
        const val ikonli_devicon = "org.kordamp.ikonli:ikonli-devicons-pack:" + Versions.ikonli
        const val ikonli_ionicons = "org.kordamp.ikonli:ikonli-ionicons-pack:" + Versions.ikonli
        const val enzo = "eu.hansolo.enzo:Enzo:" + Versions.enzo
    }

    object Scala {
        //SCALA FX
        const val scalafx = "org.scalafx:scalafx_2.13:" + Versions.scalafx
        const val scalafx_extras = "org.scalafx:scalafx-extras_2.13:" + Versions.scalafx_extras

        //SCALA
        const val scala_library = "org.scala-lang:scala-library:" + Versions.scala_library

        //SCALA TEST
        const val scalatest = "org.scalatest:scalatest_2.13:" + Versions.scalatest
        const val scalatest_wordspec = "org.scalatest:scalatest-wordspec_2.13:" + Versions.scalatest
        const val scalatest_funspec = "org.scalatest:scalatest-funspec_2.13:" + Versions.scalatest
        const val scalatest_mustmatchers = "org.scalatest:scalatest-mustmatchers_2.13:" + Versions.scalatest
        const val scalatest_shouldmatchers = "org.scalatest:scalatest-shouldmatchers_2.13:" + Versions.scalatest
        const val scalatest_scalactic = "org.scalactic:scalactic_0.26:" + Versions.scalatest
        const val scalatest_plus = "org.scalatestplus:junit-4-12_2.13:" + Versions.scalatest_plus

        //SCALA MODULES
        const val scala_collection_contrib = "org.scala-lang.modules:scala-collection-contrib_2.13:" + Versions.scala_collection_contrib
        const val scala_parallel_collections = "org.scala-lang.modules:scala-parallel-collections_2.13:" + Versions.scala_parallel_collections
        const val scala_async = "org.scala-lang.modules:scala-async_2.13:" + Versions.scala_async

        //SCALA LIBS
        const val scala_fsa = "org.driangle:simple-state-machine_2.13:" + Versions.scala_fsa
        const val scala_refined = "eu.timepit:refined_2.13:" + Versions.scala_refined
        const val scala_cats_core = "org.typelevel:cats-core_2.13:" + Versions.scala_cats_core
        const val scala_monix = "io.monix:monix_2.13:" + Versions.scala_monix
    }

    object Akka {
        //AKKA
        const val akka_actor = "com.typesafe.akka:akka-actor_2.13:" + Versions.akka
        const val akka_remote = "com.typesafe.akka:akka-remote_2.13:" + Versions.akka
        const val akka_cluster = "com.typesafe.akka:akka-cluster_2.13:" + Versions.akka
        const val akka_cluster_tools = "com.typesafe.akka:akka-cluster-tools_2.13:" + Versions.akka

        //AKKA TEST
        const val akka_testkit = "com.typesafe.akka:akka-testkit_2.13:" + Versions.akka

        //LOGGERS
        const val akka_slf4j = "com.typesafe.akka:akka-slf4j_2.13:" + Versions.akka
    }

    object Loggers {
        const val logback = "ch.qos.logback:logback-classic:" + Versions.logback_classic
    }

    object Serializers {
        //SERIALIZERS
        const val kryo = "io.altoo:akka-kryo-serialization_2.13:" + Versions.kryo
    }

    object Parsers {
        //PARSERS
        const val jackson_annotation = "com.fasterxml.jackson.core:jackson-annotations:" + Versions.jackson
        const val jackson_core = "com.fasterxml.jackson.core:jackson-core:" + Versions.jackson
        const val jackson_databind = "com.fasterxml.jackson.core:jackson-databind:" + Versions.jackson
        const val jackson_module = "com.fasterxml.jackson.module:jackson-module-scala_2.13:" + Versions.jackson
        const val jackson_yaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:" + Versions.jackson
    }
}
