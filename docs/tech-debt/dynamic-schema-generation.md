# Dynamic Schema Generation & Handling

Currently, the `CarouselData` class in the insta-api is a strongly-typed Java Record that perfectly maps to a specific JSON schema prompt (e.g., `gemini-carousel-prompt-1.md`). 

If the JSON schema prompt changes or if multiple schemas are introduced (e.g., 3-slide quote, 5-slide tutorial, 8-slide deep dive), maintaining a static Java class becomes cumbersome. 

This document outlines two potential technical approaches to address this tech debt in the future.

## Approach 1: Fully Dynamic at Runtime (Recommended)

Drop the strict `CarouselData` Java record entirely and parse the AI's response into a generic JSON tree or Map.

### Implementation Steps
1. **insta-api Changes:** Instead of using Spring AI's `BeanOutputConverter<CarouselData>`, use Jackson to parse the result into a `JsonNode` or a `Map<String, Object>`.
   ```java
   ObjectMapper mapper = new ObjectMapper();
   JsonNode carouselData = mapper.readTree(response); 
   // or Map<String, Object>
   ```
2. **Template Changes:** Update the rendering engine (e.g., Thymeleaf) to render dynamic Maps or JSON nodes. Instead of calling specific getters, access keys dynamically:
   ```html
   <!-- Example Thymeleaf usage -->
   <h1 th:text="${carousel.get('slide1').get('headline').asText()}"></h1>
   ```

**Pros:** 
- Highly flexible. You never have to touch the Java code when you update the prompt or schema.
- Easily supports entirely different carousel types dynamically.

**Cons:** 
- You lose auto-complete and strict type safety in Java for the slide data.
- The HTML templates must precisely match the payload keys without compiler warnings.

---

## Approach 2: Build-Time Code Generation

Strictly keep the strongly-typed `CarouselData.java` class but automate its creation before the Java compiler runs, deriving it directly from the Markdown prompt.

### Implementation Steps
1. Write a custom Gradle task (e.g., using a Node.js script, Python, or a Gradle plugin).
2. Before `compileJava` runs, this task reads the target `gemini-carousel-prompt-x.md`.
3. The script extracts the JSON schema block using Regex.
4. The script uses a library like `jsonschema2pojo` (or simple string templating) to automatically generate the `CarouselData.java` source file in the `build/generated/sources` folder.

**Pros:** 
- Keeps everything strictly typed and validates perfectly within Java.
- Catches schema mismatches at compile time if downstream code relies on missing fields.

**Cons:** 
- The generation script will be brittle and will break if the prompt's JSON example isn't formatted exactly as expected.
- Harder to maintain over time compared to a simple runtime JSON map.
