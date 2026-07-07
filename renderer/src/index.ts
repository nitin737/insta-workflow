import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { renderTemplate } from './renderer/renderTemplate';

// Get __dirname equivalent for ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

async function main() {
  const dataPath = path.join(__dirname, 'data', 'sample.json');
  const cssPathModern = path.join(__dirname, '..', 'dist', 'styles.css');
  const outputDir = path.join(__dirname, 'output');

  // Check if we are running in template generation mode
  const isTemplateMode = process.env.TEMPLATE_MODE === 'true';

  // Read data
  const dataRaw = fs.readFileSync(dataPath, 'utf-8');
  const data = JSON.parse(dataRaw);
  const slides = data.slides || [];

  // Create output directory if not exists
  if (!fs.existsSync(outputDir)) {
    fs.mkdirSync(outputDir, { recursive: true });
  }

  console.log(`Starting generation (Template Mode: ${isTemplateMode})`);

  // Iterate over slides and render
  for (let i = 0; i < slides.length; i++) {
    const slideData = slides[i];
    console.log(`Rendering slide ${i + 1}...`);
    
    try {
      const templateName = slideData.template || 'modern';
      const actualData = slideData.data || slideData;
      let cssPath = cssPathModern;
      if (templateName.startsWith('slide')) {
        cssPath = path.join(__dirname, 'templates', templateName, 'styles.css');
      }

      const finalHtml = await renderTemplate({
        templateName,
        data: actualData,
        cssPath,
        isTemplate: isTemplateMode,
      });

      const fileName = isTemplateMode ? `slide-${i + 1}.template.html` : `slide-${i + 1}.html`;
      const outputPath = path.join(outputDir, fileName);
      fs.writeFileSync(outputPath, finalHtml);
      console.log(`Saved ${fileName}`);
    } catch (error) {
      console.error(`Failed to render slide ${i + 1}:`, error);
    }
  }
  
  console.log('All slides rendered successfully!');
}

main();
