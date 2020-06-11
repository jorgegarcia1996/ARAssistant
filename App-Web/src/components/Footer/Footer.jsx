import React from "react";
import "./Footer.css";
import bankia from "../../assets/images/bankia.png";
import fpempresa from "../../assets/images/fpempresa.png";
import iescampanillas from "../../assets/images/iescampanillas.png";

class Footer extends React.Component {
  render() {
    return (
      <div className="Footer">
        <p>
          Desarrolladores: Lino Haller Ríos, Jorge García Molina, Rafael Aragón
          Rodríguez
        </p>
        <p>Coordinador: Moisés Martínez Gutiérrez</p>
        <p>
          <a className="link"
            href="https://github.com/jorgegarcia1996/ARAssistant"
            rel="noopener noreferrer"
            target="_blank"
          >
            Proyecto ARAssistant
          </a>
        </p>
        <a href="https://www.dualizabankia.com/" rel="noopener noreferrer" target="_blank"><img src={bankia} alt="bankia"></img></a>
        <a href="https://fpempresa.net/" rel="noopener noreferrer" target="_blank"><img src={fpempresa} alt="fpempresa" id="fpempresa"></img></a>
        <a href="https://iescampanillas.com/" rel="noopener noreferrer" target="_blank"><img src={iescampanillas} alt="iescampanillas"></img></a>
      </div>
    );
  }
}

export default Footer;
