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
