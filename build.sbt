name := "PhoneNumberSearchApp"
 
version := "1.0"
 
scalaVersion := "2.10.2"
 
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "com.typesafe.akka"   %% "akka-actor"        % akkaV,
    "org.scalaj"          %% "scalaj-http"       % "1.1.4",
    "com.typesafe.akka"   %% "akka-testkit"      % akkaV    % "test",
    "org.specs2"          %% "specs2-core"       % "2.3.11" % "test",
    "org.specs2"          %% "specs2"            % "2.3.11" % "test",
    "org.scalatest"       %  "scalatest_2.11"    % "2.2.1"  % "test",
    "junit"               % "junit"              % "4.11"   % "test"
  )
}
