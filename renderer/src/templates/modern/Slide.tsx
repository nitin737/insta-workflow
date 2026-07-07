import React from 'react';

type Props = {
  title: string;
  subtitle: string;
};

export default function Slide({ title, subtitle }: Props) {
  const titleClass = title.length > 30 ? 'text-5xl' : 'text-7xl';
  
  return (
    <div className="w-[1080px] h-[1350px] bg-black text-white p-20 flex flex-col justify-center">
      <h1 className={`${titleClass} font-bold leading-tight`}>
        {title}
      </h1>
      <p className="mt-10 text-3xl text-gray-300">
        {subtitle}
      </p>
    </div>
  );
}
