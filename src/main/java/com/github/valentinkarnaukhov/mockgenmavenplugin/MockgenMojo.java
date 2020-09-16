package com.github.valentinkarnaukhov.mockgenmavenplugin;

import com.github.valentinkarnaukhov.stubgenerator.GeneratorExecutor;
import com.github.valentinkarnaukhov.stubgenerator.model.CodegenConfiguration;
import com.github.valentinkarnaukhov.stubgenerator.model.GeneratorProperties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "generate-stubs", defaultPhase = LifecyclePhase.INSTALL)
public class MockgenMojo extends AbstractMojo {

    @Parameter(property = "modelPackage", defaultValue = "com.github.valentinkarnaukhov.mockgen.model")
    String modelPackage;

    @Parameter(property = "stubPackage", defaultValue = "com.github.valentinkarnaukhov.mockgen.stub")
    String stubPackage;

    @Parameter(property = "supportPackage", defaultValue = "com.github.valentinkarnaukhov.mockgen.support")
    String supportPackage;

    @Parameter(property = "lang", defaultValue = "java", readonly = true)
    String lang;

    @Parameter(property = "inputSpec", required = true)
    String inputSpec;

    @Parameter(property = "outputDir", defaultValue = "target/generated-sources/mockgen")
    String outputDir;

    @Parameter(property = "generateModels", defaultValue = "true")
    Boolean generateModels;

    @Parameter(property = "delegateObject")
    String delegateObject;

    @Parameter(property = "configOptions")
    ConfigOptions configOptions = new ConfigOptions();

    public void execute() {
        GeneratorExecutor executor = new GeneratorExecutor();
        CodegenConfiguration configuration = new CodegenConfiguration();
        GeneratorProperties properties = configuration.getGeneratorProperties();

        configuration.setModelPackage(modelPackage);
        configuration.setStubPackage(stubPackage);
        configuration.setSupportPackage(supportPackage);
        configuration.setLang(lang);
        configuration.setInputSpec(inputSpec);
        configuration.setOutputDir(outputDir);
        configuration.setGenerateModels(generateModels);
        configuration.setDelegateObject(delegateObject);

        properties.setPrefixMap(configOptions.prefixMap);
        properties.setExplode(configOptions.explode);
        properties.setUseTags(configOptions.useTags);
        properties.setMaxDepth(configOptions.maxDepth);

        executor.generate(configuration);
    }


}
