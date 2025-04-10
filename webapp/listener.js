document.addEventListener('keydown', (e) => {
    let movement =10;
    if (e.key === 'ArrowUp') player.y -= player.speed+movement;
    if (e.key === 'ArrowDown') player.y += player.speed+movement;
    if (e.key === 'ArrowLeft') player.x -= player.speed+movement;
    if (e.key === 'ArrowRight') player.x += player.speed+movement;
    requestAnimationFrame(reDraw);
  });