package com.example.resumeapi.compiler;

import com.example.resumeapi.exception.LatexCompilationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.Comparator;
import java.util.stream.Stream;

@Component
public class LatexCompilerImpl implements LatexCompiler {

    @Value("${resume.latex.command:pdflatex}")
    private String latexCommand;

    @Value("${resume.latex.timeout:15s}")
    private Duration timeout;

    @Override
    public byte[] compile(String latexSource) {
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("resume-");
            Path texFile = tempDir.resolve("resume.tex");
            Files.writeString(texFile, latexSource); System.out.println("GEN_LATEX:\n" + latexSource);

            ProcessBuilder pb = new ProcessBuilder(
                latexCommand,
                "-interaction=nonstopmode",
                "-halt-on-error",
                "-no-shell-escape",
                "-output-directory", tempDir.toString(),
                texFile.toString()
            );
            pb.directory(tempDir.toFile());
            pb.environment().put("openout_any", "p");  // restrict output
            pb.environment().put("TEXMFOUTPUT", tempDir.toString());

            Process process = pb.start();
            boolean finished = process.waitFor(timeout.toSeconds(), TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                throw new LatexCompilationException("Compilation timed out");
            }
            if (process.exitValue() != 0) {
                Path logFile = tempDir.resolve("resume.log");
                String log = Files.exists(logFile) ? Files.readString(logFile) : "No log found";
                String error = extractError(log);
                throw new LatexCompilationException("LaTeX error: " + error);
            }

            Path pdf = tempDir.resolve("resume.pdf");
            return Files.readAllBytes(pdf);
        } catch (IOException | InterruptedException e) {
            throw new LatexCompilationException("Compilation failed: " + e.getMessage(), e);
        } finally {
            if (tempDir != null) {
                try {
                    try (Stream<Path> pathStream = Files.walk(tempDir)) {
                        pathStream.sorted(Comparator.reverseOrder())
                                .map(Path::toFile)
                                .forEach(java.io.File::delete);
                    }
                } catch (IOException ignored) {}
            }
        }
    }

    private String extractError(String log) {
        return log.lines().filter(l -> l.startsWith("!")).findFirst().orElse("Unknown error");
    }
}
