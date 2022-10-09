import React, {Component} from 'react'

import { NavLink, Link } from 'react-router-dom'
//import UseAuth from '../authentication/AuthContext.jsx'

export default function HeaderComponent(...params) {
    //const auth = UseAuth();
    ////const isUserLoggedIn = AuthenticationService.isUserLoggedIn();
    //console.log(isUserLoggedIn);

    return (
        <header>
            <nav className="navbar navbar-expand-md navbar-dark bg-dark">
                <div><a href="http://127.0.0.1" className="navbar-brand">Something</a></div>
                <ul className="navbar-nav">
                    {<li><NavLink className="nav-link" to="/">Home</NavLink></li>}
                    {<li><NavLink className="nav-link" to="/todos">Todos</NavLink></li>}
                    {<li><NavLink className="nav-link" to="/login">Login</NavLink></li>}
                    {<li><NavLink className="nav-link" to="/logout">Logout</NavLink></li>}
                </ul>
            </nav>
        </header>
    )
    //<ul className="navbar-nav navbar-collapse justify-content-end">
    //    {!isUserLoggedIn && <li><Link className="nav-link" to="/login">Login</Link></li>}
    //    {isUserLoggedIn && <li><Link className="nav-link" to="/logout" onClick={AuthenticationService.logout}>Logout</Link></li>}
    //</ul>
    //return (
    //    <header>
    //        <nav className="navbar navbar-expand-md navbar-dark bg-dark">
    //            <div><a href="http://127.0.0.1" className="navbar-brand">Something</a></div>
    //            <ul className="navbar-nav">
    //                {isUserLoggedIn && <li><Link className="nav-link" to="/">Home</Link></li>}
    //                {isUserLoggedIn && <li><Link className="nav-link" to="/todos">Todos</Link></li>}
    //            </ul>
    //            <ul className="navbar-nav navbar-collapse justify-content-end">
    //                {!isUserLoggedIn && <li><Link className="nav-link" to="/login">Login</Link></li>}
    //                {isUserLoggedIn && <li><Link className="nav-link" to="/logout" onClick={AuthenticationService.logout}>Logout</Link></li>}
    //            </ul>
    //        </nav>
    //    </header>
    //)
}

//export default HeaderComponent
//class HeaderComponent extends Component {
//    render() {
//        ////const isUserLoggedIn = AuthenticationService.isUserLoggedIn();
//        //console.log(isUserLoggedIn);
//
//        return (
//            <header>
//                <nav className="navbar navbar-expand-md navbar-dark bg-dark">
//                    <div><a href="http://127.0.0.1" className="navbar-brand">Something</a></div>
//                    <ul className="navbar-nav">
//                        {<li><Link className="nav-link" to="/">Home</Link></li>}
//                        {<li><Link className="nav-link" to="/todos">Todos</Link></li>}
//                    </ul>
//                </nav>
//            </header>
//        )
//                    //<ul className="navbar-nav navbar-collapse justify-content-end">
//                    //    {!isUserLoggedIn && <li><Link className="nav-link" to="/login">Login</Link></li>}
//                    //    {isUserLoggedIn && <li><Link className="nav-link" to="/logout" onClick={AuthenticationService.logout}>Logout</Link></li>}
//                    //</ul>
//        //return (
//        //    <header>
//        //        <nav className="navbar navbar-expand-md navbar-dark bg-dark">
//        //            <div><a href="http://127.0.0.1" className="navbar-brand">Something</a></div>
//        //            <ul className="navbar-nav">
//        //                {isUserLoggedIn && <li><Link className="nav-link" to="/">Home</Link></li>}
//        //                {isUserLoggedIn && <li><Link className="nav-link" to="/todos">Todos</Link></li>}
//        //            </ul>
//        //            <ul className="navbar-nav navbar-collapse justify-content-end">
//        //                {!isUserLoggedIn && <li><Link className="nav-link" to="/login">Login</Link></li>}
//        //                {isUserLoggedIn && <li><Link className="nav-link" to="/logout" onClick={AuthenticationService.logout}>Logout</Link></li>}
//        //            </ul>
//        //        </nav>
//        //    </header>
//        //)
//    }
//}
//
//export default HeaderComponent
