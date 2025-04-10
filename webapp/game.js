const canvas = document.getElementById('window');
const ctx = canvas.getContext('2d', { willReadFrequently: true });

const cellSize = 50;

const elements = Map;

elements

const matrix = [
  ['&','&','&','&', 4, 4, 4, 4, 4, 4],
  [4, 1, 0, 0, 0, 0, 0, 0, 0, 4],
  [4, 0, 0, 0, 0, 0, 0, 0, 0, 4],
  [4, 0, 2, 0, 0, 0, 0, 0, 0, 4],
  [4, 0, 0, 0, 0, 0, 0, 0, 0, 4],
  [4, 0, 2, 0, 0, 0, 0, 0, 0, 4],
  [4, 0, 0, 0, 0, 0, 0, 0, 0, 4],
  [4, 4, 4, 4, 4, 4, 4, 4, 4, 4]
];
const colorMap = {
  0: "#eee",  
  1: "red",   
  2: "black", 
  3: "green", 
  4: "grey"
};

function reDraw() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  for (let row = 0; row < matrix.length; row++) {
    for (let col = 0; col < matrix[row].length; col++) {
      const value = matrix[row][col];
      const color = colorMap[value] || "#fff";
      const x = col * cellSize;
      const y = row * cellSize;

      ctx.fillStyle = color;
      ctx.fillRect(x, y, cellSize, cellSize);
      ctx.strokeStyle = "#ccc";
      ctx.strokeRect(x, y, cellSize, cellSize);
    }
  }
}

reDraw();
