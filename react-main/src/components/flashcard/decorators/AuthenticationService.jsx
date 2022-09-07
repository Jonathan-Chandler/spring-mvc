
class AuthenticationService {
  registerSuccessfulLogin(username, password){
    //console.log("logged in user")
    sessionStorage.setItem('authenticatedUser', username);
  }

  logout(){
    //console.log("logout user")
    sessionStorage.removeItem('authenticatedUser');
  }

  isUserLoggedIn(){
    let user = sessionStorage.getItem('authenticatedUser');
    if (user===null)
      return false;
    //console.log("user is logged in")
    return true;
  }
}

export default new AuthenticationService()
