<% String errorMessage = (String) request.getAttribute("error"); %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styles/principal.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <title>GBA - Subir Legajo</title>
    <link rel="icon" href="assets/Logo_PBSAS2.webp" type="image/x-icon">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
    <header>
        <div class="topnav">
            <div class="logo-container">
                <a href="/principal" class="logo-link">
                    <img src="assets/logo.png" alt="Logo">
                </a>
              </div>
            <div id="myLinks">
                <nav>
                    <a href="/principal" class="link">Inicio</a>
                    <a href="/upload" class="link">Subir Legajo</a>
                    <a href="/transfer" class="link">Transferir Alumno</a>
                    <a href="login?action=logout" class="link">Cerrar Sesion</a>
                </nav>
            </div>
            <a href="javascript:void(0);" class="icon" onclick="myFunction()">
            <i class="fa fa-bars"></i>
            </a>
        </div>    
    </header>
    <main><br><br>
        <section class="search">
            <h2>Agregar Usuario</h2>
            <form id="addUserForm" action="/adduser" method="post"> 
                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" name="nombre" placeholder="Ingrese nombre" required>              
                <label for="apellido">Apellido:</label>
                <input type="text" id="apellido" name="apellido" placeholder="Ingrese apellido" required>               
                <label for="dni">DNI:</label>
                <input type="text" maxlength="8" id="dni" name="dni" placeholder="Ingrese DNI" required>                
                <!-- Aquí agregas el select para que el usuario elija la escuela -->
                <label for="escuela">Escuela:</label>
                <select name="escuela" id="escuela" required>
                    <option value="" disabled selected>Selecciona...</option>
                    <option value="1">Escuela N°1 "Pascuala Mugaburu"</option>
                    <option value="2">Escuela N°2 "Republica de Mexico"</option>
                    <option value="3">Escuela N°3 "Alfonsina Storni"</option>
                    <option value="4">Escuela N°7 "Gabriela Mistral"</option>
                </select><br>               
                <button type="submit">Crear Usuario</button>
            </form>
        </section>
        
    </main>

    <footer>
        <p>&copy; 2024 GBA. Todos los derechos reservados.</p>
    </footer>

    <script>
        document.getElementById('addUserForm').addEventListener('submit', function(event) {
            var escuela = document.getElementById('escuela').value;
            if (escuela === "") {
                alert("Por favor, selecciona una escuela.");
                event.preventDefault();  // Detener el envío del formulario
            }
        });

        function myFunction() {
            var x = document.getElementById("myLinks");
            if (x.style.display === "block") {
                x.style.display = "none";
            } else {
                x.style.display = "block";
            }
        }
        
        <% if(errorMessage != null) { %>
            Swal.fire({
              title: 'Error',
              text: '<%= errorMessage %>',
              icon: 'error',
              confirmButtonText: 'Vale'
            });
        <% } %>
    </script>
</body>
</html>