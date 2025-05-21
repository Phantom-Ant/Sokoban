document.addEventListener("DOMContentLoaded", () => {
  fetch('http://localhost/PHP/Sokoban/levels.php')
    .then(res => res.json())
    .then(data => {
      const container = document.getElementById("levels-container");
      
      data.forEach(livello => {
        const col = document.createElement("div");
        col.className = "col-md-3 col-sm-6 mb-4";
        
        const card = document.createElement("div");
        card.className = "level-card";
        
        // Estrai la dimensione della mappa
        const rows = livello.data.split('\\n').length;
        const cols = livello.data.split('\\n')[0].length;
        
        card.innerHTML = `
          <div class="level-title">${livello.name}</div>
          <div class="level-details mt-2">
            <div>ID: ${livello.id}</div>
            <div>Dimensione: ${cols}×${rows}</div>
            <div>Autore: ${livello.publisher}</div>
            <div>Data: ${new Date(livello.publish_date).toLocaleDateString()}</div>
          </div>
        `;
        
        const levelData = encodeURIComponent(livello.data);
        
        card.addEventListener("click", () => {
          window.location.href = `../game/game.html?levelData=${levelData}&level=${livello.id}`;
        });
        
        col.appendChild(card);
        container.appendChild(col);
      });
    })
    .catch(err => {
      const errorDiv = document.getElementById("error-message");
      errorDiv.classList.remove("d-none");
      errorDiv.textContent = "Errore nel caricamento dei livelli. Riprova più tardi.";
      console.error(err);
    });
});