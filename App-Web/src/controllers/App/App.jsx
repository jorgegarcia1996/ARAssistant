import React from "react";
import "./App.css";
import { Route, Redirect, Switch } from "react-router-dom";
import { withRouter } from "react-router";
import Home from "../../containers/Home/Home";
import Login from "../../containers/Login/Login";
import Admin from "../../containers/Admin/Admin";

class App extends React.Component {
  render() {
    return (
      <div className="App">
          <div>
            <Switch key="switch">
                <Route key="home" path="/home" exact component={Home} />
                <Route key="login" path="/login" exact component={Login} />
                <Route key="admin" path="/admin" exact component={Admin} />
                <Redirect from="*" to="/home" />
            </Switch>
          </div>
      </div>
    );
  }
}

export default withRouter(App);
