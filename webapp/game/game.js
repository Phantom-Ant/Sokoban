const canvas = document.getElementById("window");
const ctx = canvas.getContext("2d");

const tileSize = 48;
const params = new URLSearchParams(window.location.search);
<<<<<<< HEAD
let level = parseInt(params.get("level")) || 1;
let movesCounter = 0;
let player;
let map;
=======
let level = parseInt(params.get("level"));
let originalMapString = "";

let movesCounter = 0;
let player = null;
let map = [];
>>>>>>> 328c8a3 (add titles)

const images = {
  floor: new Image(),
  wall: new Image(),
  box: new Image(),
  target: new Image(),
<<<<<<< HEAD
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
=======
  playerFront: new Image(),
  playerBack: new Image(),
  playerLeft: new Image(),
  playerRight: new Image(),
  boxPlaced: new Image()
};

const imageSources = {
  floor: "../../assets/floor.png",
  wall: "../../assets/wall.png",
  box: "../../assets/box.png",
  target: "../../assets/target.png",
  playerFront: "../../assets/playerFront.png",
  playerBack: "../../assets/playerBack.png",
  playerLeft: "../../assets/playerLeft.png",
  playerRight: "../../assets/playerRight.png",
  boxPlaced: "../../assets/boxPlaced.png"
};

function loadImages() {
  const promises = [];
  for (let key in images) {
    const img = images[key];
    img.src = imageSources[key];
    promises.push(new Promise(resolve => img.onload = resolve));
  }
  return Promise.all(promises);
}

function decodeMap(encoded) {
  return encoded.replace(/\\n/g, '\n').trim().split('\n').map(r => r.split(''));
}

function loadLevel() {
  const levelData = params.get("levelData");
  if (!levelData) {
    alert("Nessun livello specificato.");
    window.location.href = '../levels/levels.html';
    return;
  }

  originalMapString = decodeURIComponent(levelData).replace(/\\n/g, '\n').trim();
  map = decodeMap(levelData);

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
  document.getElementById("level").textContent = level || "?";
  document.getElementById("message").classList.remove("show");
  document.getElementById("levels").classList.add("hidden");

  resizeCanvas();
  drawMap();
>>>>>>> 328c8a3 (add titles)
}

function resizeCanvas() {
  canvas.width = map[0].length * tileSize;
  canvas.height = map.length * tileSize;
}

function drawMap() {
<<<<<<< HEAD
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
=======
  if (!player) return;
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

  let currentPlayerImage = images[`player${capitalize(player.direction)}`] || images.playerFront;
  ctx.drawImage(currentPlayerImage, player.x * tileSize, player.y * tileSize, tileSize, tileSize);
}

function capitalize(str) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

function isFree(x, y) {
  if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) return false;
  return ['-', '_', '.'].includes(map[y][x]);
>>>>>>> 328c8a3 (add titles)
}

function move(dx, dy) {
  const nx = player.x + dx;
  const ny = player.y + dy;
  const nnx = player.x + dx * 2;
  const nny = player.y + dy * 2;

<<<<<<< HEAD
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
=======
  if (nx < 0 || ny < 0 || nx >= map[0].length || ny >= map.length) return;

  const nextTile = map[ny][nx];

  if (nextTile === '#') return;

  if (nextTile === '$' || nextTile === '*') {
    if (!isFree(nnx, nny)) return;
    map[ny][nx] = nextTile === '*' ? '.' : '-';
    map[nny][nnx] = map[nny][nnx] === '.' ? '*' : '$';
  }

  player.x = nx;
  player.y = ny;
  movesCounter++;
  document.getElementById("moves").textContent = movesCounter;
  drawMap();
  checkWin();
}

function checkWin() {
  for (let row of map) {
    if (row.includes('$')) return;
>>>>>>> 328c8a3 (add titles)
  }
  document.getElementById("message").classList.add("show");
  document.getElementById("levels").classList.remove("hidden");
}

function restartLevel() {
<<<<<<< HEAD
  loadLevel();
=======
  map = originalMapString.split('\n').map(row => row.split(''));
>>>>>>> 328c8a3 (add titles)
  movesCounter = 0;
  document.getElementById("moves").textContent = movesCounter;
  document.getElementById("message").classList.remove("show");
  document.getElementById("levels").classList.add("hidden");
<<<<<<< HEAD
=======
  loadLevel();
>>>>>>> 328c8a3 (add titles)
}

document.addEventListener("keydown", (e) => {
  switch (e.key) {
<<<<<<< HEAD
    case "ArrowUp": move(0, -1); break;
    case "ArrowDown": move(0, 1); break;
    case "ArrowLeft": move(-1, 0); break;
    case "ArrowRight": move(1, 0); break;
=======
    case "ArrowUp": player.direction = "back"; move(0, -1); break;
    case "ArrowDown": player.direction = "front"; move(0, 1); break;
    case "ArrowLeft": player.direction = "left"; move(-1, 0); break;
    case "ArrowRight": player.direction = "right"; move(1, 0); break;
>>>>>>> 328c8a3 (add titles)
  }
});

document.getElementById("levels").addEventListener("click", () => {
  window.location.href = '../levels/levels.html';
});

document.getElementById("restartBtn").addEventListener("click", () => {
  restartLevel();
});
<<<<<<< HEAD
window.onload = loadLevel;
=======

window.onload = () => {
  loadImages().then(loadLevel);
};
>>>>>>> 328c8a3 (add titles)
