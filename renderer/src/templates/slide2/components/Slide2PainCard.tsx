import React from 'react';

export interface Slide2PainCardProps {
  pain: string;
}

export default function Slide2PainCard({ pain }: Slide2PainCardProps) {
  return (
    <div className="s2-card s2-card--before">
      <div className="s2-card-accent s2-card-accent--before"></div>
      <div className="s2-card-header">
        <div className="s2-card-icon s2-card-icon--before">⚠️</div>
        <span className="s2-card-label s2-card-label--before">THE PAIN</span>
      </div>
      <div className="s2-point-text" style={{ marginTop: '4px', color: 'rgba(248, 250, 252, 0.85)' }}>{pain}</div>
    </div>
  );
}
