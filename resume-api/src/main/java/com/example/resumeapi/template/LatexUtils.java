package com.example.resumeapi.template;

import org.springframework.stereotype.Component;

@Component("latexUtils")
public class LatexUtils {
    
    public String escape(String input) {
        if (input == null) {
            return "";
        }
        return input
            .replace("\\", "\\textbackslash{}")
            .replace("{", "\\{")
            .replace("}", "\\}")
            .replace("$", "\\$")
            .replace("&", "\\&")
            .replace("#", "\\#")
            .replace("^", "\\textasciicircum{}")
            .replace("_", "\\_")
            .replace("~", "\\textasciitilde{}")
            .replace("%", "\\%");
    }
}
