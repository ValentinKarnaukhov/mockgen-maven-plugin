package com.github.valentinkarnaukhov;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.Map;

public class ConfigOptions {

    @Parameter(property = "explode")
    Boolean explode;

    @Parameter(property = "useTags")
    Boolean useTags;

    @Parameter(property = "prefixMap")
    Map<String, String> prefixMap;

    @Parameter(property = "maxDepth")
    String maxDepth;


}
