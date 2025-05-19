
document.addEventListener("DOMContentLoaded", () => {
  fetch('levels.php')
document.addEventListener("DOMContentLoaded", () => {
  fetch('../levels/levels.php')
    .then(res => res.json())
    .then(data => {
      const table = document.getElementById("livelli");
      const colonne = 4;
      let tr;

      data.forEach((livello, i) => {
        if (i % colonne === 0) {
          tr = document.createElement("tr");
          table.appendChild(tr);
        }

        const td = document.createElement("td");
        td.style.padding = '10px';  // Aggiungi spazio tra le celle

        const button = document.createElement("button");
        button.type = "button";
        button.classList.add("level-button");
        td.style.padding = '10px';

        const button = document.createElement("button");
        button.type = "button";
        button.classList.add("level-button");

        button.innerHTML = `
          <strong>ID:</strong> ${livello.id_livello}<br>
          <strong>Nome:</strong> ${livello.nome}<br>
          <strong>Dim:</strong> ${livello.dimX}x${livello.dimY}<br>
          <strong>Autore:</strong> ${livello.publisher}
        `;

        // Aggiungi l'evento di click
        button.addEventListener("click", () => {
          window.location.href = `../game/game.html?level=${livello.id_livello}`;
        });


        const levelData = encodeURIComponent(livello.data);

        button.addEventListener("click", () => {
          window.location.href = `../game/game.html?levelData=${levelData}&level=${livello.id_livello}`;
        });

        td.appendChild(button);
        tr.appendChild(td);
      });
    })
    .catch(err => {
      // Mostra errore in un'area dedicata senza sovrascrivere il corpo della pagina
      const errorDiv = document.getElementById("error-message");
      errorDiv.textContent = "Errore nel caricamento dei livelli.";
      const errorDiv = document.getElementById("error-message");

      if (errorDiv) errorDiv.textContent = "Errore nel caricamento dei livelli.";
      console.error(err);
    });
});
