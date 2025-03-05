<%@ page session="true" language="java" contentType="text/html; charset=UTF-8" %>
<% 
    Boolean fail = (Boolean) request.getAttribute("fail");
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("error");
    String errorWindow = (String) request.getAttribute("errorRN");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styles/login.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <title>GBA - Iniciar Sesión</title>
    <link rel="icon" href="assets/Logo_PBSAS2.webp" type="image/x-icon">
    <!-- Incluimos SweetAlert2 -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
    <div class="nav-wrapper-white">
        <nav>
            <img src="assets/logo.png" alt="logoGBA">
        </nav>
    </div>
    <div class="wrapper">
        <form action="/login" method="post">
            <h1>Bienvenido</h1>
            <div class="input-box">
                <input type="text" id="usuario" name="usuario" placeholder="DNI" maxlength="8" required>
                <i class='bx bxs-id-card'></i>
            </div>
            <div class="input-box">
                <input type="password" id="password" name="password" placeholder="Contraseña" required>
                <i class='bx bxs-lock-alt'></i>
            </div>
            <% if(fail != null && fail) { %>
                <p><%=errorMessage %></p>
            <% } %>
            <div class="remember-forgot">
                <label><input type="checkbox" name="remember_me"> Recordarme</label>
                <a href="/forgotpassword" id="forgotpassw">¿Has olvidado la contraseña?</a>
            </div>
            <button type="submit" class="btn">Login</button>
        </form>
    </div>
    <script>
        <% if(successMessage != null) { %>
            Swal.fire({
                title: 'Éxito',
                text: '<%= successMessage %>',
                icon: 'success',
                confirmButtonText: 'Vale'
            });
        <% } %>
        <% if(errorWindow != null) { %>
            Swal.fire({
              title: 'Error',
              text: '<%= errorWindow %>',
              icon: 'error',
              confirmButtonText: 'Vale'
            });
        <% } %>
    </script>
</body>
</html>
