import sbt._

object Dependencies {

  object Versions {
    val cats                = "2.5.0"
    val catsEffect          = "2.4.1"
    val fs2                 = "2.5.4"
    val http4s              = "0.21.22"
    val circe               = "0.13.0"
    val pureConfig          = "0.14.1"

    val kindProjector       = "0.10.3"
    val logback             = "1.2.3"
    val log4cats            = "1.1.1"
    val scalaCheck          = "1.15.3"
    val scalaTest           = "3.2.7"
    val catsScalaCheck      = "0.3.0"

    val enumeratum          = "1.7.3"
    val weaverTest          = "0.4.2-RC1"
  }

  object Libraries {
    def circe(artifact: String): ModuleID = "io.circe"    %% artifact % Versions.circe
    def http4s(artifact: String): ModuleID = "org.http4s" %% artifact % Versions.http4s

    val cats                = "org.typelevel"         %% "cats-core"                  % Versions.cats
    val catsEffect          = "org.typelevel"         %% "cats-effect"                % Versions.catsEffect
    val fs2                 = "co.fs2"                %% "fs2-core"                   % Versions.fs2

    val http4sDsl           = http4s("http4s-dsl")
    val http4sServer        = http4s("http4s-blaze-server")
    val http4sCirce         = http4s("http4s-circe")
    val http4sClient        = http4s("http4s-blaze-client")
    val circeCore           = circe("circe-core")
    val circeGeneric        = circe("circe-generic")
    val circeGenericExt     = circe("circe-generic-extras")
    val circeParser         = circe("circe-parser")
    val pureConfig          = "com.github.pureconfig" %% "pureconfig"                 % Versions.pureConfig

    // Compiler plugins
    val kindProjector       = "org.typelevel"         %% "kind-projector"             % Versions.kindProjector
    val contextApplied      = "org.augustjune"        %% "context-applied"            % "0.1.3"

    // Runtime
    val log4cats            = "io.chrisdavenport"     %% "log4cats-slf4j"             % Versions.log4cats
    val logback             = "ch.qos.logback"        %  "logback-classic"            % Versions.logback

    // Test
    val scalaTest           = "org.scalatest"         %% "scalatest"                  % Versions.scalaTest
    val scalaCheck          = "org.scalacheck"        %% "scalacheck"                 % Versions.scalaCheck
    val catsScalaCheck      = "io.chrisdavenport"     %% "cats-scalacheck"            % Versions.catsScalaCheck

    val enumeratum          = "com.beachape"          %% "enumeratum-cats"            % Versions.enumeratum

    val weaverTest          = "com.disneystreaming"   %% "weaver-framework"           % Versions.weaverTest % "test,it"
    val weaverCheck         = "com.disneystreaming"   %% "weaver-scalacheck"          % Versions.weaverTest % "test,it"

    val betterMonadic       = "com.olegpy"                  %% "better-monadic-for"         % "0.3.1"
  }

}
