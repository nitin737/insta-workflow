import React from 'react';
import SlideWrapper from '../slide1/components/SlideWrapper';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

export interface Slide3Props {
  headline?: string;
  beforeCode?: string;
  afterCode?: string;
  takeaway?: string;
  backgroundImage?: string;
  handle?: string;
}

export default function Slide3({
  headline = "",
  beforeCode = "",
  afterCode = "",
  takeaway = "",
  backgroundImage = "../assets/other-slide-bg.jpg",
  handle = "@golang_verse"
}: Slide3Props) {
  return (
    <SlideWrapper
      slideNumber={3}
      backgroundImage={backgroundImage}
      badgeText="SIMPLICITY"
      badgeClass="badge-go"
      handle={handle}
      footerAction="Swipe →"
    >
      <div className="s3-content" style={{ display: 'flex', flexDirection: 'column', gap: '24px', flex: 1, justifyContent: 'center' }}>
        <div className="s3-heading-row">
          <h2 className="s3-heading-title">{headline}</h2>
        </div>

        {beforeCode && (
          <div className="code-block" style={{ opacity: 0.8, border: '1px dashed #ef4444' }}>
            <div className="code-block-header">
              <div className="code-tab" style={{ color: '#ef4444' }}>BEFORE (Manual)</div>
            </div>
            <SyntaxHighlighter language="go" style={vscDarkPlus} customStyle={{ margin: 0, background: 'transparent', padding: '16px', fontSize: '0.9rem' }}>
              {beforeCode}
            </SyntaxHighlighter>
          </div>
        )}

        {afterCode && (
          <div className="code-block" style={{ border: '1px solid #00ADD8', boxShadow: '0 0 20px rgba(0, 173, 216, 0.2)' }}>
            <div className="code-block-header">
              <div className="code-tab" style={{ color: '#00ADD8' }}>AFTER (With Library)</div>
            </div>
            <SyntaxHighlighter language="go" style={vscDarkPlus} customStyle={{ margin: 0, background: 'transparent', padding: '16px', fontSize: '0.9rem' }}>
              {afterCode}
            </SyntaxHighlighter>
          </div>
        )}

        {takeaway && (
          <div className="s3-dev-experience" style={{ marginTop: '16px', background: 'rgba(255,255,255,0.05)', padding: '20px', borderRadius: '12px' }}>
            <span className="s3-dev-exp-icon">💡</span>
            <span className="s3-dev-exp-text" style={{ fontSize: '1.2rem', lineHeight: '1.5' }}>{takeaway}</span>
          </div>
        )}
      </div>
    </SlideWrapper>
  );
}
