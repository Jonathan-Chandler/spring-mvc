import moment from 'moment'
import React, { useEffect, useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from '../../authentication/AuthProvider.tsx';
import { PLAYER_LIST_API_URL } from '../../../Constants';

export default function TictactoePlayerList()
{
    const navigate = useNavigate();
    const { isAuthenticated, getSession, username } = useAuth();
    const [state, setState] = useState({
      players: [],
      message: "",
      loadedPlayers: false,
    });

    const getPlayers = useCallback(async () =>
    {
        let axiosSession = getSession();
        const data = await axiosSession.get(PLAYER_LIST_API_URL)
        .then( response => 
        {
			console.log("response: " + response);
            return response.data;
        }
        )
        .catch(err => {
			console.log("error: " + err);
            return null
        })

        if (data !== null)
        {
            setState({players: data});
        }
    }, [getSession]);

    useEffect(() => 
    {
        // check if auth
        if (!isAuthenticated())
        {
            navigate("/login");
        }
    }, [navigate, isAuthenticated]);

    useEffect(() => 
    {
        // empty players
        if (state.loadedPlayers === false)
        {
            console.log("useEffect(getplayers)")
            // async update players list
            getPlayers();
            setState({...state, loadedPlayers: true});
        }

    }, [getPlayers, getSession, state]);

	const refreshPlayersClicked = () =>
	{
		getPlayers();
	}

	const requestMatchClicked = (username) =>
	{
		let thisUser = username
	}

    if (state.players)
    {
        return (
            <div>
                <h1>Player List</h1>
                {state.message && <div class="alert alert-success">{state.message}</div>}
                <div className="container">
                    <table className="table">
                        <thead>
                            <tr>
                                <th>Username</th>
                                <th>IsRequesting</th>
                                <th>YouRequested</th>
                                <th>InitiateRequest</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                state.players.map(
                                    player =>
                                        <tr key={player.id}>
                                            <td>{player.username}</td>
                                            <td>{player.theirRequest ? "Yes" : "No"}</td>
											<td>{player.myRequest ? "Yes" : "No"}</td>
                                            <td>
												{!player.theirRequest && <button className="btn btn-warning" onClick={() => requestMatchClicked(player.username)}>Request</button>}
												{player.theirRequest && <button className="btn btn-warning" onClick={() => requestMatchClicked(player.username)}>Accept Request</button>}
											</td>
                                        </tr>
                                )
                            }
                        </tbody>
                    </table>
					<button className="btn btn-success" onClick={refreshPlayersClicked}>Update</button>
                </div>
            </div>
        )
    }
    else
        return (
            <>
                <div className="container">
					<p>No other players are online</p>
					<p><button className="btn btn-success" onClick={refreshPlayersClicked}>Update Player List</button></p>
                </div>
            </>
        )        
}

