
document.addEventListener("DOMContentLoaded", () => {
  fetch('http://localhost/PHP/Sokoban/levels.php')
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

        button.innerHTML = `
          <strong>ID:</strong> ${livello.id}<br>
          <strong>Nome:</strong> ${livello.name}<br>
          <strong>Dim:</strong> ${livello.publish_date}<br>
          <strong>Autore:</strong> ${livello.publisher}
        `;

        const levelData = encodeURIComponent(livello.data);

        button.addEventListener("click", () => {
          window.location.href = `../game/game.html?levelData=${levelData}&level=${livello.id}`;
        });

        td.appendChild(button);
        tr.appendChild(td);
      });
    })
    .catch(err => {
      // Mostra errore in un'area dedicata senza sovrascrivere il corpo della pagina
      const errorDiv = document.getElementById("error-message");
      errorDiv.textContent = "Errore nel caricamento dei livelli.";

      if (errorDiv) errorDiv.textContent = "Errore nel caricamento dei livelli.";
      console.error(err);
    });
});
