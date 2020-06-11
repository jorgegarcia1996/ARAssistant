import React from "react";
import "./Home.css";
import Header from "../../components/Header/Header";
import logo from "../../assets/images/logo.svg";
import Footer from "../../components/Footer/Footer";

class Home extends React.Component {
  render() {
    return (
      <div className="Home">
        <Header title="Inicio"/>
        <div className="ButtonMenu">
          <a href="./login" className="adminBtn">
            Admin
          </a>
        </div>
        <img src={logo} alt="icono de ARAssistant" id="logo"></img>
        <div className="Description">
          <p>
            El proyecto consiste en una aplicación Android mediante la cual
            podrémos administrar las gafas de Realidad Aumentada. En esta
            aplicación podemos incluir capas con información o elementos 3D los
            cuales serán superpuestos en el entorno del usuario.
          </p>
          <p>
            La app Android será utilizada como servidor de contenidos para las
            gafas Epson Moverio BT-350 y con servicios para recoger o proveer
            datos a la aplicación instalada en las gafas de Realidad Aumentada.
            Gracias al posicionamiento de las capas, el usuario puede moverse
            libremente y los elementos aparecerás por proximidad, indicando
            tareas, descripciones, recordatorios, notificaciones, etc...
          </p>
        </div>
        <Footer />
      </div>
    );
  }
}

export default Home;
