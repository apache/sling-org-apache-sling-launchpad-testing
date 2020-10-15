[![Apache Sling](https://sling.apache.org/res/logos/sling.png)](https://sling.apache.org)

&#32;[![Build Status](https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-launchpad-testing/job/master/badge/icon)](https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-launchpad-testing/job/master/)&#32;[![Test Status](https://img.shields.io/jenkins/tests.svg?jobUrl=https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-launchpad-testing/job/master/)](https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-launchpad-testing/job/master/test/?width=800&height=600)&#32;[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=apache_sling-org-apache-sling-launchpad-testing&metric=alert_status)](https://sonarcloud.io/dashboard?id=apache_sling-org-apache-sling-launchpad-testing)&#32;[![JavaDoc](https://www.javadoc.io/badge/org.apache.sling/org.apache.sling.launchpad.testing.svg)](https://www.javadoc.io/doc/org.apache.sling/org-apache-sling-launchpad-testing)&#32;[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.apache.sling/org.apache.sling.launchpad.testing/badge.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.apache.sling%22%20a%3A%22org.apache.sling.launchpad.testing%22)&#32;[![launchpad](https://sling.apache.org/badges/group-launchpad.svg)](https://github.com/apache/sling-aggregator/blob/master/docs/group/launchpad.md) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# Apache Sling Launchpad Testing

This module is part of the [Apache Sling](https://sling.apache.org) project.

It builds a Sling instance using the [Sling Starter Feature](https://github.com/apache/sling-org-apache-sling-starter) which has the same version has this module, and runs integration tests against it via HTTP.

The tests are provided by the sibling [sling-org-apache-sling-launchpad-integration-tests](https://github.com/apache/sling-org-apache-sling-launchpad-integration-tests)
module.

## How to execute and debug individual tests

To start a Sling instance with the exact same setup used in the full
build of this module, run

    mvn clean package

and then use

    java -jar target/dependency/org.apache.sling.feature.launcher.jar -f target/slingfeature-tmp/feature-testing-oak_tar.json

To start the Sling instance to test on port 8080 by default, using appropriate `java` options for server-side debugging
as needed. CTRL-C stops that instance.

The tests of the [sling-org-apache-sling-launchpad-integration-tests](https://github.com/apache/sling-org-apache-sling-launchpad-integration-tests) module can then be run against this instance, as described
in the README of that module.
