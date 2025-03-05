<!DOCTYPE html>
<html lang="ES">
<head>
    <meta charset="UTF-8"> 
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width-device-width,initial-scale=1.0">
    <link rel="stylesheet" href="styles/forgotpassword.css">
    <title>GBA - Recuperar contrase�a</title>
    <link rel="icon" href="assets/Logo_PBSAS2.webp" type="image/x-icon">
</head>
<body>
    <div class="nav-wrapper-white">
        <nav>
            <img src="assets/logo.png" alt="logoGBA">
        </nav>
    </div>
    <div class="wrapper">
        <form action="/renamepassword" id="reset-password-form" method="post">
            <h1>Olvidaste tu contrase�a?</h1>
            <input type="hidden" name="dni" value="${dni}">
            <div class="input-box">
                <input type="password" id="new-password" name="new-password" placeholder="Contrase�a nueva" required>
            </div>
            <div class="input-box">
                <input type="password" id="confirm-password" name="confirm-password" placeholder="Confirmar contrase�a" required>
            </div>
            <div class="remember-forgot">
                <a href="javascript:void(0);" class="btn" id="reset-password-link">Resetear Contrase�a</a>
            </div>
        </form>
    </div>
    <script>
        document.getElementById('reset-password-link').addEventListener('click', function() {

            var form = document.getElementById('reset-password-form');
            var newPassword = document.getElementById('new-password').value;
            var confirmPassword = document.getElementById('confirm-password').value;

            if (form.checkValidity()) {
                if (newPassword === confirmPassword) {
                    form.submit(); // Env�a el formulario al backend
                } else {
                    alert('Las contrase�as no coinciden.');
                }
            } else {
                form.reportValidity(); // Muestra mensajes de validaci�n
            }
        });
    </script>
</body>
</html>