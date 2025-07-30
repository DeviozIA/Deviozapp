import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import ProfilePage from "./pages/ProfilePage";
import RegisterPage from "./pages/RegisterPage";


import { AuthProvider, AuthContext } from "./context/AuthContext";
import { useContext } from "react";

function RutasProtegidas({ children }) {
  const { token } = useContext(AuthContext);
  return token ? children : <Navigate to="/" />;
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route
            path="/register"
            element={
              <RutasProtegidas>
                  <RegisterPage />
              </RutasProtegidas>
            }
          />

          <Route
            path="/perfil"
            element={
              <RutasProtegidas>
                <ProfilePage />
              </RutasProtegidas>
            }
          />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
