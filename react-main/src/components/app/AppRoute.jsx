//import React from "react";
//import { Route, useOutlet } from "react-router-dom";
//import { Navigate } from 'react-router-dom'
// 
//import { useAuth } from '../authentication/AuthContext.jsx'
//import LoginComponent from '../authentication/LoginComponent.jsx'
// 
//const AppRoute = (key, path, ElementName, authRequired, ...props) => {
//    const outlet = useOutlet();
// 
////    const userDetails = useAuth()
//
//    if (authRequired)
//    {
//        console.log("authrequired for " + path);
//        return props => (
//            <Route
//                key={key}
//                path={path}
//                element=<ElementName {...props} />
//            />
//        )
//    }
//    else
//    {
//        console.log("not required for " + path);
//        // send to login if not authenticated
//        return (
//            <Route
//                key={key}
//                path={path}
//                element=<Navigate to="/login" />
//            />
//        )
//    }
//}
//// return props => <Component {...props} params={useParams()} />;
//                //element=<LoginComponent {...props} />
// 
////            element={props =>
////                    <ElementName {...props} />
//export default AppRoute
////            element={props =>
////                authRequired && !Boolean(userDetails.token) ? (
////                    <Navigate to="/login" />
////                ) : (
////                    <Component {...props} />
////                    )
////            }
