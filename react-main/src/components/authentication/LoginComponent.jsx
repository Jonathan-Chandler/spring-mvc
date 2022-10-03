import React, { Component } from 'react'
import AuthenticationService from './AuthenticationService.js'

class LoginComponent extends Component {

    constructor(props) {
        super(props)

        this.state = {
            username: 'test_user1',
            password: 'password3',
            loginFailed: false,
        }
        this.handleChange = this.handleChange.bind(this)
        this.loginClicked = this.loginClicked.bind(this)
    }

    handleChange(event) {
        this.setState(
            {
                [event.target.name]
                    : event.target.value
            }
        )
    }

//    useEffect(() => {
//      //Runs on every render
//    });

//    useEffect(() => {
//      //Runs only on the first render
//    }, []);

//  useEffect(() => {
//    setCalculation(() => count * 2);
//  }, [count]); // <- add the count variable here

//    componentDidMount() {
//        ChatAPI.subscribeToFriendStatus(
//            this.props.friend.id,
//            this.handleStatusChange
//        );
//    }
//
//    componentDidUpdate(prevProps) {
//        // Unsubscribe from the previous friend.id    
//        ChatAPI.unsubscribeFromFriendStatus(
//            prevProps.friend.id,
//            this.handleStatusChange    
//        );    
//        // Subscribe to the next friend.id    
//        ChatAPI.subscribeToFriendStatus(
//            this.props.friend.id,
//            this.handleStatusChange
//        );  
//    }
//
//    componentWillUnmount() {
//        ChatAPI.unsubscribeFromFriendStatus(
//            this.props.friend.id,
//            this.handleStatusChange
//        );
//    }

    loginClicked() {
        //AuthenticationService
        if (AuthenticationService.getJwtLoginPromise(this.state.username, this.state.password)) {
            this.props.navigate(`/welcome/${this.state.username}`)
        } else {
            this.setState(this.setState({loginFailed: true}))
        }
        

        //AuthenticationService
        //    .executeJwtAuthenticationService(this.state.username, this.state.password)
        //    .then((response) => {
        //        console.log("receive response.header (string): " + String(response.headers));
        //        console.log("receive response.header: " + JSON.stringify(response.headers));
        //        console.log("receive response.header.Authorization: " + response.headers.Authorization);
        //        console.log("receive response.body (string): " + String(response.body));
        //        console.log("receive response.body: " + JSON.stringify(response.body));
        //        console.log("receive response.body.Authorization: " + response.body.Authorization);
        //        AuthenticationService.registerSuccessfulLoginForJwt(response.headers.Authorization)
        //        this.props.navigate(`/welcome/${this.state.username}`)
        //    }).catch(() => {
        //        console.log("Error from auth service");
        //        this.setState({ loginFailed: true })
        //    })

    }

    render() {
        return (
            <div>
                <h1>Login</h1>
                <div className="container">
                    {/*<ShowInvalidCredentials loginFailed={this.state.loginFailed}/>*/}
                    {this.state.loginFailed && <div className="alert alert-warning">Invalid Credentials</div>}
                    User Name: <input type="text" name="username" value={this.state.username} onChange={this.handleChange} />
                    Password: <input type="password" name="password" value={this.state.password} onChange={this.handleChange} />
                    <button className="btn btn-success" onClick={this.loginClicked}>Login</button>
                </div>
            </div>
        )
    }
}

export default LoginComponent
