import { combineReducers } from 'redux';
import userListReducer from "./databaseReducer";
import userIsLoggedInReducer from "./loginReducer";

export default combineReducers(
    {
        userListReducer,
        userIsLoggedInReducer
    }
)