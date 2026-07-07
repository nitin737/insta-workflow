import React from 'react';
import SlideWrapper from './components/SlideWrapper';
import GithubHeader from './components/GithubHeader';
import GithubHero from './components/GithubHero';
import GithubStats from './components/GithubStats';
import GithubLanguageBar from './components/GithubLanguageBar';
import GithubContributors from './components/GithubContributors';

export interface Slide1Props {
  owner?: string;
  repo?: string;
  stars?: string;
  bigTitle?: string;
  highlightedText?: string;
  description?: string;
  about?: string;
  tags?: string[];
  license?: string;
  activity?: string;
  watchers?: number | string;
  forks?: number | string;
  latestRelease?: string;
  contributorsCount?: number | string;
  langGoPct?: number | string;
  langOtherPct?: number | string;
  backgroundImage?: string;
  headerTheme?: string;
  handle?: string;
  isTemplate?: boolean;
}

export default function Slide1({
  owner = "owner",
  repo = "repo",
  stars = "0.0k",
  bigTitle = "Repository",
  highlightedText: highlight = "Go (golang)",
  description = "",
  about = "",
  tags = [],
  license = "MIT",
  activity = "Activity",
  watchers = 0,
  forks = 0,
  latestRelease = "Latest",
  contributorsCount = 7,
  langGoPct = 90.0,
  langOtherPct = 10.0,
  backgroundImage = "src/assets/slide1-bg.jpg",
  headerTheme,
  handle = "@golang_verse",
  isTemplate = false,
}: Slide1Props) {
  // Convert relative path to absolute or correctly resolve for renderer
  // Note: For static HTML rendering in Puppeteer, absolute paths or base64 are safest.
  // We'll use the path as provided, assuming it resolves properly from the working directory 
  // where the HTML is served/opened.

  return (
    <SlideWrapper
      slideNumber={1}
      backgroundImage={backgroundImage}
      showHeader={false}
      handle={isTemplate ? "@[[${carousel.slide1.owner}]]" : handle}
      footerAction="Swipe →"
    >
      <div className="glow-accent-1"></div>

      <GithubHeader 
        headerTheme={headerTheme} 
        owner={isTemplate ? "[[${slide.owner}]]" : owner} 
        repo={isTemplate ? "[[${slide.repo}]]" : repo} 
        stars={isTemplate ? "[[${slide.stars}]]" : stars} 
      />

      <div className="github-hero-center">
        <GithubHero 
          bigTitle={isTemplate ? "[[${slide.bigTitle}]]" : bigTitle} 
          description={isTemplate ? "[[${slide.description}]]" : description} 
          highlight={isTemplate ? "[[${slide.highlightedText}]]" : highlight} 
        />

        <GithubStats 
          watchers={isTemplate ? "[[${slide.watchers}]]" : watchers as any} 
          forks={isTemplate ? "[[${slide.forks}]]" : forks as any} 
          latestRelease={isTemplate ? "[[${slide.latestRelease}]]" : latestRelease} 
        />
      </div>

      <GithubLanguageBar 
        langGoPct={isTemplate ? "[[${slide.langGoPct}]]" : langGoPct as any} 
        langOtherPct={isTemplate ? "[[${slide.langOtherPct}]]" : langOtherPct as any} 
        isTemplate={isTemplate}
      />
      
      <GithubContributors 
        contributorsCount={isTemplate ? "[[${slide.contributorsCount}]]" : contributorsCount as any} 
      />
    </SlideWrapper>
  );
}
