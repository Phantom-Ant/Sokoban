const canvas = document.getElementById("window");
const ctx = canvas.getContext("2d");

const tileSize = 48;

const tiles = {
  wall: "#444",
  floor: "#222",
  goal: "#ffeb3b",
  box: "#ff9800",
  player: "#03a9f4",
  boxOnGoal: "#4caf50"
};

let level = 1;
let moves = 0;

let player;
let map;

function loadLevel() {
  map = [
    [1,1,1,1,1,1,1,1,1,1],
    [1,0,0,0,0,0,0,0,0,1],
    [1,0,3,0,0,0,4,0,0,1],
    [1,0,0,2,0,0,0,0,0,1],
    [1,1,1,1,1,1,1,1,1,1]
  ];

  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[y].length; x++) {
      if (map[y][x] === 2) {
        player = { x, y };
        map[y][x] = 0;
      }
    }
  }

  moves = 0;
  document.getElementById("moves").textContent = moves;
  document.getElementById("message").classList.remove("show");
  document.getElementById("nextLevelBtn").classList.add("hidden");
  drawMap();
}

function drawMap() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[y].length; x++) {
      const tile = map[y][x];
      ctx.fillStyle = tiles.floor;
      ctx.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);

      if (tile === 1) {
        ctx.fillStyle = tiles.wall;
        ctx.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
      } else if (tile === 3) {
        ctx.fillStyle = tiles.box;
        ctx.fillRect(x * tileSize + 6, y * tileSize + 6, tileSize - 12, tileSize - 12);
      } else if (tile === 4) {
        ctx.fillStyle = tiles.goal;
        ctx.beginPath();
        ctx.arc(x * tileSize + 24, y * tileSize + 24, 10, 0, 2 * Math.PI);
        ctx.fill();
      } else if (tile === 5) {
        ctx.fillStyle = tiles.boxOnGoal;
        ctx.fillRect(x * tileSize + 6, y * tileSize + 6, tileSize - 12, tileSize - 12);
      }
    }
  }

  // Draw player with animation
  ctx.fillStyle = tiles.player;
  ctx.beginPath();
  ctx.arc(player.x * tileSize + 24, player.y * tileSize + 24, 18, 0, 2 * Math.PI);
  ctx.fill();
}

function isFree(x, y) {
  return map[y][x] === 0 || map[y][x] === 4;
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
    } else return;
  }

  player.x = nx;
  player.y = ny;
  moves++;
  document.getElementById("moves").textContent = moves;
  drawMap();
  checkWin();
}

function checkWin() {
  for (let row of map) {
    if (row.includes(4)) return;
  }

  document.getElementById("message").classList.add("show");
  document.getElementById("nextLevelBtn").classList.remove("hidden");
}

function resetMap() {
  loadLevel();
}

document.addEventListener("keydown", (e) => {
  switch (e.key) {
    case "ArrowUp": move(0, -1); break;
    case "ArrowDown": move(0, 1); break;
    case "ArrowLeft": move(-1, 0); break;
    case "ArrowRight": move(1, 0); break;
  }
});

//audio
resetMap();


document.getElementById("nextLevelBtn").addEventListener("click", () => {
  level++;
  document.getElementById("level").textContent = level;
  alert("Prossimo livello coming soon!");
});

loadLevel();
