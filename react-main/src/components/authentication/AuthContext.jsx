//import React, {useContext} from "react";
////import AuthCentext from "AuthProvider.jsx";
//
//export const AuthContext = React.createContext();
//export const AuthDispatchContext = React.createContext();
// 
//export default function UseAuth() {
//  const auth = useContext(AuthContext);
//  console.log("useAuth: " + auth);
//  if (auth === undefined) {
//    throw new Error("useAuth must be used within a AuthProvider");
//  }
// 
//  return auth;
//}
//////export function useAuth() {
////export const useAuth = () => {
////  const auth = useContext(AuthContext);
////  console.log("useAuth: " + auth);
////  if (auth === undefined) {
////    throw new Error("useAuth must be used within a AuthProvider");
////  }
//// 
////  return auth;
////}
//// 
//export function useAuthDispatch() {
//  const auth = useContext(AuthDispatchContext);
//  console.log("useAuthDispatch: " + auth);
//  if (auth === undefined) {
//    throw new Error("useAuthDispatch must be used within a AuthProvider");
//  }
// 
//  return auth;
//}
