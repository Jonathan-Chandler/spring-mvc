//import UseAuth, { AuthContext, AuthDispatchContext, useAuthDispatch } from './AuthContext.jsx'
//import { AuthReducer, initialState, AUTH_ACTIONS } from './AuthReducer.jsx'
import React, { ReactNode, useState, useEffect, useMemo, createContext, useContext } from 'react';
import axios, { AxiosDefaults, Axios, AxiosRequestHeaders, AxiosRequestConfig, AxiosInstance, AxiosError, AxiosStatic, AxiosResponse } from 'axios'
import { LOGIN_API_URL } from '../../Constants'

//export const AuthProvider = ({ children }) => {
//  const [auth, dispatch] = useReducer(AuthReducer, initialState);
//  console.log("AuthProvider: " + auth);
// 
//  return (
//    <AuthContext.Provider value={auth}>
//      <AuthDispatchContext.Provider value={dispatch}>
//        {children}
//      </AuthDispatchContext.Provider>
//    </AuthContext.Provider>
//  );
//};

interface AuthContextType {
  // a simple object with name and password.
  username: string;
  token: string;
  apiSession: AxiosDefaults;
  loading: boolean;
  error?: any;
  login: (username: string, password: string) => void;
  signUp: (username: string, password: string) => void;
  logout: () => void;
  getSession: () => Axios;
}

//  apiSession: any;

const AuthContext = createContext<AuthContextType>(
  {} as AuthContextType
);

// Export the provider as we need to wrap the entire app with it
export function AuthProvider({
  children,
}: {
  children: ReactNode;
}): JSX.Element {
  const [username, setUsername] = useState<string>("");
  const [token, setToken] = useState<string>("");
  //const [apiSession, setApiSession] = useState<typeof Axios>(axios.create());
  //const [apiSession, setApiSession] = useState<AxiosRequestConfig>(axios.create());
  //const [apiSession, setApiSession] = useState<AxiosRequestHeaders>(axios.defaults);
  const [apiSession, setApiSession] = useState<AxiosDefaults>(axios.defaults);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<any>();
  const [loadingInitial, setLoadingInitial] = useState<boolean>(true);

  useEffect(() => {
    ////console.log("init authcontext");
    let newApiSession = axios.create();

    ////// update token from response headers with updated auth tokens and handle failed authentication
    ////newApiSession.interceptors.response.use(HandleResponseHeaders, HandleResponseAuthFailure);

    ////// set new axios session
    setApiSession(axios.defaults)

    // initialize done
    setLoadingInitial(false)
  }, []);

  useEffect(() => {
    console.log("use token effect");
    //let newApiSession = axios.create();

    //// update token from response headers with updated auth tokens and handle failed authentication
    //newApiSession.interceptors.response.use(HandleResponseHeaders, HandleResponseAuthFailure);
    //newApiSession.defaults.headers.common['Authorization'] = token;
    //setApiSession(newApiSession);

    let currentSession = axios.create();
    //if (token !== null && token !== "")
    //{
    //  console.log("use token: " + token);
    //  currentSession.defaults.headers.common['Authorization'] = token;
    //}

    // use auth token for all future headers
    setApiSession(axios.defaults);

    //}
    //else
    //{
    //  console.log("fail to set new token: " + token);
    //}
  }, [token]);

  // If we change page, reset the error state.
  //useEffect(() => {
  //  if (error) setError(null);
  //}, [location.pathname]);

  //useEffect(() => {
  //  usersApi.getCurrentUser()
  //    .then((user) => setUser(user))
  //    .catch((_error) => {})
  //    .finally(() => setLoadingInitial(false));
  //}, []);

  // Flags the component loading state and posts the login
  // data to the server.
  //
  // An error means that the email/password combination is
  // not valid.
  //
  // Finally, just signal the component that loading the
  // loading state is over.
  //const handleSubmit = async () => {
  //}

//  const login = async (username: string, password: string) => {
  async function login(username: string, password: string) {
    setLoading(true);
    const userData = {username: username, password: password};
    console.log("username=" + username)
    console.log("password=" + password)

    await axios
      .post(`${LOGIN_API_URL}`, userData)
      .then((response) => {
        // if returned valid auth token then update context with new token/username
        if (response.data.Authorization)
        {
          console.log("success login; token=" + token)
          setToken(response.data.Authorization)

          // set username and login success
          setUsername(username)
          setLoading(false);
        }
      })
      .catch((error) => {
        setUsername("");
        setToken("");
        let errorMessage = "";
        if (error.response) {
          console.log(error.response);
          errorMessage = "ErrorResponse: " + error.request;
        } else if (error.request) {
          console.log("network error");
          errorMessage = "ErrorRequest: " + error.request;
        } else {
          errorMessage = "Error: " + error;
        }

        console.log("Fail login: " + errorMessage);
        setError(errorMessage);
        setLoading(false);
      });
  }

  // Sends sign up details to the server. On success we just apply
  // the created user to the state.
  function signUp(username: string, password: string) {
    setLoading(true);
  }

  // Call the logout endpoint and then remove the user
  // from the state.
  function logout() {
    setUsername("");
    setToken("");
  }

  const HandleResponseHeaders = (response) => {
      if (response.headers.authorization)
      {
          console.log("has auth: " + response.headers.authorization);
          setToken(response.headers.authorization);
      }
      return response;
  }
  
  // handle failed authentication
  const HandleResponseAuthFailure = (error: AxiosResponse) => {
      console.log("handleResponseFailure: " + error);
      //error.toJSON()
  
      // remove token from session if auth rejected
      if (error)
      {
        if (error.status === 401)
        {
            //dispatch(AUTH_ACTIONS.LOGOUT)
          setError("HandleResponseFailure: " + error)

          // force logout on auth failure
          logout();
        }
      }
      return Promise.reject(error);
  }

  function getSession()
  {
    let axiosSession = axios.create()

    // update headers with auth token
    axiosSession.defaults.headers.common['Authorization'] = token;

    // update token from response headers with updated auth tokens and handle failed authentication
    axiosSession.interceptors.response.use(HandleResponseHeaders, HandleResponseAuthFailure);

    return axiosSession;
  }

  // Make the provider update only when it should.
  // We only want to force re-renders if the user,
  // loading or error states change.
  const memoedValue = useMemo(
    () => ({
      username,
      token,
      apiSession,
      loading,
      error,
      login,
      signUp,
      logout,
      getSession,
    }),
    [ username, token, apiSession, loading, error, login, signUp, logout, getSession ]
  );

  // We only want to render the underlying app after we
  // assert for the presence of a current user.
  return (
    <AuthContext.Provider value={memoedValue}>
      {!loadingInitial && children}
    </AuthContext.Provider>
  );
}

