import React from 'react';

export interface GithubLanguageBarProps {
  langGoPct: number;
  langOtherPct: number;
}

export default function GithubLanguageBar({ langGoPct, langOtherPct }: GithubLanguageBarProps) {
  return (
    <div className="github-language-horizontal-bar">
      <div className="language-bar-track">
        <div className="language-bar-fill go" style={{ width: `${langGoPct}%` }}></div>
        <div className="language-bar-fill other" style={{ width: `${langOtherPct}%` }}></div>
      </div>
      <div className="language-bar-labels">
        <span className="label-go">
          <span className="indicator go" aria-hidden="true"></span>
          Go {langGoPct}%
        </span>
        <span className="label-other">
          <span className="indicator other" aria-hidden="true"></span>
          Other {langOtherPct}%
        </span>
      </div>
    </div>
  );
}
