import React, { ReactNode } from 'react';

interface SlideWrapperProps {
  slideNumber: number;
  backgroundImage: string;
  showHeader?: boolean;
  badgeText?: string;
  badgeClass?: string;
  handle?: string;
  footerAction?: string;
  totalSlides?: number;
  children?: ReactNode;
}

export default function SlideWrapper({
  slideNumber,
  backgroundImage,
  showHeader = true,
  badgeText = '',
  badgeClass = 'badge-go',
  handle = '@golang_verse',
  footerAction = 'Swipe →',
  totalSlides = 5,
  children
}: SlideWrapperProps) {
  return (
    <div 
      className={`slide-wrapper actual-size`} 
      id={`slide-${slideNumber}-wrap`}
    >
      <div 
        className="slide-inner" 
        id={`slide-${slideNumber}`} 
        style={{ backgroundImage: `url('${backgroundImage}')` }}
      >
        {showHeader && (
          <div className="slide-header">
            <div className={`slide-badge ${badgeClass}`}>{badgeText}</div>
            <div className="footer-page">{slideNumber} / {totalSlides}</div>
          </div>
        )}
        
        {children}
        
        <div className="slide-footer">
          <div className="footer-handle">{handle}</div>
          <div className="footer-action">{footerAction}</div>
        </div>
      </div>
    </div>
  );
}