// Let's only export the `useAuth` hook instead of the context.
// We only want to use the hook directly and never the context component.
export default function useAuth() {
  return useContext(AuthContext);
}

//// update token from response headers with updated auth tokens
//export const HandleResponseHeaders = (response) => {
//    if (response.headers.authorization)
//    {
//        const token = response.headers.authorization;
//        console.log("has auth: " + token);
//        this.updateAuthorizationToken(token);
//    }
//    return response;
//}
//
//// handle failed authentication
//export const HandleResponseAuthFailure = (error) => {
//    const [state, dispatch] = useReducer(AuthReducer, initialState);
//    console.log("handleResponseFailure: " + error.response);
//
//    // remove token from session if auth rejected
//    if (error.response.status === 401)
//    {
//        dispatch(AUTH_ACTIONS.LOGOUT)
//    }
//    return Promise.reject(error);
//}
//
////export default new AuthenticationService();
//// send login request
//export const DoLogin = async (username, password) => {
//    const [auth, authDispatch] = [UseAuth(), useAuthDispatch()]
//    //const [state, dispatch] = useReducer(AuthReducer, initialState);
//    const userData = {
//        username: username,
//        password: password
//    };
//    await axios
//        .post(`${LOGIN_API_URL}`, userData)
//        .then((response) => {
//            // if returned valid auth token then update context with new token/username
//            if (response.data.Authorization)
//            {
//                const token = response.data.Authorization;
//                console.log("finished login; token=" + token)
//
//                authDispatch(
//                    {type: AUTH_ACTIONS.UPDATE_JWT, payload: {token: token}}
//                );
//
//                // add auth token to all axios headers
//                // use service as a wrapper for axios to handle auth
//                let newSession = axios.create();
//
//                // update token from response headers with updated auth tokens and handle failed authentication
//                newSession.apiSession.interceptors.response.use(HandleResponseHeaders, HandleResponseAuthFailure);
//                    
//                // use auth token for all future headers
//                newSession.apiSession.defaults.headers.common['Authorization'] = this.token;
//
//                // set username and login success
//                authDispatch(
//                    {type: AUTH_ACTIONS.LOGIN_SUCCESS, payload: {username: username, token: token, apiSession: newSession}
//                });
//
//                //// set new token for axios requests
//                //dispatch(
//                //    {type: AUTH_ACTIONS.UPDATE_JWT, payload: {token: token}}
//                //);
//
//                //// set username and login success
//                //dispatch(
//                //    {type: AUTH_ACTIONS.LOGIN_SUCCESS, payload: {username: username}
//                //});
//            }
//        })
//        .catch((error) => {
//            //state.username = "";
//            //state.token = "";
//            let errorMessage = "";
//            if (error.response) {
//                //state.username = username;
//                //state.token = token;
//                //dispatch(
//                //    {type: AUTH_ACTIONS.AUTH_ERROR, payload: {username: username}
//                //});
//                console.log(error.response);
//                errorMessage = "ErrorResponse: " + error.request;
//            } else if (error.request) {
//                console.log("network error");
//                errorMessage = "ErrorRequest: " + error.request;
//            } else {
//                console.log(error);
//                errorMessage = "Error: " + error;
//            }
//            authDispatch(
//                {type: AUTH_ACTIONS.AUTH_ERROR, payload: {errorMessage: errorMessage}
//            });
//        });
//}
//
