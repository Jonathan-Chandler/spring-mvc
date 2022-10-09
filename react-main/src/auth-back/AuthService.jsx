//import React, { createContext, useContext, useMemo } from 'react'
//import { useNavigate } from "react-router-dom";
//import { useSessionStorage } from "./UseSessionStorage.jsx";
//
//const AuthContext = createContext();
//
//export const AuthService = ({ component }) => {
//	const [authorization, setAuthorization] = useSessionStorage("Authorization", null);
//	const navigate = useNavigate();
//
//	const login = async (data) => {
//    setAuthorization(data);
//    navigate("/welcome/user");
//  };
//
//  // call this function to sign out logged in user
//  const logout = () => {
//    setAuthorization(null);
//    navigate("/", { replace: true });
//  };
//
//  const value = useMemo(
//    () => ({
//      authorization,
//      login,
//      logout
//    }),
//    [authorization]
//  );
//  return <AuthContext.Provider value={value}>{component}</AuthContext.Provider>;
//};
//
//export const useAuth = () => {
//  return useContext(AuthContext);
//};
//
