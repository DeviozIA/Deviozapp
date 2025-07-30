import { useState, useContext } from "react";
import axios from "../service/api";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
// 'tailwindcss' no necesita ser importado directamente en JSX,
// solo asegúrate de que esté configurado en tu proyecto.

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(""); // Limpiar errores previos
    try {
      const res = await axios.post("/auth/login", { username, password });
      login(res.data.token);
      navigate("/perfil"); // Redirigir a la página de perfil en caso de éxito
    } catch (err) {
      setError("Credenciales inválidas. Por favor, verifica tu usuario y contraseña.");
      console.error("Error de inicio de sesión:", err); // Para depuración
    }
  };

  return (
    <div className="login login-v2 fw-bold min-h-screen flex items-center justify-center relative overflow-hidden">
      {/* BEGIN login-cover (Adaptado con Tailwind para simular) */}
      <div className="login-cover absolute inset-0">
        <div
          className="login-cover-img absolute inset-0 bg-cover bg-center"
          style={{ backgroundImage: 'url(../assets/img/login-bg/login-bg-17.jpg)' }}
          data-id="login-cover-image"
        ></div>
        <div className="login-cover-bg absolute inset-0 bg-gradient-to-br from-blue-600 to-blue-900 opacity-75"></div>
      </div>
      {/* END login-cover */}

      {/* BEGIN login-container */}
      <div className="login-container relative z-10 bg-white p-6 md:p-8 rounded-lg shadow-2xl w-full max-w-sm md:max-w-md animate-fade-in-up">
        {/* BEGIN login-header */}
        <div className="login-header flex justify-between items-center mb-6">
          <div className="brand flex items-center">
            <div className="d-flex align-items-center text-gray-800 text-3xl font-bold">
              <span className="logo inline-block w-8 h-8 mr-2 bg-blue-500 rounded-md"></span>{" "}
              <b>Color</b> Admin
            </div>
            <small className="block text-gray-500 text-sm mt-1">Bootstrap 5 Responsive Admin Template</small>
          </div>
          <div className="icon text-gray-500 text-3xl">
            <i className="fa fa-lock"></i> {/* Asegúrate de que Font Awesome esté enlazado en tu index.html */}
          </div>
        </div>
        {/* END login-header */}

        {/* BEGIN login-content */}
        <div className="login-content">
          <form onSubmit={handleSubmit}>
            <div className="form-floating mb-4 relative">
              <input
                type="text"
                className="form-control w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm h-12"
                placeholder=" " // Espacio para que el label flote
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
              <label
                htmlFor="username"
                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-500 text-sm transition-all duration-200 peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-focus:top-3 peer-focus:-translate-y-0 peer-focus:text-xs peer-focus:text-blue-600"
              >
                Usuario
              </label>
            </div>
            <div className="form-floating mb-4 relative">
              <input
                type="password"
                className="form-control w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm h-12"
                placeholder=" " // Espacio para que el label flote
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <label
                htmlFor="password"
                className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-500 text-sm transition-all duration-200 peer-placeholder-shown:top-1/2 peer-placeholder-shown:-translate-y-1/2 peer-focus:top-3 peer-focus:-translate-y-0 peer-focus:text-xs peer-focus:text-blue-600"
              >
                Contraseña
              </label>
            </div>
            {/* Checkbox "Remember Me" - opcional, no tiene lógica en el backend actual */}
            <div className="form-check mb-4 flex items-center">
              <input
                className="form-check-input h-4 w-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500 mr-2"
                type="checkbox"
                value="1"
                id="rememberMe"
              />
              <label className="form-check-label text-sm text-gray-600" htmlFor="rememberMe">
                Recuérdame
              </label>
            </div>
            <div className="mb-4">
              <button
                type="submit"
                className="btn btn-theme w-full h-12 bg-blue-600 text-white font-semibold py-2 rounded-md hover:bg-blue-700 transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                Iniciar sesión
              </button>
            </div>
            {error && <p className="text-red-500 text-sm mt-3 text-center animate-fade-in">{error}</p>}
            <div className="text-gray-600 text-center text-sm mt-4">
              ¿Aún no eres miembro? Haz clic{" "}
              <a href="/register" className="text-blue-600 hover:underline">
                aquí
              </a>{" "}
              para registrarte.
            </div>
          </form>
        </div>
        {/* END login-content */}
      </div>
      {/* END login-container */}
    </div>
  );
}