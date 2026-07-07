import React from 'react';
import SlideWrapper from '../slide1/components/SlideWrapper';
import { TemplateArray } from '../../components/TemplateArray';

export interface Slide4Props {
  headline?: string;
  points?: any[];
  backgroundImage?: string;
  handle?: string;
  isTemplate?: boolean;
}

export default function Slide4({
  headline = "",
  points = [],
  backgroundImage = "../assets/other-slide-bg.jpg",
  handle = "@golang_verse",
  isTemplate = false,
}: Slide4Props) {
  return (
    <SlideWrapper
      slideNumber={4} 
      backgroundImage={backgroundImage}
      badgeText="INTEGRATION"
      badgeClass="badge-go"
      handle={isTemplate ? "@[[${carousel.slide1.owner}]]" : handle}
      footerAction="Swipe →"
    >
      <div className="slide-content" style={{ display: 'flex', flexDirection: 'column', gap: '32px', justifyContent: 'center', flex: 1 }}>
        <h2 className="slide-subtitle" style={{ fontSize: '2.8rem', color: '#f8fafc', marginBottom: '10px' }}>
          {isTemplate ? "[[${slide.headline}]]" : headline}
        </h2>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr', gap: '20px' }}>
          <TemplateArray name="points" items={points} isTemplate={isTemplate}>
            {(pt, i) => (
              <div key={i} style={{ background: 'rgba(30, 41, 59, 0.5)', border: '1px solid rgba(0, 173, 216, 0.2)', borderRadius: '12px', padding: '24px', display: 'flex', flexDirection: 'column', gap: '8px', backdropFilter: 'blur(10px)' }}>
                <div style={{ color: '#00ADD8', fontSize: '1.4rem', fontWeight: 700, fontFamily: 'var(--font-mono)' }}>
                  {isTemplate ? "[[${pointsItem.title}]]" : pt.title}
                </div>
                <div style={{ color: '#cbd5e1', fontSize: '1.2rem', lineHeight: '1.5' }}>
                  {isTemplate ? "[[${pointsItem.desc}]]" : pt.desc}
                </div>
              </div>
            )}
          </TemplateArray>
        </div>
      </div>
    </SlideWrapper>
  );
}
