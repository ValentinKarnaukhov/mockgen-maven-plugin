# mockgen-maven-plugin

A Maven plugin to create source code to build mocks for Wiremock (https://github.com/tomakehurst/wiremock) framework based on
Swagger (https://github.com/swagger-api) spec.

<H1>Usage</H1>

Add plugin to your pom.xml file.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.github.valentinkarnaukhov</groupId>
            <artifactId>mockgen-maven-plugin</artifactId>
            <version>1.0.0-SNAPSHOT</version>
            <executions>
                <execution>
                    <id>generate-stubs</id>
                    <goals>
                        <goal>generate-stubs</goal>
                    </goals>
                    <configuration>
                        <inputSpec>${project.basedir}/src/test/resources/test_swagger_2_0.yaml</inputSpec>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
Run build
`mvn clean install`

<H3>Configuration parameters</H3>

- `inputSpec` - Swagger spec file path
- `modelPackage` - package of existing models or output package
- `generateModels` - flag to generate models or use existing
- `stubPackage` - out package for stubs
- `supportPackage` - package for support files
- `outputDir` - target output path
- `delegateObject` - object to wrap default logic of mocks creation
- `configOptions.explode` - explode mode on/off
- `configOptions.useTags` - if this parameter is true then Tags will be used for class names
- `configOptions.maxDepth` - max depth for explosion
- `configOptions.prefixMap` - map for query/body/response prefixes

See example of full configuration there: https://github.com/ValentinKarnaukhov/mockgen-example/blob/master/pom.xml

<H3>Result</H3>

After plugin execution in target folder will appear *Mock classes.
By default generated file contains 

* query section (default prefix "inQuery")
* body section (default prefix "inBody")
* response section (default prefix "inResp")
* code section (code*, e.g. code200(), code400()...)
* mock() method
* buildStub() method

<H4>Example in tests</H4>

```java
@Test
public void allQueryParamsMatchShouldReturn200() throws ApiException {
    QueryParamsGetMock.GetObjectByParams mock = new QueryParamsGetMock.GetObjectByParams(); //created code
    mock.inQueryStingParam("STRING")
            .inQueryIntegerParam(123)
            .inQueryLongParam(123L)
            .inQueryFloatParam(123.123f)
            .inQueryDoubleParam(123.123)
            .inQueryBoolParam(true)
            .inQueryEnumParam("EV1")
            .code200()
            .inResp("MATCHED")
            .mock();

    String response = getApi.getObjectByParams("STRING", 123, 123L, true,
            123.123f, 123.123, "EV1"); //client from swagger
    Assertions.assertEquals(200, getApi.getApiClient().getStatusCode());
    Assertions.assertEquals("MATCHED", response
}
```

<H3>Delegate object</H3>

By default mocks will be created in WireMock instance, but it can be changed via `<delegateObject>` section. 

Target file should contains java class with static public method `mock(StubMapping stub)`

```java
package com.github.valentinkarnaukhov.mockgenexample;

import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class CustomWiremock {

    public static void mock(StubMapping stub) {
        System.out.println("Hello: " + stub);
    }

}
```

To use this class during mock creation add this to configuration
```xml
<delegateObject>com.github.valentinkarnaukhov.mockgenexample.CustomWiremock</delegateObject>
```








