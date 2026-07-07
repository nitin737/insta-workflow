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
      handle={isTemplate ? "{{handle}}" : handle}
      footerAction="Swipe →"
    >
      <div className="glow-accent-1"></div>

      <GithubHeader 
        headerTheme={headerTheme} 
        owner={isTemplate ? "{{owner}}" : owner} 
        repo={isTemplate ? "{{repo}}" : repo} 
        stars={isTemplate ? "{{stars}}" : stars} 
      />

      <div className="github-hero-center">
        <GithubHero 
          bigTitle={isTemplate ? "{{bigTitle}}" : bigTitle} 
          description={isTemplate ? "{{description}}" : description} 
          highlight={isTemplate ? "{{highlightedText}}" : highlight} 
        />

        <GithubStats 
          watchers={isTemplate ? "{{watchers}}" : watchers as any} 
          forks={isTemplate ? "{{forks}}" : forks as any} 
          latestRelease={isTemplate ? "{{latestRelease}}" : latestRelease} 
        />
      </div>

      <GithubLanguageBar 
        langGoPct={isTemplate ? "{{langGoPct}}" : langGoPct as any} 
        langOtherPct={isTemplate ? "{{langOtherPct}}" : langOtherPct as any} 
      />
      
      <GithubContributors 
        contributorsCount={isTemplate ? "{{contributorsCount}}" : contributorsCount as any} 
      />
    </SlideWrapper>
  );
}
