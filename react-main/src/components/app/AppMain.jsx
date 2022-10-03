import React, {Component} from 'react'

import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'

import withNavigation from '../wrappers/WithNavigation.jsx'	
import withParams from '../wrappers/WithParams.jsx'	

import HeaderComponent from '../static/HeaderComponent.jsx'
import FooterComponent from '../static/FooterComponent.jsx'
import AuthenticatedRoute from '../authentication/AuthenticatedRoute.jsx'
import LoginComponent from '../authentication/LoginComponent.jsx'
import ErrorComponent from './ErrorComponent.jsx'
import LogoutComponent from '../authentication/LogoutComponent.jsx'
import WelcomeComponent from './WelcomeComponent.jsx'
import ListTodosComponent from './userpages/ListTodosComponent.jsx'

class AppMain extends Component {
    render() {
    	const LoginComponentWithNavigation = withNavigation(LoginComponent);
    	const HeaderComponentWithNavigation = withNavigation(HeaderComponent);
    	const WelcomeComponentWithParams = withParams(WelcomeComponent);
    	const ListTodosComponentWithParams = withParams(ListTodosComponent);

        return (
            <div className="AppMain">
            	<Router>
            		<HeaderComponentWithNavigation/>
            		<Routes>
		            	<Route path="/" element={<LoginComponentWithNavigation />} />
		            	<Route path="/login" element={<LoginComponentWithNavigation />} />
		            	<Route path="/welcome/:name" element={
		            		<AuthenticatedRoute>
		            			<WelcomeComponentWithParams />
		            		</AuthenticatedRoute>
		            	} />
		            	<Route path="/todos" element={
		            		<AuthenticatedRoute>
		            			<ListTodosComponentWithParams />
		            		</AuthenticatedRoute>
		            	} />
		            	<Route path="/logout" element={
		            		<AuthenticatedRoute>
		            			<LogoutComponent />
		            		</AuthenticatedRoute>
		            	} />
		            	<Route path="*" element={<ErrorComponent />} />
	            	</Routes>
	            	<FooterComponent/>
            	</Router>
            </div>
        )
    }
}

export default AppMain

