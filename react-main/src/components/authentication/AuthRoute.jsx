import React, { Fragment } from "react";
import { Navigate } from "react-router-dom";
//import { useAuth } from "./AuthService.jsx";
//import { useAuth } from "./AuthContext.jsx";
import { BrowserRouter as Router, Redirect, Route, Routes } from 'react-router-dom'

//export const AuthorizedRoute = ({ component, ...props }) => {
//export const AuthorizedRoute = ({ component: Component, ...rest }) => {
//export default function AuthorizedRoute({ component: Component, ...rest }) {
//export default function AuthorizedRoute( props ) 
//export const AuthorizedRoute = ( props ) => (
//    <Fragment>
//      {props.children}
//    </Fragment>
//  )
export const AuthRoute = ( {children} ) => {
  const authorized = true;

  return authorized ? children : <Navigate to="/" />;
}
