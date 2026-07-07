import { defineConfig } from 'vite';

export default defineConfig({
  build: {
    outDir: 'dist',
    rollupOptions: {
      input: 'src/templates/modern/styles.css',
      output: {
        assetFileNames: 'styles.[ext]'
      }
    }
  }
});
