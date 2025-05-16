const canvas = document.getElementById("window");
const ctx = canvas.getContext("2d");

const tileSize = 48;
const params = new URLSearchParams(window.location.search);
let level = parseInt(params.get("level")) || 1;
let movesCounter = 0;
let player;
let map;

const images = {
  floor: new Image(),
  wall: new Image(),
  box: new Image(),
  target: new Image(),
  player: new Image(),
  boxPlaced: new Image()
};

images.floor.src = "../assets/floor.png";
images.wall.src = "../assets/wall.png";
images.box.src = "../assets/box.png";
images.target.src = "../assets/target.png";
images.player.src = "../assets/boxPlaced.png";
images.boxPlaced.src = "../assets/boxPlaced.png";

function loadLevel() {
  fetch('get_level.php?level=' + level)
    .then(response => response.json())
    .then(data => {
      console.log(data);
      if (data.error) {
        alert(data.error);
      } else {
        map = data.map;
        player = data.player;
        movesCounter = 0;
        document.getElementById("moves").textContent = movesCounter;
        document.getElementById("level").textContent = level;
        document.getElementById("message").classList.remove("show");
        document.getElementById("levels").classList.add("hidden");

        resizeCanvas();
        
        drawMap();
      }
    })
    .catch(error => {
      console.error('Error loading level:', error);
    });
}

function resizeCanvas() {
  canvas.width = map[0].length * tileSize;
  canvas.height = map.length * tileSize;
}

function drawMap() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  
  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[y].length; x++) {
      const tile = map[y][x];
      if (tile === 0) {
        ctx.drawImage(images.floor, x * tileSize, y * tileSize, tileSize, tileSize);
      } else if (tile === 1) {
        ctx.drawImage(images.wall, x * tileSize, y * tileSize, tileSize, tileSize);
      } else if (tile === 3) {
        ctx.drawImage(images.box, x * tileSize, y * tileSize, tileSize, tileSize);
      } else if (tile === 4) {
        ctx.drawImage(images.target, x * tileSize, y * tileSize, tileSize, tileSize);
      } else if (tile === 5) {
        ctx.drawImage(images.boxPlaced, x * tileSize, y * tileSize, tileSize, tileSize);
      }
    }
  }
  
  ctx.drawImage(images.player, player.x * tileSize, player.y * tileSize, tileSize, tileSize);
}

function move(dx, dy) {
  const nx = player.x + dx;
  const ny = player.y + dy;
  const nnx = player.x + dx * 2;
  const nny = player.y + dy * 2;

  if (map[ny][nx] === 1) return;

  if (map[ny][nx] === 3 || map[ny][nx] === 5) {

    if (isFree(nnx, nny)) {

      map[ny][nx] = (map[ny][nx] === 5) ? 4 : 0; 
      map[nny][nnx] = (map[nny][nnx] === 4) ? 5 : 3;
    } else {
      return;
    }
  }
  
  player.x = nx;
  player.y = ny;

  movesCounter++;
  document.getElementById("moves").textContent = movesCounter;

  drawMap();

  checkWin();
}

function isFree(x, y) {
  if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) {
    return false;
  }
  return map[y][x] === 0 || map[y][x] === 4;
}

function checkWin() {
  for (let row of map) {
    if (row.includes(4)) return;
  }
  document.getElementById("message").classList.add("show");
  document.getElementById("levels").classList.remove("hidden");
}

function restartLevel() {
  loadLevel();
  movesCounter = 0;
  document.getElementById("moves").textContent = movesCounter;
  document.getElementById("message").classList.remove("show");
  document.getElementById("levels").classList.add("hidden");
}

document.addEventListener("keydown", (e) => {
  switch (e.key) {
    case "ArrowUp": move(0, -1); break;
    case "ArrowDown": move(0, 1); break;
    case "ArrowLeft": move(-1, 0); break;
    case "ArrowRight": move(1, 0); break;
  }
});

document.getElementById("levels").addEventListener("click", () => {
  window.location.href = '../levels/levels.html';
});

document.getElementById("restartBtn").addEventListener("click", () => {
  restartLevel();
});
window.onload = loadLevel;
