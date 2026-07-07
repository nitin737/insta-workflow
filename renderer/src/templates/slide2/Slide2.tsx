import React from 'react';
import SlideWrapper from '../slide1/components/SlideWrapper';
import Slide2PainCard from './components/Slide2PainCard';
import Slide2CureCard from './components/Slide2CureCard';

export interface Slide2Props {
  headline?: string;
  pain?: string;
  cure?: string;
  features?: string[];
  backgroundImage?: string;
  handle?: string;
  isTemplate?: boolean;
}

export default function Slide2({
  headline = "",
  pain = "",
  cure = "",
  features = [],
  backgroundImage = "../assets/other-slide-bg.jpg",
  handle = "@golang_verse",
  isTemplate = false,
}: Slide2Props) {
  return (
    <SlideWrapper
      slideNumber={2}
      backgroundImage={backgroundImage}
      badgeText="THE PROBLEM & THE SOLUTION"
      badgeClass="badge-go"
      handle={isTemplate ? "@[[${carousel.slide1.owner}]]" : handle}
      footerAction="Swipe →"
    >
      <div className="s2-glow-red" />
      <div className="s2-glow-cyan" />

      <div className="s2-content">
        <div className="slide2-header">
          <div className="accent-line"></div>
          <h2 className="slide2-title">
            {isTemplate ? "[[${slide.headline}]]" : headline}
          </h2>
        </div>

        <div className="slide2-content-split">
          <Slide2PainCard pain={isTemplate ? "[[${slide.pain}]]" : pain} />

          <div className="s2-divider">
            <div className="s2-divider-line"></div>
            <div className="s2-divider-badge">VS</div>
            <div className="s2-divider-line"></div>
          </div>

          {/* We pass isTemplate into Slide2CureCard to handle the features array inside it */}
          <Slide2CureCard cure={isTemplate ? "[[${slide.cure}]]" : cure} features={features} isTemplate={isTemplate} />
        </div>
      </div>
    </SlideWrapper>
  );
}
