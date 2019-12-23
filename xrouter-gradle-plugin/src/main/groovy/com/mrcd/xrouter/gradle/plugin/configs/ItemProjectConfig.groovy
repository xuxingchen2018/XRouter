package com.mrcd.xrouter.gradle.plugin.configs;

@Deprecated
class ItemProjectConfig {

    String name

    boolean useAnnotation = true

    boolean useCompiler = true

    boolean useLibrary = true

    ItemProjectConfig(String name) {
        this.name = name
    }

    boolean isUseAnnotation() {
        return useAnnotation
    }

    void setUseAnnotation(boolean useAnnotation) {
        this.useAnnotation = useAnnotation;
    }

    boolean isUseCompiler() {
        return useCompiler
    }

    void setUseCompiler(boolean useCompiler) {
        this.useCompiler = useCompiler
    }

    boolean isUseLibrary() {
        return useLibrary
    }

    void setUseLibrary(boolean useLibrary) {
        this.useLibrary = useLibrary
    }


    @Override
    public String toString() {
        return "ItemProjectConfig{" +
                "name='" + name + '\'' +
                ", useAnnotation=" + useAnnotation +
                ", useCompiler=" + useCompiler +
                ", useLibrary=" + useLibrary +
                '}';
    }
}
