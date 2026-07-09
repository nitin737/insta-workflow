---
name: awesome-cv
description: Generates or edits a Resume/CV using the Awesome-CV LaTeX template (posquit0/Awesome-CV). Use this skill when dealing with LaTeX resume structures, specifically Awesome-CV.
---

# Awesome-CV Skill

Awesome-CV is a popular LaTeX template for resumes and cover letters.
When writing or maintaining a resume using Awesome-CV, follow these guidelines and structures.

## 1. Document Structure

The main file (e.g., `resume.tex`) should be set up as follows:
```latex
%!TEX TS-program = xelatex
%!TEX encoding = UTF-8 Unicode

\documentclass[11pt, a4paper]{awesome-cv}
\geometry{left=1.4cm, top=.8cm, right=1.4cm, bottom=1.8cm, footskip=.5cm}

% Highlight color
\colorlet{awesome}{awesome-red}
% OR custom color: \definecolor{awesome}{HTML}{CA63A8}
\setbool{acvSectionColorHighlight}{true}

\renewcommand{\acvHeaderSocialSep}{\quad\textbar\quad}
```

## 2. Personal Information

Define personal information before `\begin{document}`.
Use the following available macros:
```latex
\name{First}{Last}
\position{Job Title{\enskip\cdotp\enskip}Subtitle}
\address{Location}
\mobile{(+1) 123-456-7890}
\email{email@example.com}
\homepage{www.example.com}
\github{github-id}
\linkedin{linkedin-id}
% Other options: \gitlab, \stackoverflow, \twitter, \skype, \reddit, \medium, \kaggle, \hackerrank
\quote{``A quote if needed."}
```

Start the document body and render header/footer:
```latex
\begin{document}
\makecvheader[C] % Align: C(Center), L(Left), R(Right)
\makecvfooter{\today}{First Last~~~·~~~Résumé}{\thepage}
```

## 3. Sections and Content

Usually, each major section is abstracted into its own `.tex` file (e.g., `\input{resume/experience.tex}`).
Inside those files or directly in the document, use:

### Paragraphs (Summary)
```latex
\cvsection{Summary}
\begin{cvparagraph}
Your summary text goes here.
\end{cvparagraph}
```

### Entries (Work Experience, Education, Projects)
Use `cventries` environment with `\cventry`:
```latex
\cvsection{Work Experience}
\begin{cventries}
  \cventry
    {Job Title} % Position
    {Company Name} % Organization
    {Location} % Location
    {Date - Date} % Date
    {
      \begin{cvitems}
        \item {Description point 1.}
        \item {Description point 2.}
      \end{cvitems}
    }
\end{cventries}
```

### Skills
Use `cvskills` environment with `\cvskill`:
```latex
\cvsection{Skills}
\begin{cvskills}
  \cvskill
    {Category} % e.g., Frameworks
    {React, Node.js, Spring Boot} % Skills list
  \cvskill
    {Languages}
    {Java, TypeScript, Python}
\end{cvskills}
```

### Honors & Awards
Use `cvhonors` environment with `\cvhonor`:
```latex
\cvsection{Honors \& Awards}
\begin{cvhonors}
  \cvhonor
    {Award Title} % Award
    {Event/Organization} % Event
    {Location} % Location
    {Year} % Date
\end{cvhonors}
```

## 4. Compilation
Always compile Awesome-CV using `xelatex` or `lualatex` since it relies on `fontspec` and Unicode fonts.
```bash
xelatex resume.tex
```

## 5. Best Practices
- Keep structural consistency. Use `\cvsection{}` for main headers.
- Modularize content by using `\input{...}` for different sections.
- Avoid using `\textbf` or similar formatting inside `\cvitems` unless explicitly asked; Awesome-CV styles text appropriately by default.
- Watch out for unescaped characters in LaTeX like `&` (use `\&`), `%` (use `\%`), and `$` (use `\$`).
