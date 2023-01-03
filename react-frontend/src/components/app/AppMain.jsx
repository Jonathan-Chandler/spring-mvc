import React, { useEffect, useState } from 'react'
//import io from 'socket.io-client';
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router-dom'
import HeaderComponent from '../static/HeaderComponent.jsx'
import FooterComponent from '../static/FooterComponent.jsx'
import LoginComponent from '../authentication/LoginComponent.jsx'
import LogoutComponent from '../authentication/LogoutComponent.jsx'
import WelcomeComponent from './WelcomeComponent.jsx'
import TictactoeGameSelect from './tictactoe/TictactoeGameSelect.jsx'
import LocalGame from './tictactoe/game/LocalGame.jsx'
import AiGame from './tictactoe/game/AiGame.jsx'
import OnlineGame from './tictactoe/game/OnlineGame.jsx'
import TictactoePlayerList from './tictactoe/TictactoePlayerList.jsx'
import RegisterComponent from '../authentication/RegisterComponent.jsx'
import { AuthProvider } from '../authentication/AuthProvider.tsx'
import { TictactoeProvider } from './tictactoe/TictactoeProvider.tsx'
import 'bootstrap/dist/css/bootstrap.css';
import '../css/AppMain.css';

export default function AppMain(...props) 
{
	return (
		<div className="App">
			<AuthProvider>
				<Router>
					<HeaderComponent/>
					<Routes>
						<Route exact path="/" element={<Navigate to="/welcome" />} />
						<Route exact path="/welcome" element={<WelcomeComponent />} />
						<Route exact path="/tictactoe" element={<TictactoeGameSelect />} />
						<Route exact path="/tictactoe/game/local" element={<LocalGame />} />
						<Route exact path="/tictactoe/game/ai" element={<AiGame />} />
						<Route exact path="/tictactoe/game/online" element={<OnlineGame />} />
						<Route exact path="/tictactoe/playerlist" element={<TictactoePlayerList />} />
						<Route exact path="/register" element={<RegisterComponent />} />
						<Route exact path="/login" element={<LoginComponent />} />
						<Route exact path="/logout" element={<LogoutComponent />} />
					</Routes>
					<FooterComponent/>
				</Router>
			</AuthProvider>
		</div>
	);
}

//	return (
//		<div className="App">
//			<AuthProvider>
//			<TictactoeProvider>
//				<Router>
//					<HeaderComponent/>
//					<Routes>
//						<Route exact path="/" element={<Navigate to="/welcome" />} />
//						<Route exact path="/welcome" element={<WelcomeComponent />} />
//						<Route exact path="/tictactoe" element={<TictactoeGameSelect />} />
//						<Route exact path="/tictactoe/game/local" element={<LocalGame />} />
//						<Route exact path="/tictactoe/game/ai" element={<AiGame />} />
//						<Route exact path="/tictactoe/game/online" element={<OnlineGame />} />
//						<Route exact path="/tictactoe/playerlist" element={<TictactoePlayerList />} />
//						<Route exact path="/register" element={<RegisterComponent />} />
//						<Route exact path="/login" element={<LoginComponent />} />
//						<Route exact path="/logout" element={<LogoutComponent />} />
//					</Routes>
//					<FooterComponent/>
//				</Router>
//			</TictactoeProvider>
//			</AuthProvider>
//		</div>
//	);
