Interfaz cliente sobre servidor tomcat. 
Formulario que hace post a servicio REST para hacer un insert en la base de datos.
La contraseña se encripta con la funcion SHA-1 antes de ingresarla a base de datos.
El servicio inserta al usuario inactivo, con un token y con un tiempo de expiracion para validarlo. 
A la vez envia un correo con un enlace donde se aplica el token correspondiente como parametro.
Si todo va correcto, tanto la contraseña, como el correo y el token, el usuario será dado de alta como usuario activo en la base de datos.
