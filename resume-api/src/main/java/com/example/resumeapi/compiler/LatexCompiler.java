package com.example.resumeapi.compiler;

import com.example.resumeapi.exception.LatexCompilationException;

public interface LatexCompiler {
    byte[] compile(String latexSource) throws LatexCompilationException;
}
