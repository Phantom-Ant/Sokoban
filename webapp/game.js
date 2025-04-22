const canvas = document.getElementById("window");
const ctx = canvas.getContext("2d");

const tileSize = 48;
let level = 1;
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

images.floor.src = "assets/floor.png";
images.wall.src = "assets/wall.png";
images.box.src = "assets/box.png";
images.target.src = "assets/target.png";
images.player.src = "assets/boxPlaced.png";
images.boxPlaced.src = "assets/boxPlaced.png";


function loadLevel() {
  map = [
  [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1],
  [1,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1],
  [1,0,3,0,1,0,0,0,3,0,0,1,0,1,0,0,1],
  [1,0,0,0,1,0,1,0,0,0,0,0,0,0,4,0,1],
  [1,0,0,0,1,0,1,1,1,1,1,1,0,1,0,0,1],
  [1,0,0,1,1,0,0,0,0,0,0,0,0,1,0,0,1],
  [1,0,0,0,0,0,1,1,0,1,1,1,0,1,1,0,1],
  [1,0,4,0,1,0,1,0,2,0,0,1,0,0,1,0,1],
  [1,1,1,0,1,0,1,0,0,0,0,1,1,0,1,1,1],
  [1,0,0,0,1,0,1,1,1,1,0,0,0,0,0,0,1],
  [1,0,3,0,0,0,0,0,0,1,1,1,1,1,0,0,1],
  [1,0,0,1,1,1,1,1,0,1,0,0,0,1,0,4,1],
  [1,0,0,1,0,0,0,1,0,1,0,3,0,1,0,0,1],
  [1,0,0,1,0,3,0,1,0,1,0,0,0,1,0,0,1],
  [1,0,4,0,0,0,0,1,0,1,1,1,1,1,0,0,1],
  [1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1],
  [1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1]
];

canvas.width = map[0].length * tileSize;
canvas.height = map.length * tileSize;

  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[y].length; x++) {
      if (map[y][x] === 2) {
        player = { x, y };
        map[y][x] = 0;
      }
    }
  }

  movesCounter = 0;
  document.getElementById("moves").textContent = movesCounter;
  document.getElementById("message").classList.remove("show");
  document.getElementById("nextLevelBtn").classList.add("hidden");
  drawMap();
}

function drawMap() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[y].length; x++) {
      const character = map[y][x];
      if (character === 0) {
        ctx.drawImage(images.floor, x * tileSize, y * tileSize, tileSize, tileSize);
      } else if (character === 1) {
        ctx.drawImage(images.wall, x * tileSize, y * tileSize, tileSize, tileSize);
      } else if (character === 3) {
        ctx.drawImage(images.box, x * tileSize, y * tileSize, tileSize, tileSize);
      } else if (character === 4) {
        ctx.drawImage(images.target, x * tileSize, y * tileSize, tileSize, tileSize);
      } else if (character === 5) {
        ctx.drawImage(images.boxPlaced, x * tileSize, y * tileSize, tileSize, tileSize);
      }
    }
  }
  ctx.drawImage(images.player, player.x * tileSize, player.y * tileSize, tileSize, tileSize);
}


function isFree(x, y) {
  return map[y][x] === 0 || map[y][x] === 4;
}

function move(dx, dy) {
  let moves=[];
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
  moves.push(new Moves(nx,ny));

  movesCounter++;
  document.getElementById("moves").textContent = movesCounter;
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
  alert("ancora in aggiornamento ahaha");
});
