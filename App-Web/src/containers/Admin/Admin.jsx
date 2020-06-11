import React from "react";
import "./Admin.css";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import Alert from "../../components/Alert/Alert";
import { Table } from "react-bootstrap";
import { getAllUsersList } from "../../redux/actions/databaseActions";
import { logout } from "../../redux/actions/loginActions";
import { connect } from "react-redux";
import { Redirect } from "react-router";

class Admin extends React.Component {
  //Add users to table
  createTable = (size) => {
    let table = [];
    const { fbUsers, uidList } = this.props;
    const fbUsersList = Object.values(fbUsers);
    fbUsersList.forEach((user, i) => {
      let children = [];
      children.push(<td key={user.name}>{user.name}</td>);
      children.push(<td key={user.surname}>{user.surname}</td>);
      children.push(<td key={user.email}>{user.email}</td>);
      children.push(
        <td key="icons" id="icons">
          <Alert userUid={uidList[i]} userData={user} />
        </td>
      );
      table.push(<tr key={"user " + i}>{children}</tr>);
    });
    return table;
  };

  logout = () => {
    this.props.logout();
  };

  componentDidMount() {
    this.props.getAllUsersList();
  }

  render() {
    if (this.props.isLoggedIn === "false") {
      console.log(
        "Admin -> render -> this.props.isLoggedIn",
        this.props.isLoggedIn
      );
      return <Redirect to="/home" />;
    } else {
      let isLoaded = false;
      if (!!this.props.fbUsers) {
        isLoaded = true;
      }
      if (isLoaded) {
        return (
          <div className="Admin">
            <Header title="Usuarios" />
            <div className="ButtonMenu">
              <button className="logoutBtn" onClick={this.logout}>
                Logout
              </button>
            </div>
            <Table className="Table" striped hover responsive>
              <thead>
                <tr>
                  <th id="name">Nombre</th>
                  <th id="surname">Apellidos</th>
                  <th id="email">Mail</th>
                  <th id="actions">Editar</th>
                </tr>
              </thead>
              <tbody>
                {this.createTable(Object.keys(this.props.fbUsers).length)}
              </tbody>
            </Table>
            <Footer />
          </div>
        );
      }
    }
  }
}

function mapState(state) {
  return {
    uidList: state.userListReducer.uidList,
    fbUsers: state.userListReducer.fbUsers,
    isLoggedIn: state.userIsLoggedInReducer.isLoggedIn,
  };
}

const mapDispatch = {
  getAllUsersList,
  logout,
};

export default connect(mapState, mapDispatch)(Admin);
