
document.addEventListener("DOMContentLoaded", function () {
    // Función para agregar el efecto de desvanecimiento
    const fadeInElements = document.querySelectorAll('.fade-in');
    fadeInElements.forEach((element) => {
        element.style.opacity = 0;
        element.style.transition = 'opacity 2s ease-in-out';
        element.style.opacity = 1;
    });

    // Responsividad del menú móvil
    const mobileMenuButton = document.getElementById("bar");
    const closeMenuButton = document.getElementById("close");
    const navbar = document.getElementById("navbar");

    mobileMenuButton.addEventListener("click", () => {
        navbar.style.display = "block"; // Muestra el menú
    });

    closeMenuButton.addEventListener("click", () => {
        navbar.style.display = "none"; // Oculta el menú
    });
});
