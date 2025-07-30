import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

export default function ProfilePage() {
  const { user, logout } = useContext(AuthContext);

  if (!user) return <p className="p-4">Cargando...</p>;

  return (
    <div className="p-4">
      <h1 className="text-2xl font-bold">Bienvenido, {user.username}</h1>
      <p>Rol: {user.roles}</p>
 


      <button
        className="mt-4 bg-red-500 text-white px-4 py-2 rounded"
        onClick={logout}
      >
        Cerrar sesión
      </button>
    </div>
  );
}
