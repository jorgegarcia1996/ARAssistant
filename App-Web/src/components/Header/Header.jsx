import React from "react";
import "./Header.css";
import icono from "../../assets/images/Icono_persona_arassistant.svg";

class Header extends React.Component {
  render() {
    const {title} = this.props;
    return (
      <div className="Header">
        <a href="./home">
          <img src={icono} alt="ARAssistant"></img>
        </a>
        <h1 className="Header-title">{title}</h1>
      </div>
    );
  }
}

export default Header;