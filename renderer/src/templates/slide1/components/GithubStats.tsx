import React from 'react';

export interface GithubStatsProps {
  watchers: number;
  forks: number;
  latestRelease: string;
}

export default function GithubStats({ watchers, forks, latestRelease }: GithubStatsProps) {
  return (
    <div className="github-stats-grid">
      <div className="github-stat-card">
        <span className="stat-card-icon" aria-hidden="true">👁️</span>
        <span className="stat-card-text">{watchers} watching</span>
      </div>
      <div className="github-stat-card">
        <span className="stat-card-icon" aria-hidden="true">🍴</span>
        <span className="stat-card-text">{forks} forks</span>
      </div>
      <div className="github-stat-card highlight-release">
        <span className="stat-card-icon" aria-hidden="true">🏷️</span>
        <span className="stat-card-text">release: {latestRelease}</span>
      </div>
    </div>
  );
}
