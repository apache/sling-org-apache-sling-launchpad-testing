[<img src="https://sling.apache.org/res/logos/sling.png"/>](https://sling.apache.org)

 [![Build Status](https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-launchpad-testing/job/master/badge/icon)](https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-launchpad-testing/job/master/) [![Test Status](https://img.shields.io/jenkins/tests.svg?jobUrl=https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-launchpad-testing/job/master/)](https://ci-builds.apache.org/job/Sling/job/modules/job/sling-org-apache-sling-launchpad-testing/job/master/test/?width=800&height=600) [![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=apache_sling-org-apache-sling-launchpad-testing&metric=alert_status)](https://sonarcloud.io/dashboard?id=apache_sling-org-apache-sling-launchpad-testing) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.apache.sling/org.apache.sling.launchpad.testing/badge.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.apache.sling%22%20a%3A%22org.apache.sling.launchpad.testing%22) [![JavaDocs](https://www.javadoc.io/badge/org.apache.sling/org.apache.sling.launchpad.testing.svg)](https://www.javadoc.io/doc/org.apache.sling/org.apache.sling.launchpad.testing) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0) [![launchpad](https://sling.apache.org/badges/group-launchpad.svg)](https://github.com/apache/sling-aggregator/blob/master/docs/groups/launchpad.md)

# Apache Sling Launchpad Testing

This module is part of the [Apache Sling](https://sling.apache.org) project.

This module builds a Sling instance using bundles from the trunk, and
runs integration tests against it via HTTP.

## Default build with integration tests

The integration tests are provided by the sibling [sling-org-apache-sling-launchpad-integration-tests](https://github.com/apache/sling-org-apache-sling-launchpad-integration-tests)
module. By default the Sling instance to test is started, including a
few test-specific bundles, the integration tests are executed and 
the instance is stopped.

## Executing individual tests

To start a Sling instance with the exact same setup used in the full
build of this module, use

    mvn clean install -Dlaunchpad.keep.running=true -Dhttp.port=8080

Adding `-Dsling.debug.options="<debug options>"` for server-side debugging if needed.

Use CTRL-C to stop that instance.

The tests of the [sling-org-apache-sling-launchpad-integration-tests](https://github.com/apache/sling-org-apache-sling-launchpad-integration-tests) module can then be run against this instance, as described there.
