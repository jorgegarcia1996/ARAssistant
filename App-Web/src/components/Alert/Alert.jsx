import React, { useState } from "react";
import "./Alert.css";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Dialog from "@material-ui/core/Dialog";
import DialogActions from "@material-ui/core/DialogActions";
import DialogContent from "@material-ui/core/DialogContent";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContentText from "@material-ui/core/DialogContentText";
import { FaPencilAlt } from "react-icons/fa";
import firebase from "firebase";

export default function FormDialog(props) {


  const [open, setOpen] = useState(false);

  const [userUid] = useState(props.userUid);
  const [userData] = useState(props.userData);

  const [name, setName] = useState(userData.name);
  const [surname, setSurname] = useState(userData.surname);
  
  const handleClickOpen = () => {
    setOpen(true);
  };
  
  //Edit user from fbdb
  const handleClose = (uid) => {
    let ref =
    typeof uid == "string" ? firebase.database().ref("/user") : null
    if(!!ref) {
          ref.child(uid).update({ 
          'name': name, 
          'surname': surname  
        })
      }
    setOpen(false);
  };

  return (
    <div className="Alert">
      <FaPencilAlt size="20px" id="edit" onClick={handleClickOpen} />
      <Dialog
        maxWidth='lg'
        open={open}
        onClose={handleClose}
        aria-labelledby="form-dialog-title"
      >
        <DialogTitle id="form-dialog-title">Editar usuario: {userData.email}</DialogTitle>
        <DialogContent className="dialog-content">
          <DialogContentText>
            Introduce el nuevo nombre o apellidos del usuario.  
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="name"
            label="Nombre"
            type="text"
            fullWidth
            onChange={(event) => setName(event.target.value)}
          />
          <TextField
            margin="dense"
            id="surname"
            label="Apellidos"
            type="text"
            fullWidth
            onChange={(event) => setSurname(event.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Cancelar
          </Button>
          <Button
            onClick={() => handleClose(userUid)}
            color="primary"
          >
            Aceptar
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
