[<img src="https://sling.apache.org/res/logos/sling.png"/>](https://sling.apache.org)

 [![Build Status](https://builds.apache.org/buildStatus/icon?job=Sling/sling-org-apache-sling-launchpad-testing/master)](https://builds.apache.org/job/Sling/job/sling-org-apache-sling-launchpad-testing/job/master) [![Test Status](https://img.shields.io/jenkins/t/https/builds.apache.org/job/Sling/job/sling-org-apache-sling-launchpad-testing/job/master.svg)](https://builds.apache.org/job/Sling/job/sling-org-apache-sling-launchpad-testing/job/master/test_results_analyzer/) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.apache.sling/org.apache.sling.launchpad.testing/badge.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.apache.sling%22%20a%3A%22org.apache.sling.launchpad.testing%22) [![JavaDocs](https://www.javadoc.io/badge/org.apache.sling/org.apache.sling.launchpad.testing.svg)](https://www.javadoc.io/doc/org.apache.sling/org.apache.sling.launchpad.testing) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0) [![launchpad](https://sling.apache.org/badges/group-launchpad.svg)](https://github.com/apache/sling-aggregator/blob/master/docs/groups/launchpad.md)

# Apache Sling Launchpad Testing

This module is part of the [Apache Sling](https://sling.apache.org) project.

This module builds a Sling instance using bundles from the trunk, and
runs integration tests against it via HTTP.

## Default build with integration tests

The integration tests are provided by the sibling integration-tests 
module. By default the Sling instance to test is started, including a
few test-specific bundles, the integration tests are executed and 
the instance is stopped.

## Executing individual tests

To run individual tests against this instance, with the exact same setup used
in the full build, use

  mvn clean install -Dlaunchpad.keep.running=true -Dhttp.port=8080 -Ddebug

The -Ddebug option enables server-side debugging of the instance under test, 
on port 8000. It can be omitted, of course.

Use CTRL-C to stop that instance.

See the README.txt in the integration-tests module for how to run specific 
tests against that instance.
