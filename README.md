# FLOWX Quickstart for Connectors

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ec3388834bc74032ac69bdf8adf050f3)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=flowx-ai/quickstart-connector&amp;utm_campaign=Badge_Grade)
[![Java version](https://img.shields.io/badge/java%20version-11-yellow.svg?color=yellow)](https://adoptopenjdk.net/index.html?variant=openjdk11&jvmVariant=hotspot)
[![String boot version](https://img.shields.io/badge/spring%20boot%20version-2.5.4-9cf.svg?color=9cf)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/license-MIT-orange.svg)](https://opensource.org/licenses/MIT)

Basic setup for a connector.

Includes:
  - Kafka related configs and listener & message sender examples
  - Jaeger related configs & example
  - config example for activating custom health checks

Please check and follow the TODOs in the code for implementing your own custom FLOWX connector:

1. choose a meaningful name for your connector service and set it in the configuration file
2. decide what topic should the connector listen on and set it in the configuration file
3. decide what topic should the connector reply on (this topic name must match the topic pattern the Engine listens on)
4. adjust number of consumer threads. make sure number of instances * number of threads = number of partitions per topic
5. define the incoming DTO format
6. define the outgoing DTO format
7. implement the business logic for handling messages received from the Engine and sending back a reply
8. make sure to send back the process instance uuid as a key for the Kafka message

optional steps:
- decide whether you want to use jaeger tracing in your setup and choose a prefix name in teh configuration file
- enable health check for all the services you use in the service

[Built with](#built-with) | [Usage](#usage) | [Contributing](#contributing) | [License](#license)

## Built with

Java 11 & Spring Boot 2.5.4

Uses two custom libraries which are available on [github packages](https://github.com/orgs/flowx-ai/packages?repo_name=public-mvn-packages). To be able to download them using maven, you need to authenticate to github using a personal access token with `read:packages` scope (go to you github account -> settings -> developer settings - > personal access tokens -> tokens (classic) -> generate new token).

An error might occur if you run your project using Intellij, to prevent it you need to delegate IDE build/run action to maven (go to preferences -> maven -> runner -> check the `delegate IDE build/run action to maven` checkbox) .

### Maven settings
Update settings (Source: [Github Maven Packages](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)):
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>central</id>
          <url>https://repo1.maven.org/maven2</url>
        </repository>
        <repository>
          <id>github</id>
          <url>https://maven.pkg.github.com/flowx-ai/public-mvn-packages</url>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <servers>
    <server>
      <id>github</id>
      <username>USERNAME</username>
      <password>TOKEN</password>
    </server>
  </servers>
</settings>
```

## Usage

Prerequisites:
  - [Kafka](https://kafka.apache.org/) engine
  - [Jaeger](https://www.jaegertracing.io) agent

A docker-compose file is provided for starting these dependencies locally.

## Contributing

### Branching model

- `master` branch should always have the latest code that is in production.
- `feature/issue_code` branches that fix specific github issues
- `feature/feature_name` branches for new features
- `bugfix/issue_code` branches that fix specific bugs

### Code quality

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ec3388834bc74032ac69bdf8adf050f3)](https://www.codacy.com?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=flowx-ai/quickstart-connector&amp;utm_campaign=Badge_Grade)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

