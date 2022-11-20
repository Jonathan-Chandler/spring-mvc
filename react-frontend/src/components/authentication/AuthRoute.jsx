import React from "react";
import { Navigate } from "react-router-dom";
import useAuth from "./AuthProvider.tsx";

export const AuthRoute = ( {children} ) => {
  const { isAuthenticated } = useAuth();
  //const { token } = useAuth();

  return isAuthenticated() ? children : <Navigate to="/login" />;
}
