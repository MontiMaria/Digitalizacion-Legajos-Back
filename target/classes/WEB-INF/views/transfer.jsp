    <%@ page session="true" language="java" contentType="text/html; charset=UTF-8" %>
    <%@ page import="java.util.List" %>
    <%@ page import="com.mycompany.prueba.model.Legajo" %>
    <% 
        Boolean isAdmin = (Boolean) session.getAttribute("rol");
        Boolean busquedaRealizada = (Boolean) request.getAttribute("busquedaRealizada"); 
        String successMessage = (String) request.getAttribute("successMessage");
        String errorMessage = (String) request.getAttribute("error");
        String mensaje = (String) request.getAttribute("mensaje"); // Obtener el mensaje de la búsqueda
    %>
    <!DOCTYPE html>
    <html lang="es">
    <head>
        <meta charset="UTF-8"> 
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="styles/principal.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <title>GBA - Inicio</title>
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
                        <% if (isAdmin != null && isAdmin) { %>
                            <a href="/principal" class="link">Inicio</a>
                            <a href="/upload" class="link">Subir Legajo</a>
                            <a href="/adduser" class="link">Agregar Usuario</a>
                            <a href="login?action=logout" class="link">Cerrar Sesión</a>
                        <% } else { %>
                            <a href="/principal" class="link">Inicio</a>
                            <a href="/upload" class="link">Subir Legajo</a>
                            <a href="login?action=logout" class="link">Cerrar Sesión</a>
                        <% } %>
                    </nav>
                </div>
                <a href="javascript:void(0);" class="icon" onclick="myFunction()">
                    <i class="fa fa-bars"></i>
                </a>
            </div>    
        </header>

        <main>
            <br><br>
            <section class="search">
                <h2>Buscar Legajo</h2>
                <form action="/principal" method="post">
                    <input type="hidden" name="view" value="transfer">
                    <label for="search-input">Nombre y apellido:</label>
                    <input type="text" id="search-input" name="search-input" placeholder="Buscar..." required>
                    <button type="submit">Buscar</button>
                </form>
            </section>
            <br>

            <!-- Resultados de búsqueda -->
            <section class="results">
                <h2>Resultados:</h2>
                <% 
                    List<Legajo> alumnos = (List<Legajo>) request.getAttribute("alumnos");

                    if (mensaje != null && !mensaje.isEmpty()) {
                %>
                    <p><%= mensaje %></p>
                <% 
                    } else if ((busquedaRealizada != null) && busquedaRealizada && (alumnos == null || alumnos.isEmpty())) { 
                %>
                    <p>No se encontraron alumnos con ese criterio de búsqueda.</p>
                <% 
                    } 
                    else if (alumnos != null && !alumnos.isEmpty()) {
                        for (Legajo alumno : alumnos) {
                %>
                    <br><div class="alumno">
                        <h3><%= alumno.getNombreAlumno() %></h3>
                        <form action="/transfer" method="post">
                            <input type="hidden" name="id" value="<%= alumno.getId() %>">
                            <label for="escuela">Escuela:</label>
                            <select name="escuela" id="escuela" required>
                                <option value="" disabled selected>Selecciona...</option>
                                <% 
                                    int escuelaActual = alumno.getIdEscuela();
                                    String[][] escuelas = {
                                        {"1", "Escuela N°1 \"Pascuala Mugaburu\""},
                                        {"2", "Escuela N°2 \"Republica de Mexico\""},
                                        {"3", "Escuela N°3 \"Alfonsina Storni\""},
                                        {"4", "Escuela N°7 \"Gabriela Mistral\""}
                                    };
                                    for (String[] escuela : escuelas) {
                                        int idEscuela = Integer.parseInt(escuela[0]);
                                        if (idEscuela != escuelaActual) {
                                %>
                                            <option value="<%= idEscuela %>"><%= escuela[1] %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                            <button type="submit">Subir</button>
                        </form>
                    </div><br>
                <% 
                        }
                    }
                %>
            </section>
        </main>

        <footer>
            <p>&copy; 2024 GBA. Todos los derechos reservados.</p>
        </footer>

        <script>
            function myFunction() {
                var x = document.getElementById("myLinks");
                if (x.style.display === "block") {
                    x.style.display = "none";
                } else {
                    x.style.display = "block";
                }
            }
        </script>
    </body>
    </html>
