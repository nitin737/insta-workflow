package com.instaworkflow.resumeapi.compiler;

import com.instaworkflow.resumeapi.exception.LatexCompilationException;

public interface LatexCompiler {
    byte[] compile(String latexSource) throws LatexCompilationException;
}
