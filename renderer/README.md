# Carousel HTML Renderer

This is a standalone tool designed to convert React components and JSON data into pure, self-contained HTML files with Tailwind CSS compiled inline. The output HTML files are meant to be consumed by Puppeteer for screenshotting or PDF generation.

## How it works

1. **React Components** (`src/templates/modern/Slide.tsx`) define the structure and styling using Tailwind CSS classes.
2. **JSON Data** (`src/data/sample.json`) provides the dynamic content to be injected into the templates.
3. **Tailwind CLI** compiles the CSS from the templates.
4. **React DOM Server** (`src/renderer/renderTemplate.tsx`) renders the React components to static markup and injects the compiled CSS into a `<style>` tag, producing a single portable HTML string.
5. The generated HTML files are saved into `src/output/`.

## Prerequisites

Ensure you have Node.js installed (v18+ recommended).

## Setup & Compilation

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Generate HTML files**:
   ```bash
   npm run generate
   ```
   This command will:
   - Build the Tailwind CSS file.
   - Run the renderer script (`src/index.ts`).
   - Output the final HTML slides into the `src/output/` directory.

## Project Structure

```
renderer/
├── package.json
├── tailwind.config.js       # Tailwind configuration
├── tsconfig.json            # TypeScript configuration
├── src/
│   ├── index.ts             # Main execution script
│   ├── data/
│   │   └── sample.json      # Dynamic data to inject into templates
│   ├── output/              # Generated HTML files go here (ignored in git)
│   ├── renderer/
│   │   └── renderTemplate.tsx # Logic to convert React to HTML string + inject CSS
│   └── templates/
│       └── modern/
│           ├── Slide.tsx    # React component using Tailwind classes
│           └── styles.css   # Main CSS entry with @tailwind directives
```

## Creating New Templates

To add a new template:
1. Create a new folder under `src/templates/` (e.g., `minimal/`).
2. Add your React components there using Tailwind classes.
3. Import and register your new template in `src/renderer/renderTemplate.tsx` under the `templates` object.
4. Update `src/index.ts` to pass the correct `templateName` when calling `renderTemplate`.
