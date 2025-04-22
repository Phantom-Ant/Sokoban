document.addEventListener('keydown', (e) => {
    let movement =10;
    if (e.key === 'ArrowUp') matrix[position[0]][position[1]] = 0;
    if (e.key === 'ArrowDown') matrix[position[0]][position[1]] = 0;
    if (e.key === 'ArrowLeft') matrix[position[0]][position[1]] = 0;
    if (e.key === 'ArrowRight') matrix[position[0]][position[1]] = 0;
    
    reDraw();

  });