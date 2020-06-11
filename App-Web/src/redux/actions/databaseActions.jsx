import firebase from "firebase";
import firebaseConfig from "../../firebaseConfig";

export const GET_ALL_USERS = "GET_ALL_USERS_SUCCESS";

export const getAllUsersSuccess = (fbUsers, uidList) => ({
    type: GET_ALL_USERS,
    payload: { fbUsers, uidList }
});

export function getAllUsersList() {
    return dispatch => { 
        if (!firebase.apps.length) {
            firebase.initializeApp(firebaseConfig);
        }
        let fbUsers;
        let uidList;
        let ref = firebase.database().ref("/user");
        ref.on("value", (snapshot) => {
            uidList = []
            fbUsers = Object.keys(snapshot.val()).map((item, i) => {
                uidList.push(item);
            });
            fbUsers = snapshot.val();
            return dispatch(getAllUsersSuccess(fbUsers, uidList))
        });
        
    }
}