import React from 'react';
import {useLocalState} from "../util/useLocalStorage";

const Login = () => {
  const [jwt, setJwt] = useLocalState("", "jwt");

  function sendLoginRequest() {
    const reqBody = {
      username: "username",
      password: "pass1"
    }
  };
  
  fetch("api/auth/login", {
    headers: {
      "Content-Type": "application/json",
    },
    method: "post",
    body: JSON.stringify(reqBody),
  })
    .then((response) => Promise.all([response.json(), response.headers])).then
    .then(([body, headers]) => {
      setJwt(headers.get("authorization"));
    });
  return (
    <>
      <div>
        <label htmlFor="username">Username</label>
        <input type="email" id="username" />
      </div>
      <div>
        <label htmlFor="password">Password</label>
        <input type="password" id="password" />
      </div>
      <div>
        <button id="submit" type="button" onClick={() => sendLoginRequest()}>
          Login
        </button>
      </div>
    </>
  )
}
