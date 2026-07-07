import React from 'react';

export interface TemplateArrayProps<T> {
  name: string;
  items: T[];
  isTemplate?: boolean;
  children: (item: T, index: number) => React.ReactNode;
}

export function TemplateArray<T>({ name, items, isTemplate, children }: TemplateArrayProps<T>) {
  if (isTemplate) {
    // Renders the block tags using standard data-th-* attributes
    // These will be rendered seamlessly by React into static markup
    return (
      <div data-th-each={`${name}Item : \${slide.${name}}`} data-th-remove="tag">
        {children({} as T, 0)}
      </div>
    );
  }

  // Normal React rendering for preview mode
  return (
    <>
      {items.map((item, index) => children(item, index))}
    </>
  );
}
