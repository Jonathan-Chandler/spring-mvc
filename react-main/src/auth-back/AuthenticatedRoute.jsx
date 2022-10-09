import React, { Component } from 'react'
import { Navigate } from 'react-router-dom'
import AuthenticationService from './AuthenticationService.jsx'

export default function AuthenticatedRoute(props)
{
	if (AuthenticationService.isUserLoggedIn()) 
	{
		return {...props.children}
	} else {
		return <Navigate to="/login" />
	}
}

