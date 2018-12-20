name := """TweetAnalytics"""

version := "2.6.x"

inThisBuild(
  List(
    scalaVersion := "2.12.3",
    dependencyOverrides := Set(
       "org.codehaus.plexus" % "plexus-utils" % "3.0.18",
       "com.google.code.findbugs" % "jsr305" % "3.0.1",
       "com.google.guava" % "guava" % "22.0",
       "com.typesafe.akka" %% "akka-stream" % "2.5.10",
       "com.typesafe.akka" %% "akka-actor" % "2.5.10"
    )
  )
)


lazy val GatlingTest = config("gatling") extend Test

lazy val root = (project in file(".")).enablePlugins(PlayJava, GatlingPlugin).configs(GatlingTest)
  .settings(inConfig(GatlingTest)(Defaults.testSettings): _*)
  .settings(
    scalaSource in GatlingTest := baseDirectory.value / "/gatling/simulation"
  )

libraryDependencies += guice
libraryDependencies += javaJpa
libraryDependencies += "com.h2database" % "h2" % "1.4.194"

libraryDependencies += "org.hibernate" % "hibernate-core" % "5.2.9.Final"
libraryDependencies += "io.dropwizard.metrics" % "metrics-core" % "3.2.1"
libraryDependencies += "com.palominolabs.http" % "url-builder" % "1.1.0"
libraryDependencies += "net.jodah" % "failsafe" % "1.0.3"

libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.0" % Test
libraryDependencies += "io.gatling" % "gatling-test-framework" % "2.3.0" % Test
libraryDependencies ++= Seq(
  ws
)
libraryDependencies += "org.hamcrest" % "hamcrest-all" % "1.3" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "2.15.0" % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.10" % Test

PlayKeys.externalizeResources := false

testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

EclipseKeys.preTasks := Seq(compile in Compile, compile in Test)


