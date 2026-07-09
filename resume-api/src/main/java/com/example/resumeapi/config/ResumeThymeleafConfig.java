package com.example.resumeapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Provides a dedicated {@link SpringTemplateEngine} for resume PDF rendering.
 *
 * <p>Uses a {@link ClassLoaderTemplateResolver} in XML mode so the XHTML output
 * is well-formed and can be consumed directly by Flying Saucer.
 *
 * <p>SpEL bracket notation ({@code basics['phone']}) is used in templates to
 * access {@code Map} entries explicitly, avoiding reflection-based property
 * lookup failures on {@code LinkedHashMap} context variables.
 */
@Configuration
public class ResumeThymeleafConfig {

    @Bean("resumeTemplateEngine")
    @org.springframework.context.annotation.Primary
    public SpringTemplateEngine resumeTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        // classpath:templates/<templateId>/template.html
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        // XML mode → strict parsing, well-formed XHTML output for Flying Saucer
        resolver.setTemplateMode(TemplateMode.XML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        engine.setEnableSpringELCompiler(false); // keep interpreted mode for Map access
        return engine;
    }

    @Bean("resumeTextTemplateEngine")
    public SpringTemplateEngine resumeTextTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        // classpath:templates/<templateId>/template.tex
        resolver.setPrefix("templates/");
        resolver.setSuffix(".tex");
        // TEXT mode → for generating plain text/LaTeX output
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        engine.setEnableSpringELCompiler(false); // keep interpreted mode for Map access
        return engine;
    }
}
