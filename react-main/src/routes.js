// Config/routes.js
import LoginComponent from './components/authentication/LoginComponent.jsx'
//import ErrorComponent from './components/app/ErrorComponent.jsx'
import LogoutComponent from './components/authentication/LogoutComponent.jsx'
//import { AuthorizedRoute } from './components/authentication/AuthorizedRoute.jsx'
import WelcomeComponent from './components/app/WelcomeComponent.jsx'
import ListTodosComponent from './components/app/userpages/ListTodosComponent.jsx'
import IndexComponent from './components/app/WelcomeComponent.jsx'
 
const routes =[
  {
    path:"/",
    element: IndexComponent,
    //element: <IndexComponent />,
    authRequired: false,
  },
  //{
  //  path:'/',
  //  element: LoginComponent,
  //  authRequired: false,
  //},
  //{
  //  path:'/login',
  //  element: LoginComponent,
  //  authRequired: false,
  //},
  //{
  //  path:'/logout',
  //  element: LogoutComponent,
  //  authRequired: false,
  //},
  ////{
  ////  path:'/todos',
  ////  element: ListTodosComponent,
  ////  authRequired: true,
  ////},
  ////{
  ////  path:'/welcome/:name',
  ////  element: WelcomeComponent,
  ////  authRequired: true,
  ////},
  //{
  //  path:'/*',
  //  component: PageNotFound
  //},
]
 
export default routes
