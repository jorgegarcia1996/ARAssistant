import firebase from "firebase";
import firebaseConfig from "../../firebaseConfig";

export const LOGIN = "LOGIN";

export const loginSuccess = (isLoggedIn) => ({
    type: LOGIN,
    payload: {isLoggedIn}
});

export function login(email, password) {
    return dispatch => {
        let isLoggedIn = "false"
         if (!firebase.apps.length) {
            firebase.initializeApp(firebaseConfig);
        } 
        firebase.auth().signInWithEmailAndPassword(email, password)
            .then(function (user) {
                isLoggedIn = "true"
                return dispatch(loginSuccess(isLoggedIn));
            }).catch(function (error) {
                console.log(error.message);
            });
    }
}

export function logout() {
    return dispatch => {
        let isLoggedIn = "true"
        if (!firebase.apps.length) {
            firebase.initializeApp(firebaseConfig);
        }
        firebase.auth().signOut()
            .then(function () {
                isLoggedIn = "false"
                return dispatch(loginSuccess(isLoggedIn));
            }).catch(function (error) {
                console.log(error.message);
            });

    }
}