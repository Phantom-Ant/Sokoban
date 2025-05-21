document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('loginForm');
  const resultDiv = document.getElementById('result');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const data = {
      username: form.username.value,
      password: form.password.value
    };

    try {
      const response = await fetch('http://localhost/PHP/Sokoban/login.php', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });

      if (!response.ok) {
        throw new Error('Errore nella risposta dal server');
      }

      const text = await response.text();

      if (text.trim() === "Login successful") {
        resultDiv.textContent = text;
        resultDiv.style.color = "lightgreen";
        window.location.href = 'sokoban.html';
      } else if (text.trim() === "Login failed") {
        resultDiv.textContent = "Nome utente o password errati";
        resultDiv.style.color = "tomato";
      } else {
        resultDiv.textContent = "Risposta inaspettata dal server";
        resultDiv.style.color = "orange";
      }
    } catch (error) {
      resultDiv.textContent = "Errore di connessione o server";
      resultDiv.style.color = "tomato";
      console.error(error);
    }
  });
});
