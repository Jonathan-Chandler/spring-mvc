import React from "react";
import { Navigate } from "react-router-dom";
import useAuth from "./AuthProvider.tsx";

export const AuthRoute = ( {children} ) => {
  const { token } = useAuth();

  return token ? children : <Navigate to="/login" />;
}
