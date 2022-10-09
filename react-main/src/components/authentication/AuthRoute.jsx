import React, { Fragment } from "react";
import { Navigate } from "react-router-dom";
import { BrowserRouter as Router, Redirect, Route, Routes } from 'react-router-dom'
import useAuth from "./AuthProvider.tsx";

export const AuthRoute = ( {children} ) => {
  const { username, token, apiSession, loading, error, login, signUp, logout } = useAuth();

  return token ? children : <Navigate to="/login" />;
}
