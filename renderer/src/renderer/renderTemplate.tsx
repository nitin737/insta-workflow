import React from 'react';
import ReactDOMServer from 'react-dom/server';
import fs from 'fs';
import path from 'path';

// Import templates
import ModernSlide from '../templates/modern/Slide';
import Slide1 from '../templates/slide1/Slide1';
import Slide2 from '../templates/slide2/Slide2';
import Slide3 from '../templates/slide3/Slide3';
import Slide4 from '../templates/slide4/Slide4';
import Slide5 from '../templates/slide5/Slide5';

const templates: Record<string, React.FC<any>> = {
  modern: ModernSlide,
  slide1: Slide1,
  slide2: Slide2,
  slide3: Slide3,
  slide4: Slide4,
  slide5: Slide5,
};

type RenderTemplateOptions = {
  templateName: string;
  data: any;
  cssPath?: string;
  isTemplate?: boolean;
};

export async function renderTemplate({ templateName, data, cssPath, isTemplate = false }: RenderTemplateOptions) {
  const Component = templates[templateName];
  if (!Component) {
    throw new Error(`Template ${templateName} not found`);
  }

  // Render React component to HTML string
  const html = ReactDOMServer.renderToStaticMarkup(<Component {...data} isTemplate={isTemplate} />);
  
  let css = '';
  if (cssPath && fs.existsSync(cssPath)) {
    css = fs.readFileSync(cssPath, 'utf8');
  }

  return buildHtmlDocument(html, css);
}

function buildHtmlDocument(html: string, css: string) {
  return `<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Slide</title>
  <style>
    ${css}
  </style>
</head>
<body style="margin: 0; padding: 0;">
  ${html}
</body>
</html>`;
}
