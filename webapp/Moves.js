class Moves {
    constructor(moveX, moveY) {
      this._moveX = moveX;
      this._moveY = moveY;
      console.log(moveX +" "+moveY);
      
    }
  
    get moveX() {
      return this._moveX;
    }
  
    get moveY() {
      return this._moveY;
    }
  }