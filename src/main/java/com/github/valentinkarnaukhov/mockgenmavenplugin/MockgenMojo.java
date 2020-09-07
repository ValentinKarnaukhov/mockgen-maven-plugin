package com.github.valentinkarnaukhov.mockgenmavenplugin;

import com.github.valentinkarnaukhov.stubgenerator.WiremockGenerator;
import io.swagger.codegen.v3.ClientOptInput;
import io.swagger.codegen.v3.config.CodegenConfigurator;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.util.InlineModelResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.Objects;

@Mojo(name = "generate-stubs", defaultPhase = LifecyclePhase.INSTALL)
public class MockgenMojo extends AbstractMojo {

    @Parameter(property = "modelPackage", defaultValue = "com.github.valentinkarnaukhov.mockgen.model")
    String modelPackage;

    @Parameter(property = "lang", defaultValue = "java", readonly = true)
    String lang;

    @Parameter(property = "inputSpec", required = true)
    String inputSpec;

    @Parameter(property = "outputDir", defaultValue = "target/generated-sources/mockgen")
    String outputDir;

    @Parameter(property = "generateModels", defaultValue = "true")
    Boolean generateModels;

    @Parameter(property = "stubPackage")
    String stubPackage;

    @Parameter(property = "delegateObject")
    String delegateObject;

    @Parameter(property = "configOptions")
    ConfigOptions configOptions = new ConfigOptions();

    public void execute() {
        CodegenConfigurator codegenConfigurator = new CodegenConfigurator();

        codegenConfigurator.setModelPackage(modelPackage);
        codegenConfigurator.setLang(lang);
        codegenConfigurator.setInputSpec(inputSpec);
        codegenConfigurator.setOutputDir(outputDir);

        ClientOptInput input = codegenConfigurator.toClientOptInput();

        OpenAPI openAPI = new OpenAPIV3Parser().read(codegenConfigurator.getInputSpec());
        InlineModelResolver resolver = new InlineModelResolver(true, true);
        resolver.flatten(openAPI);
        input.setOpenAPI(openAPI);

        WiremockGenerator wiremockGenerator = new WiremockGenerator();
        wiremockGenerator.setGeneratorPropertyDefault("explode", nullOrToString(configOptions.explode));
        wiremockGenerator.setGeneratorPropertyDefault("useTags", nullOrToString(configOptions.useTags));
        wiremockGenerator.setGeneratorPropertyDefault("maxDepth", configOptions.maxDepth);
        wiremockGenerator.setGeneratorPropertyDefault("generateModels", nullOrToString(generateModels));
        wiremockGenerator.setGeneratorPropertyDefault("generateStub", "true");
        wiremockGenerator.setGeneratorPropertyDefault("stubPackage", stubPackage);
        wiremockGenerator.setGeneratorPropertyDefault("delegateObject", delegateObject);
        wiremockGenerator.setPrefixMap(configOptions.prefixMap);

        wiremockGenerator.opts(input);

        wiremockGenerator.generate();
    }

    private String nullOrToString(Object object) {
        return Objects.nonNull(object) ? object.toString() : null;
    }
}
