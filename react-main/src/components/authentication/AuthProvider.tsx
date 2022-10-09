import React, { ReactNode, useState, useEffect, useMemo, createContext, useContext } from 'react';
import axios, { AxiosDefaults, Axios, AxiosRequestHeaders, AxiosRequestConfig, AxiosInstance, AxiosError, AxiosStatic, AxiosResponse } from 'axios'
import { LOGIN_API_URL } from '../../Constants'

// atuh context vars/functions
interface AuthContextType {
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
  const [apiSession, setApiSession] = useState<AxiosDefaults>(axios.defaults);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<any>();
  const [loadingInitial, setLoadingInitial] = useState<boolean>(true);

  useEffect(() => {
    // load from session storage or default to empty
    //setToken("");
    //setUsername("");

    let newApiSession = axios.create();

    // axios defaults
    setApiSession(axios.defaults)

    // initialize done
    setLoadingInitial(false)
  }, []);

  useEffect(() => {
    console.log("Update token");
    let currentSession = axios.create();

    // use auth token for all future headers
    setApiSession(axios.defaults);

  }, [token]);

  // send login request
  async function login(username: string, password: string) {
    setLoading(true);
    const userData = {username: username, password: password};

    await axios
      .post(`${LOGIN_API_URL}`, userData)
      .then((response) => 
      {
        // if returned valid auth token then update context with new token/username
        if (response.data.Authorization)
        {
          console.log("success login; token=" + token)
          setToken(response.data.Authorization)

          // set username and login success
          setUsername(username)
          sessionStorage.setItem("Authorization", token);
          sessionStorage.setItem("username", username);
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
  }

  // new user
  function signUp(username: string, password: string) {
    setLoading(true);
  }

  // logout and remove username/token
  function logout() {
    setUsername("");
    setToken("");
  }

  const HandleResponseHeaders = (response) => 
  {
    // update token if exists in response header
    if (response.headers.authorization)
    {
      setToken(response.headers.authorization);
    }

    return response;
  }
  
  // handle failed authentication
  const HandleResponseAuthFailure = (error: AxiosResponse) => 
  {
      // remove token from session if auth rejected
      if (String(error).indexOf("401") !== -1)
      {
        // update error message
        setError("HandleResponseFailure: " + error)

        // force logout on auth failure
        logout();
      }

      return Promise.reject(error);
  }

  // return session with response handlers and auth token
  function getSession()
  {
    let axiosSession = axios.create()

    // update headers with auth token
    axiosSession.defaults.headers.common['Authorization'] = token;

    // update token from response headers with updated auth tokens and handle failed authentication
    axiosSession.interceptors.response.use(HandleResponseHeaders, HandleResponseAuthFailure);

    return axiosSession;
  }

  // use memo for AuthContext
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

