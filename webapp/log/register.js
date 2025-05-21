document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('registerForm');
  const resultDiv = document.getElementById('result');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const data = {
      username: form.username.value,
      email: form.email.value,
      password: form.password.value
    };

    try {
      const response = await fetch('http://localhost/PHP/Sokoban/register.php', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
      });

      if (!response.ok) {
        throw new Error('Errore nella risposta dal server');
      }

      const text = await response.text();

      if (text.trim() === "Register successful") {
        resultDiv.textContent = "Registrazione completata";
        resultDiv.style.color = "lightgreen";
        window.location.href = 'sokoban.html';
      } else {
        resultDiv.textContent = "Utente già esistente";
        resultDiv.style.color = "orange";
      }
    } catch (error) {
      resultDiv.textContent = "Errore di connessione o server";
      resultDiv.style.color = "tomato";
      console.error(error);
    }
  });
});
