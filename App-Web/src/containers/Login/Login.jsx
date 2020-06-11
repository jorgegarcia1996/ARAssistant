import React from "react";
import "./Login.css";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import { login } from '../../redux/actions/loginActions';
import { Redirect } from 'react-router-dom';
import { connect } from "react-redux";


class Login extends React.Component {
  state = { email: "", password: ""};
  //Save email in state
  setEmail = (e) => {
    this.setState({ email: e.target.value });
  };

  //Save password in state
  setPassword = (e) => {
    this.setState({ password: e.target.value });
  };

  //Try to login in firebase
  login = () => {
    const { email, password } = this.state;
    if(email !== "" && password !== "") {
      this.props.login(email, password)
    }
  };

  render() {
    if(this.props.isLoggedIn === "true") {
      return (
        <Redirect to="/admin"></Redirect>
      )
    } else {
      return (
        <div className="Login">
          <Header title="Login"/>
          <div className="Main">
            <div className="Form">
              <h1>Login</h1>
              <label for="email">Email</label>
              <input
                placeholder="user@gmail.com"
                type="email"
                onChange={this.setEmail}
                ></input>
              <label for="password">Contraseña</label>
              <input
                placeholder="*****"
                type="password"
                onChange={this.setPassword}
                ></input>
              <button id="login" onClick={this.login}>
                Iniciar sesión
              </button>
            </div>
          </div>
          <Footer />
        </div>
      );
    }
  }
}

function mapState(state) {
  return {
    isLoggedIn: state.userIsLoggedInReducer.isLoggedIn
  }
}

const mapDispatch = {
  login
}

export default connect(mapState, mapDispatch)(Login);
