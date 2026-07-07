import React from 'react';

export interface TemplateArrayProps<T> {
  name: string;
  items: T[];
  isTemplate?: boolean;
  children: (item: T, index: number) => React.ReactNode;
}

export function TemplateArray<T>({ name, items, isTemplate, children }: TemplateArrayProps<T>) {
  if (isTemplate) {
    // Renders the block tags without HTML comments when using renderToStaticMarkup
    return (
      <>
        {`{{#${name}}}`}
        {children({} as T, 0)}
        {`{{/${name}}}`}
      </>
    );
  }

  // Normal React rendering for preview mode
  return (
    <>
      {items.map((item, index) => children(item, index))}
    </>
  );
}
