import React from 'react';
import SlideWrapper from '../slide1/components/SlideWrapper';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { TemplateArray } from '../../components/TemplateArray';

export interface Slide5Props {
  headline?: string;
  quickstart?: string;
  minimalSetup?: string;
  resources?: any[];
  ctas?: any[];
  backgroundImage?: string;
  handle?: string;
  isTemplate?: boolean;
}

export default function Slide5({
  headline = "Add This to Your Go.mod Today.",
  quickstart = "",
  minimalSetup = "",
  resources = [],
  ctas = [],
  backgroundImage = "../assets/other-slide-bg.jpg",
  handle = "@golang_verse",
  isTemplate = false,
}: Slide5Props) {
  return (
    <SlideWrapper
      slideNumber={5}
      backgroundImage={backgroundImage}
      badgeText="GET STARTED"
      badgeClass="badge-ai"
      handle={isTemplate ? "@[[${carousel.slide1.owner}]]" : handle}
      footerAction="Save Post 🔖"
    >
      <div className="glow-accent-1"></div>
      <div className="glow-accent-2"></div>

      <div className="slide-content" style={{ gap: '20px', justifyContent: 'center' }}>
        <h2 className="slide-title-h1" style={{ fontSize: '3.2rem', textAlign: 'center', marginBottom: '10px' }}>
          {isTemplate ? "[[${slide.headline}]]" : headline}
        </h2>

        {/* Quickstart */}
        {(isTemplate || quickstart) && (
          <div className="code-block" style={{ background: '#0d1117', border: '1px solid #30363d', padding: '16px', borderRadius: '8px' }}>
            <code style={{ color: '#58a6ff', fontFamily: 'var(--font-mono)' }}>$ {isTemplate ? "[[${slide.quickstart}]]" : quickstart}</code>
          </div>
        )}

        {/* Minimal Setup Code Block */}
        {(isTemplate || minimalSetup) && (
          <div className="code-block" style={{ marginTop: '10px' }}>
            <div className="code-block-header">
              <div className="code-tab">main.go</div>
            </div>
            <SyntaxHighlighter language="go" style={vscDarkPlus} customStyle={{ margin: 0, background: 'transparent', padding: '16px', fontSize: '0.9rem' }}>
              {isTemplate ? "[[${slide.minimalSetup}]]" : minimalSetup}
            </SyntaxHighlighter>
          </div>
        )}

        {/* Resources */}
        <div style={{ display: 'flex', gap: '24px', marginTop: '20px' }}>
          {(isTemplate || resources.length > 0) && (
            <div className="s5-resources" style={{ flex: 1, padding: '20px', background: 'rgba(255,255,255,0.03)', borderRadius: '12px' }}>
              <div style={{ marginBottom: '12px', color: '#cbd5e1', fontWeight: 600, fontSize: '1.2rem', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Resources</div>
              <TemplateArray name="resources" items={resources} isTemplate={isTemplate}>
                {(res, i) => (
                  <div key={i} className="s5-resource-item" style={{ marginBottom: '8px' }}>
                    <span className="s5-resource-icon">📖</span>
                    <span className="s5-resource-label" style={{ color: '#f8fafc', fontWeight: 500, marginRight: '8px' }}>
                      {isTemplate ? "[[${resourcesItem.label}]]" : res.label}:
                    </span>
                    <span className="s5-resource-url" style={{ color: '#00ADD8' }}>
                      {isTemplate ? "[[${resourcesItem.url}]]" : res.url}
                    </span>
                  </div>
                )}
              </TemplateArray>
            </div>
          )}
        </div>
      </div>
    </SlideWrapper>
  );
}
