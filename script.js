// Animación de desvanecimiento
window.onload = function() {
	var login = document.querySelector(".login");
	login.style.opacity = 0;

	(function fade() {
		var val = parseFloat(login.style.opacity);
		if (!((val += .1) > 1)) {
			login.style.opacity = val;
			requestAnimationFrame(fade);
		}
	})();
};

const form = document.getElementById('cadastro');

form.addEventListener('submit', (event) => {
  event.preventDefault();
  const nome = document.getElementById('nome').value;
  const email = document.getElementById('email').value;
  const telefone = document.getElementById('telefone').value;
  const endereco = document.getElementById('endereco').value;
  console.log(`Nome: ${nome}\nEmail: ${email}\nTelefone: ${telefone}\nEndereço: ${endereco}`);
  // Aqui você pode adicionar código para enviar os dados do formulário para um servidor ou armazená-los localmente
});

const menu = document.querySelector('.menu');
const hamburguer = document.querySelector('.hamburguer');

hamburguer.addEventListener('click', () => {
  menu.classList.toggle('aberto');
});
