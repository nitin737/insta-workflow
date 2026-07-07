import React from 'react';

export interface GithubContributorsProps {
  contributorsCount: number;
}

export default function GithubContributors({ contributorsCount }: GithubContributorsProps) {
  const renderAvatars = () => {
    const avatarUrls = [
      "https://avatars.githubusercontent.com/u/1?v=4",     // mojombo
      "https://avatars.githubusercontent.com/u/583231?v=4", // octocat
      "https://avatars.githubusercontent.com/u/2?v=4",      // defunkt
      "https://avatars.githubusercontent.com/u/3?v=4",      // pjhyett
    ];

    const showCount = Math.min(4, contributorsCount);
    const avatars = [];
    for (let i = 0; i < showCount; i++) {
      avatars.push(
        <img
          key={i}
          className="github-avatar"
          src={avatarUrls[i % avatarUrls.length]}
          alt={`Contributor ${i + 1}`}
        />
      );
    }

    if (contributorsCount > 4) {
      avatars.push(
        <div key="more" className="github-contributors-more">
          +{contributorsCount - 4}
        </div>
      );
    }

    return avatars;
  };

  return (
    <div className="github-contributors-container">
      <div className="contributors-header">
        <div className="github-contributors-label">
          <span>Contributors</span>
          <span className="github-contributors-badge">{contributorsCount}</span>
        </div>
      </div>
      <div className="github-avatar-row">
        {renderAvatars()}
      </div>
    </div>
  );
}
