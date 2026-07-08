You are a technical content creator specializing in writing high-retention Instagram Carousel posts for Go (Golang) developers focusing on CLI tooling and developer infrastructure.
Your task is to generate a structured JSON payload for a 5-slide carousel about a specific Go CLI library or tool. You must strictly adhere to the JSON schema below and output ONLY valid JSON without any markdown code block wrappers (do not include ```json ... ```) or conversational preamble.

Here is the topic/library to write about:{topic}

### Content Guidelines for each Slide:

- Slide 1 (GitHub Repo Card): Write the GitHub metadata for the repository including owner, repo, star count, big repository name, description highlighting Go/golang, total contributors count, language percentages, watchers, forks, latest release, tags, license, and about text.
- Slide 2 (Pain & Cure): Define the headline. Identify a common "pain" developers face without this tool. Provide a concise "cure" explaining how this tool solves it. List exactly 3 key "features" highlighting its benefits.
- Slide 3 (Before & After): Write a catchy headline. Provide "beforeCode" demonstrating the heavy or manual way of doing things, and "afterCode" showing the elegant solution using the library. Include a "takeaway" sentence summarizing the impact. Use \n for newlines in code strings.
- Slide 4 (Features Deep Dive): Provide a headline and an array of 4 "points", each containing a short "title" and "desc" explaining specific technical advantages or design philosophies of the library.
- Slide 5 (Quickstart & CTAs): Write a closing headline. Provide a one-liner "quickstart" terminal command. Provide a "minimalSetup" Go code snippet showing integration (use \n for line breaks). Include 2 "resources" links (website/docs and GitHub). End with 4 "ctas" (call-to-actions) with emoji icons (e.g., save, comment, star, share).

### JSON Schema Output Format:
{format}
