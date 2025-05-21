const canvas = document.getElementById("window");
const ctx = canvas.getContext("2d");

const tileSize = 48;
const params = new URLSearchParams(window.location.search);

let level = parseInt(params.get("level"));
let player = null;
let map = [];
let movesCounter = 0;
let originalMapString = "";

let startTime = null;
let timerInterval = null;

const images = {
  floor: new Image(),
  wall: new Image(),
  box: new Image(),
  target: new Image(),
  playerFront: new Image(),
  playerBack: new Image(),
  playerLeft: new Image(),
  playerRight: new Image(),
  boxPlaced: new Image()
};

const imageSources = {
  floor: "../../assets/img/floor.png",
  wall: "../../assets/img/wall.png",
  box: "../../assets/img/box.png",
  target: "../../assets/img/target.png",
  playerFront: "../../assets/img/playerFront.jpg",
  playerBack: "../../assets/img/playerBack.jpg",
  playerLeft: "../../assets/img/playerLeft.png",
  playerRight: "../../assets/img/playerRight.png",
  boxPlaced: "../../assets/img/boxPlaced.png"
};

function loadImages() {
  const promises = [];
  for (let key in images) {
    images[key].src = imageSources[key];
    promises.push(new Promise(resolve => images[key].onload = resolve));
  }
  return Promise.all(promises);
}

function decodeMap(encoded) {
  return encoded.replace(/\\n/g, '\n').trim().split('\n').map(row => row.split(''));
}

function startTimer() {
  startTime = new Date();
  timerInterval = setInterval(updateTimer, 1000);
}

function updateTimer() {
  const currentTime = new Date();
  const elapsed = new Date(currentTime - startTime);
  const minutes = elapsed.getMinutes().toString().padStart(2, '0');
  const seconds = elapsed.getSeconds().toString().padStart(2, '0');
  document.getElementById("timer").textContent = `${minutes}:${seconds}`;
}

function stopTimer() {
  clearInterval(timerInterval);
}

function loadLevel() {
  stopTimer();
  const levelData = params.get("levelData");
  if (!levelData) {
    alert("Nessun livello specificato.");
    window.location.href = '../levels/levels.html';
    startTimer();
    return;
  }

  originalMapString = decodeURIComponent(levelData).replace(/\\n/g, '\n').trim();
  map = decodeMap(originalMapString);

  let playerFound = false;
  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[y].length; x++) {
      if (map[y][x] === '@') {
        player = { x, y, direction: 'front' };
        map[y][x] = '-';
        playerFound = true;
        break;
      }
    }
    if (playerFound) break;
  }

  if (!playerFound) {
    alert("Giocatore non trovato nella mappa!");
    return;
  }

  movesCounter = 0;
  document.getElementById("moves").textContent = movesCounter;
  document.getElementById("message").classList.remove("show");
  document.getElementById("levels").classList.add("hidden");

  resizeCanvas();
  drawMap();
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
      const px = x * tileSize;
      const py = y * tileSize;
      switch (tile) {
        case '#': ctx.drawImage(images.wall, px, py, tileSize, tileSize); break;
        case '-': case '_': ctx.drawImage(images.floor, px, py, tileSize, tileSize); break;
        case '$': ctx.drawImage(images.box, px, py, tileSize, tileSize); break;
        case '.': ctx.drawImage(images.target, px, py, tileSize, tileSize); break;
        case '*': ctx.drawImage(images.boxPlaced, px, py, tileSize, tileSize); break;
        default: ctx.drawImage(images.floor, px, py, tileSize, tileSize); break;
      }
    }
  }

  const currentPlayerImage = images[`player${capitalize(player.direction)}`] || images.playerFront;
  ctx.drawImage(currentPlayerImage, player.x * tileSize, player.y * tileSize, tileSize, tileSize);
}

function capitalize(str) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

function isFree(x, y) {
  if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) return false;
  return ['-', '_', '.'].includes(map[y][x]);
}

function move(dx, dy, direction) {
  const nx = player.x + dx;
  const ny = player.y + dy;
  const nnx = player.x + dx * 2;
  const nny = player.y + dy * 2;

  if (nx < 0 || ny < 0 || nx >= map[0].length || ny >= map.length) return;

  const nextTile = map[ny][nx];

  if (nextTile === '#' || nextTile === 1) return;

  if (nextTile === '$' || nextTile === '*') {
    if (!isFree(nnx, nny)) return;
    map[ny][nx] = nextTile === '*' ? '.' : '-';
    map[nny][nnx] = map[nny][nnx] === '.' ? '*' : '$';
  }

  player.x = nx;
  player.y = ny;
  player.direction = direction;

  movesCounter++;
  document.getElementById("moves").textContent = movesCounter;
  drawMap();
  checkWin();
}

function checkWin() {
  for (let row of map) {
    if (row.includes('$')) return;
  }
  stopTimer();
  document.getElementById("message").classList.add("show");
  document.getElementById("levels").classList.remove("hidden");
}

function restartLevel() {
  map = decodeMap(originalMapString);
  movesCounter = 0;
  document.getElementById("moves").textContent = movesCounter;
  document.getElementById("message").classList.remove("show");
  document.getElementById("levels").classList.add("hidden");

  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[y].length; x++) {
      if (map[y][x] === '@') {
        player = { x, y, direction: 'front' };
        map[y][x] = '-';
      }
    }
  }

  drawMap();
}

document.addEventListener("keydown", (e) => {
  switch (e.key) {
    case "ArrowUp": move(0, -1, "back"); break;
    case "ArrowDown": move(0, 1, "front"); break;
    case "ArrowLeft": move(-1, 0, "left"); break;
    case "ArrowRight": move(1, 0, "right"); break;
  }
});

document.getElementById("levels").addEventListener("click", () => {
  window.location.href = '../levels/levels.html';
});

document.getElementById("restartBtn").addEventListener("click", () => {
  restartLevel();
});

window.onload = () => {
  loadImages().then(loadLevel);
};
