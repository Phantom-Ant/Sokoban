// Ottieni il canvas e il suo contesto 2D
const canvas = document.getElementById("window");
const ctx = canvas.getContext("2d");

const tileSize = 48; // Dimensione di ogni tile in pixel
const params = new URLSearchParams(window.location.search); 
let level = parseInt(params.get("level")); // livello corrente
let player = null; //giocatore
let map = []; //mappa
let movesCounter = 0; // count mosse
let originalMapString = ""; // mappa originale per reset
let gameTime = 0; // tempo di gioco in secondi
let timerInterval = null; //  intervallo del timer

//immagini
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

// vari percorsi delle immagini
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
function startTimer() {
  gameTime = 0;
  updateTimer();
  timerInterval = setInterval(updateTimer, 1000); // aggiorna ogni secondo
}

// aggiorna il display del timer
function updateTimer() {
  gameTime++;
  const minutes = Math.floor(gameTime / 60).toString().padStart(2, '0');
  const seconds = (gameTime % 60).toString().padStart(2, '0');
  document.getElementById("timer").textContent = `${minutes}:${seconds}`;
}

function stopTimer() {
  clearInterval(timerInterval);
}
function loadImages() {
  const promises = [];
  for (let key in images) {
    images[key].src = imageSources[key];
    promises.push(new Promise(resolve => images[key].onload = resolve));
  }
  return Promise.all(promises); // restituisce una promise che si risolve quando tutte le immagini sono caricate
}

// Decodifica la mappa da stringa a array 2d
function decodeMap(encoded) {
  return encoded.replace(/\\n/g, '\n').trim().split('\n').map(row => row.split(''));
}

// carica il livello corrente
function loadLevel() {
  stopTimer();
  const levelData = params.get("levelData");
  
  // controlla livello
  if (!levelData) {
    alert("Nessun livello specificato.");
    window.location.href = '../levels/levels.html';
    return;
  }

  // decodifica e prepara la mappa
  originalMapString = decodeURIComponent(levelData).replace(/\\n/g, '\n').trim();
  map = decodeMap(originalMapString);

  // trova la posizione iniziale del giocatore
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

  // resetta
  movesCounter = 0;
  document.getElementById("moves").textContent = movesCounter;
  document.getElementById("message").classList.remove("show");
  document.getElementById("levels").classList.add("hidden");

  resizeCanvas();
  drawMap();
  startTimer();
}

// ridimensiona il canvas in base alla mappa
function resizeCanvas() {
  const tileSize = 48;
  canvas.width = map[0].length * tileSize;
  canvas.height = map.length * tileSize;
  
  // calcola la scala per adattarsi allo schermo
  const maxDisplaySize = 580;
  const scale = Math.min(
    maxDisplaySize / canvas.width,
    maxDisplaySize / canvas.height,
    1
  );
  
  // applica le dimensioni ridimensionate
  canvas.style.width = `${canvas.width * scale}px`;
  canvas.style.height = `${canvas.height * scale}px`;
}

// disegna mappa
function drawMap() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  // disegna ogni tile della mappa
  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[y].length; x++) {
      const tile = map[y][x];
      const px = x * tileSize;
      const py = y * tileSize;
      
      // seleziona l'immagine corretta in base al tipo di tile
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

  // disegna il giocatore con la direzione corretta
  const currentPlayerImage = images[`player${capitalize(player.direction)}`] || images.playerFront;
  ctx.drawImage(currentPlayerImage, player.x * tileSize, player.y * tileSize, tileSize, tileSize);
}

function capitalize(str) { // Capitalizza la prima lettera di una stringa 
  return str.charAt(0).toUpperCase() + str.slice(1);
}

// se posizione è libera (pavimento o target)
function isFree(x, y) {
  if (x < 0 || y < 0 || x >= map[0].length || y >= map.length) return false;
  return ['-', '_', '.'].includes(map[y][x]);
}

// gestisce il movimento del giocatore
function move(dx, dy, direction) {
  const nx = player.x + dx; // x del giocatore
  const ny = player.y + dy; // y del giocatore
  const nnx = player.x + dx * 2; //  x per scatola
  const nny = player.y + dy * 2; //  y per scatola

  // controllo i bordi della mappa
  if (nx < 0 || ny < 0 || nx >= map[0].length || ny >= map.length) return;

  const nextTile = map[ny][nx];

  // blocca il movimento su muri
  if (nextTile === '#' || nextTile === 1) return;

  // movimento delle scatole
  if (nextTile === '$' || nextTile === '*') {
    if (!isFree(nnx, nny)) return;
    map[ny][nx] = nextTile === '*' ? '.' : '-';
    map[nny][nnx] = map[nny][nnx] === '.' ? '*' : '$';
  }

  // aggiorna la posizione del giocatore
  player.x = nx;
  player.y = ny;
  player.direction = direction;

  // ++ aggiorna il contatore mosse
  movesCounter++;
  document.getElementById("moves").textContent = movesCounter;
  drawMap();
  checkWin();
}

function checkWin() {
  for (let row of map) {
    if (row.includes('$')) return; // se ci sono ancora scatole da posizionare
  }
  

  stopTimer();
  document.getElementById("message").classList.add("show");
  document.getElementById("levels").classList.remove("hidden");
}

function restartLevel() {
  stopTimer();
  map = decodeMap(originalMapString);
  movesCounter = 0;
  document.getElementById("moves").textContent = movesCounter;
  document.getElementById("message").classList.remove("show");
  document.getElementById("levels").classList.add("hidden");

  //riposiziona il giocatore
  for (let y = 0; y < map.length; y++) {
    for (let x = 0; x < map[y].length; x++) {
      if (map[y][x] === '@') {
        player = { x, y, direction: 'front' };
        map[y][x] = '-';
      }
    }
  }

  drawMap();
  startTimer();
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


window.addEventListener('resize', () => {
  if (map.length > 0) {
    resizeCanvas();
    drawMap();
  }
});
window.onload = () => {
  loadImages().then(loadLevel);
};