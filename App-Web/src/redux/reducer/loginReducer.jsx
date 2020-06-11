import {
    LOGIN
} from '../actions/loginActions';

const initialState = {
    isLoggedIn: localStorage.getItem("isLoggedIn")
}

export default function userIsLoggedInReducer(state = initialState, action) {
    switch(action.type) {
        case LOGIN:
            localStorage.setItem("isLoggedIn", action.payload.isLoggedIn)
            return {
                ...state,
                isLoggedIn: action.payload.isLoggedIn
            };
        default:
            return state;
    }
}