package com.github.valentinkarnaukhov;

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

@Mojo(name = "generate-stubs", defaultPhase = LifecyclePhase.COMPILE)
public class MockgenMojo extends AbstractMojo {

    @Parameter(property = "modelPackage")
    String modelPackage;

    @Parameter(property = "lang", defaultValue = "java", readonly = true)
    String lang;

    @Parameter(property = "inputSpec", required = true)
    String inputSpec;

    @Parameter(property = "outputDir", defaultValue = "target/generated-sources/mockgen")
    String outputDir;

    @Parameter(property = "generateModels", defaultValue = "true")
    Boolean generateModels;

    @Parameter(property = "generateStub", defaultValue = "true", readonly = true)
    Boolean generateStub;

    @Parameter(property = "stubPackage")
    String stubPackage;

    @Parameter(property = "delegateObject")
    String delegateObject;

    @Parameter(property = "configOptions")
    ConfigOptions configOptions;

    public void execute() {
        CodegenConfigurator codegenConfigurator = new CodegenConfigurator();

        codegenConfigurator.setModelPackage(modelPackage);
        codegenConfigurator.setLang(lang);
        codegenConfigurator.setInputSpec(inputSpec);
        codegenConfigurator.setOutputDir(outputDir);
        codegenConfigurator.setTemplateDir("src/main/resources");

        ClientOptInput input = codegenConfigurator.toClientOptInput();

        OpenAPI openAPI = new OpenAPIV3Parser().read(codegenConfigurator.getInputSpec());
        InlineModelResolver resolver = new InlineModelResolver(true, true);
        resolver.flatten(openAPI);
        input.setOpenAPI(openAPI);

        WiremockGenerator wiremockGenerator = new WiremockGenerator();
        wiremockGenerator.setGeneratorPropertyDefault("explode", configOptions.explode.toString());
        wiremockGenerator.setGeneratorPropertyDefault("useTags", configOptions.useTags.toString());
        wiremockGenerator.setGeneratorPropertyDefault("maxDepth", configOptions.maxDepth);
        wiremockGenerator.setGeneratorPropertyDefault("generateModels", generateModels.toString());
        wiremockGenerator.setGeneratorPropertyDefault("generateStub", generateStub.toString());
        wiremockGenerator.setGeneratorPropertyDefault("stubPackage", stubPackage);
        wiremockGenerator.setGeneratorPropertyDefault("delegateObject", delegateObject);
        wiremockGenerator.setPrefixMap(configOptions.prefixMap);

        wiremockGenerator.opts(input);

        wiremockGenerator.generate();
    }
}
