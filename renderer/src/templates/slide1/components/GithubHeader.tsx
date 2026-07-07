import React from 'react';

export interface GithubHeaderProps {
  headerTheme?: string;
  owner: string;
  repo: string;
  stars: string;
}

export default function GithubHeader({ headerTheme, owner, repo, stars }: GithubHeaderProps) {
  if (headerTheme === 'terminal') {
    return (
      <div className="github-pill-header theme-terminal">
        <div className="terminal-left-group">
          <div className="terminal-dots">
            <span className="terminal-dot dot-red"></span>
            <span className="terminal-dot dot-yellow"></span>
            <span className="terminal-dot dot-green"></span>
          </div>
          <div className="terminal-path">
            ~/{owner}/<span className="accent">{repo}</span>
          </div>
        </div>
        <div className="terminal-git-status">
          <svg viewBox="0 0 16 16" width="16" height="16" fill="currentColor" aria-hidden="true">
            <path fillRule="evenodd" d="M11.75 2.5a.75.75 0 01.75-.75h1a.75.75 0 01.75.75v1a.75.75 0 01-.75.75h-1a.75.75 0 01-.75-.75v-1zm-6 3a.75.75 0 01.75-.75h5a.75.75 0 01.75.75v5a.75.75 0 01-.75.75h-5a.75.75 0 01-.75-.75v-5z"></path>
          </svg>
          <span>main</span>
          <span style={{ opacity: 0.5 }}>|</span>
          <span>★ {stars}</span>
        </div>
      </div>
    );
  }

  const headerClass = `github-pill-header ${headerTheme !== 'classic' ? `theme-${headerTheme}` : ''}`;
  return (
    <div className={headerClass}>
      <div className="github-pill-left">
        <svg viewBox="0 0 16 16" version="1.1" width="22" height="22" fill="currentColor" aria-hidden="true">
          <path fillRule="evenodd" d="M2 2.5A2.5 2.5 0 014.5 0h8.75a.75.75 0 01.75.75v12.5a.75.75 0 01-.75.75h-2.5a.75.75 0 110-1.5h1.75v-2h-8a1 1 0 00-.714 1.7.75.75 0 01-1.072 1.05A2.495 2.495 0 012 11.5v-9zm10.5-1V9h-8c-.356 0-.694.074-1 .208V2.5a1 1 0 011-1h8zM5 12.25v3.25a.25.25 0 00.4.2l1.45-1.087a.25.25 0 01.3 0L8.6 15.7a.25.25 0 00.4-.2v-3.25a.25.25 0 00-.25-.25h-3.5a.25.25 0 00-.25.25z"></path>
        </svg>
        <span className="github-pill-repo-owner">{owner}</span>
        <span style={{ color: 'rgba(255,255,255,0.4)' }}>/</span>
        <span className="github-pill-repo-name">{repo}</span>
        <span className="github-pill-public-badge">Public</span>
      </div>
      <div className="github-pill-right">
        <button className="github-btn-star">
          <svg viewBox="0 0 16 16" version="1.1" width="20" height="20" fill="currentColor" aria-hidden="true">
            <path fillRule="evenodd" d="M8 .25a.75.75 0 01.673.418l1.882 3.815 4.21.612a.75.75 0 01.416 1.279l-3.046 2.97.719 4.192a.75.75 0 01-1.088.791L8 12.347l-3.766 1.98a.75.75 0 01-1.088-.79l.72-4.194L.818 6.374a.75.75 0 01.416-1.28l4.21-.611L7.327.668A.75.75 0 018 .25zm0 2.445L6.615 5.5a.75.75 0 01-.564.41l-3.097.45 2.24 2.184a.75.75 0 01.216.664l-.528 3.084 2.769-1.456a.75.75 0 01.698 0l2.77 1.456-.53-3.084a.75.75 0 01.216-.664l2.24-2.183-3.096-.45a.75.75 0 01-.564-.41L8 2.694z"></path>
          </svg>
          <span>Star</span>
          <span className="github-star-count">{stars}</span>
        </button>
        <button className="github-btn-arrow" aria-label="Toggle details">
          <svg viewBox="0 0 16 16" version="1.1" width="16" height="16" fill="currentColor" aria-hidden="true">
            <path d="M4.427 7.427l3.396 3.396a.25.25 0 00.354 0l3.396-3.396A.25.25 0 0011.396 7H4.604a.25.25 0 00-.177.427z"></path>
          </svg>
        </button>
      </div>
    </div>
  );
}
