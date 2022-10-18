import React, { ReactNode, useCallback, useState, useEffect, useMemo, createContext, useContext } from 'react';
import axios, { Axios, AxiosError } from 'axios'
import { LOGIN_API_URL } from '../../Constants'

// atuh context vars/functions
interface AuthContextType {
  username: string;
  token: string;
  loading: boolean;
  error?: any;
  login: (username: string, password: string) => void;
  signUp: (username: string, password: string) => void;
  logout: () => void;
  getSession: () => Axios;
}

// shared context
const AuthContext = createContext<AuthContextType>(
  {} as AuthContextType
);

// Export the provider as we need to wrap the entire app with it
export function AuthProvider({
  children,
}: {
  children: ReactNode;
}): JSX.Element {
  const [username, setUsername] = useState<string>(sessionStorage.getItem("username"));
  const [token, setToken] = useState<string>(sessionStorage.getItem("Authorization"));
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<any>();
  const [loadingInitial, setLoadingInitial] = useState<boolean>(true);

  useEffect(() => {
    // load from session storage or default to empty
    //setToken("");
    //setUsername("");

    // initialize done
    setLoadingInitial(false)
  }, []);

  // send login request
  const login = useCallback(async (username: string, password: string) => {
    setLoading(true);
    const userData = {username: username, password: password};

    await axios
      .post(`${LOGIN_API_URL}`, userData)
      .then((response) => 
      {
        // if returned valid auth token then update context with new token/username
        if (response.data.Authorization)
        {
          console.log("success login; token=" + response.data.Authorization)
          setToken(response.data.Authorization)

          // set username and login success
          setUsername(username)
          sessionStorage.setItem("Authorization", response.data.Authorization);
          sessionStorage.setItem("username", username);
          setError("");
          setLoading(false);
        }
      })
      .catch((error) => {
        // log out and set error response message
        setUsername("");
        setToken("");

        // update error from axios
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
  }, []);

  // create new user
  const signUp = useCallback((username: string, password: string) => {
    setUsername("");
    setToken("");
  }, []);

  // logout and remove username/token
  const logout = useCallback(() => {
    setUsername("");
    setToken("");
  }, []);

  // interceptor updates token from response header
  const handleResponseHeaders = (response) => 
  {
    // update token if exists in response header
    if (response.headers.authorization)
    {
      //console.log("set response header: " + response.headers.authorization);

      // update token for session and local storage
      sessionStorage.setItem("Authorization", response.data.Authorization);
      setToken(response.headers.authorization);
    }

    // return intercepted response to caller
    return response;
  }
  
  // interceptor forces logout if backend returns authentication failure
  const handleResponseAuthFailure = useCallback((error: AxiosError) => 
  {
      console.log("Auth Failure: " + error);
      if (error.response.status === 401)
      {
        console.log("error code Failure: " + error);
        logout();
      }

      // remove token from session if auth rejected
      if (String(error).indexOf("401") !== -1)
      {
        // update error message
        setError("HandleResponseFailure: " + error)

        // force logout on auth failure
        logout();
      }

      // return intercepted error to caller
      return Promise.reject(error);
  }, [logout]);

  // get session with response handlers and auth token header
  const getSession = useCallback(() =>
  {
    let axiosSession = axios.create()

    // add auth token header
    axiosSession.defaults.headers.common['Authorization'] = token;

    // update token from response headers with updated auth tokens and handle failed authentication
    axiosSession.interceptors.response.use(handleResponseHeaders, handleResponseAuthFailure);

    console.log("using auth header token: " + token)

    return axiosSession;
  }, [handleResponseAuthFailure, token]);

  // use memo for AuthContext to reduce rendering
  const memoedValue = useMemo(
    () => ({
      username,
      token,
      loading,
      error,
      login,
      signUp,
      logout,
      getSession,
    }),
    [ username, token, loading, error, login, signUp, logout, getSession ]
  );

  // render components after initialization
  return (
    <AuthContext.Provider value={memoedValue}>
      {!loadingInitial && children}
    </AuthContext.Provider>
  );
}

// only handle context through useAuth
export default function useAuth() {
  return useContext(AuthContext);
}

