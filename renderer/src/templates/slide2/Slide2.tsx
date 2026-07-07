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
}

export default function Slide2({
  headline = "",
  pain = "",
  cure = "",
  features = [],
  backgroundImage = "../assets/other-slide-bg.jpg",
  handle = "@golang_verse"
}: Slide2Props) {
  return (
    <SlideWrapper
      slideNumber={2}
      backgroundImage={backgroundImage}
      badgeText="THE PROBLEM & THE SOLUTION"
      badgeClass="badge-go"
      handle={handle}
      footerAction="Swipe →"
    >
      <div className="s2-glow-red" />
      <div className="s2-glow-cyan" />

      <div className="s2-content">
        <div className="s2-heading-row">
          <h2 className="s2-heading-title">{headline}</h2>
        </div>

        <div className="s2-compare-stack">
          <Slide2PainCard pain={pain} />

          <div className="s2-divider">
            <div className="s2-divider-line"></div>
            <div className="s2-divider-badge">VS</div>
            <div className="s2-divider-line"></div>
          </div>

          <Slide2CureCard cure={cure} features={features} />
        </div>
      </div>
    </SlideWrapper>
  );
}
