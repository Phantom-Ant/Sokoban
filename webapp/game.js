const canvas = document.getElementById('window');
const ctx = canvas.getContext('2d');

// defined and instantiated the components
const player = { x: 50, y: 50, width: 50, height: 50, color: 'red', speed: 5 };
const wall = { x: 200, y: 200, width: 50, height: 50, color: 'black' };
const box = { x: 300, y: 200, width: 50, height: 50, color: 'green' };
const path = { x: 400, y: 200, width: 50, height: 50, color: 'grey' };

function drawRect(obj) {
    ctx.fillStyle = obj.color;
    ctx.fillRect(obj.x, obj.y, obj.width, obj.height);
}
// A gameloop is redundant for this game, a waste of the processor
/// the components are static and waiting for an event
function reDraw() {// rewrite the canvas(all components?)
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // these are the main components of the game
    drawRect(player);
    drawRect(wall);
    drawRect(box);
    drawRect(path);
}
reDraw();