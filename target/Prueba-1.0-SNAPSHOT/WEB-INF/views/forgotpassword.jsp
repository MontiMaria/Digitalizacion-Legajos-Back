<%@ page session="true" language="java" contentType="text/html; charset=UTF-8" %>
<% 
    Boolean fail = (Boolean) request.getAttribute("fail");
    String successMessage = (String) request.getAttribute("successMessage");
    String errorMessage = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="ES">
<head>
    <meta charset="UTF-8"> 
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="styles/forgotpassword.css">
    <title>GBA - Recuperar contraseña</title>
    <link rel="icon" href="assets/Logo_PBSAS2.webp" type="image/x-icon">
</head>
<body>
    <div class="nav-wrapper-white">
        <nav>
            <img src="assets/logo.png" alt="logoGBA">
        </nav>
    </div>
    <div class="wrapper">
        <form action="/forgotpassword" id="reset-password-form" method="POST">
            <h1>Olvidaste tu contraseña?</h1>
            <div class="input-box">
                <input type="text" maxlength="8" id="dni" placeholder="Ingresa tu DNI" required>
                <% if(fail != null && fail) { %>
                <p><%=errorMessage %></p>
                <% } %>
            </div><br>
            <div class="remember-forgot">
                <a href="javascript:void(0);" class="btn" id="reset-password-link">Enviar</a>
            </div>
        </form>
    </div>
    <script>
        document.getElementById('reset-password-link').addEventListener('click', function() {
            // Obtener el valor del campo DNI
            var dniValue = document.getElementById('dni').value.trim();

            // Verificar si el campo DNI está vacío
            if (dniValue === "") {
                alert("Por favor, ingresa tu DNI.");
                return;
            }

            // Obtener el formulario
            var form = document.getElementById('reset-password-form');
    
            // Verificar si ya existe un input hidden con name 'dni'
            var dniField = form.querySelector("input[name='dni']");
            if (!dniField) {
                // Crear el campo hidden si no existe
                dniField = document.createElement('input');
                dniField.type = 'hidden';
                dniField.name = 'dni';
                form.appendChild(dniField);
            }
            dniField.value = dniValue; // Actualizar o asignar el valor

            // Enviar el formulario
            form.submit();
        });
    </script>
</body>
</html>
