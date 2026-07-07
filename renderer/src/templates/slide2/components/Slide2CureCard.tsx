import React from 'react';

export interface Slide2CureCardProps {
  cure: string;
  features: string[];
}

export default function Slide2CureCard({ cure, features }: Slide2CureCardProps) {
  return (
    <div className="s2-card s2-card--after">
      <div className="s2-card-accent s2-card-accent--after"></div>
      <div className="s2-card-header">
        <div className="s2-card-icon s2-card-icon--after">✨</div>
        <span className="s2-card-label s2-card-label--after">THE CURE</span>
      </div>
      <div className="s2-point-text" style={{ marginTop: '4px', color: 'rgba(248, 250, 252, 0.85)' }}>{cure}</div>
      
      <ul className="s2-checkpoint-list" style={{ marginTop: '16px' }}>
        {features.map((pt: any, i: number) => (
          <li key={i} className="s2-checkpoint-item" style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
            <span className="s2-checkpoint-icon">✅</span>
            <span className="s2-checkpoint-text" style={{ fontSize: '1.1rem', color: '#cbd5e1' }}>{pt}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
