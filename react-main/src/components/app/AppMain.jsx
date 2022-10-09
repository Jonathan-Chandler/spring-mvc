import React, {Component} from 'react'

import { BrowserRouter as Router, Redirect, Route, Routes } from 'react-router-dom'

import withNavigation from '../wrappers/WithNavigation.jsx'	
import withParams from '../wrappers/WithParams.jsx'	

import HeaderComponent from '../static/HeaderComponent.jsx'
import FooterComponent from '../static/FooterComponent.jsx'
import routes from '../../routes.js';
import AppRoute from './AppRoute.jsx'
//import IndexComponent from './WelcomeComponent.jsx'
//import IndexComponent from './app/WelcomeComponent.jsx'
import IndexComponent from './IndexComponent.jsx'
import LoginComponent from '../authentication/LoginComponent.jsx'
import LogoutComponent from '../authentication/LogoutComponent.jsx'
import ListTodosComponent from './userpages/ListTodosComponent.jsx'
//import AuthorizedRoute from '../authentication/AuthorizedRoute.jsx'
//import AuthenticatedRoute from '../authentication/AuthenticatedRoute.jsx'
import { AuthRoute } from '../authentication/AuthRoute.jsx'
 
export default function AppMain(props) {
	const HeaderComponentWithNavigation = withNavigation(HeaderComponent);

//      <AuthProvider>
  return (
    <>
    <Router>
      <HeaderComponentWithNavigation/>
      <Routes>
				<Route path="/" element={<LoginComponent />} />
        <Route path="/welcome/:username" element={<IndexComponent />} />
				<Route path="/login" element={<LoginComponent />} />
				<Route path="/logout" element={<LogoutComponent />} />
        <Route path="/todos" 
          element={
              <ListTodosComponent /> 
          }
        />
      </Routes>
      <FooterComponent/>
    </Router>
    </>
  );
}
/////              <ListTodosComponent /> 
/////            </AuthProvider>
////        <Route path="/todos" 
////          element={
////            <AuthProvider>
////            <AuthorizedRoute> 
////              <ListTodosComponent /> 
////            </AuthorizedRoute>
////            </AuthProvider>
////          }
////        />
//        <Route path="/todos" element={
//        <Route path="/todos" element={<ListTodosComponent />} />

//        <Route path="/todos" element={
//            <AuthorizedRoute component=<ListTodosComponent /> />
//          } />

//            <AuthorizedRoute component=<ListTodosComponent {...props} /> />
//            <AuthorizedRoute component={ListTodosComponent} {...props} />
//        <Route path="/todos" element={
//            <AuthorizedRoute component={ListTodosComponent} {...props} />
//          } />

////    <AuthProvider>
////    </AuthProvider>
//        {routes.map((route) => (
//          <Route exact
//            key={route.path}
//            path={route.path}
//            render={(props) /> {
//              return <route.component {...props} />
//            }
//          />
//        ))}
////////////////          <Route exact
////////////////            key={route.path}
////////////////            path={route.path}
////////////////            render={route.component} 
////////////////          />
//          AppRoute(route.path, route.path, route.element, route.authRequired)

//        {routes.map((route) => (
//          AppRoute(route.path, route.path, route.element, route.authRequired)
//        ))}


            //element={<route.element {...props} />}
//          <AppRoute 
//            key={route.path} 
//            path={route.path} 
//            ElementName={route.element} 
//            authRequired={route.authRequired}
//          />

///          <Route
///            key={route.path}
///            path={route.path}
///            element={route.element}
///          />
////class AppMain extends Component {
////export default function AppMain extends Component {
//export default function AppMain(props)
//{
//	//    render() {
//	//const LoginComponentWithNavigation = withNavigation(LoginComponent);
//	const HeaderComponentWithNavigation = withNavigation(HeaderComponent);
//	const WelcomeComponentWithParams = withParams(WelcomeComponent);
//	const ListTodosComponentWithParams = withParams(ListTodosComponent);
//
//	return (
//		<div className="AppMain">
//			<Router>
//				<HeaderComponentWithNavigation/>
//				<Routes>
//					<Route path="/" element={<LoginComponent />} />
//					<Route path="/login" element={<LoginComponent />} />
//					<Route path="/welcome/:name" element={
//						<AuthorizedRoute>
//							<WelcomeComponentWithParams />
//						</AuthorizedRoute>
//						} />
//					<Route path="/todos" element={
//						<AuthorizedRoute>
//							<ListTodosComponentWithParams />
//						</AuthorizedRoute>
//						} />
//					<Route path="/logout" element={
//						<AuthorizedRoute>
//							<LogoutComponent />
//						</AuthorizedRoute>
//						} />
//					<Route path="*" element={<ErrorComponent />} />
//				</Routes>
//				<FooterComponent/>
//			</Router>
//		</div>
//	)
//}

//							<Route path={"/welcome/:name"} element={WelcomeComponent} />

//							{/*<WelcomeComponentWithParams />*/}
//		            	<Route path="/" element={<LoginComponentWithNavigation />} />
//		            	<Route path="/login" element={<LoginComponentWithNavigation />} />
