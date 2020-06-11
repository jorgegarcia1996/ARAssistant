import {
    GET_ALL_USERS,
} from '../actions/databaseActions';

const initialState = {
    fbUsers: {},
    uidList: []
}

export default function usersListReducer(state = initialState, action) {
    switch(action.type) {
        case GET_ALL_USERS:
            return {
                ...state,
                fbUsers: action.payload.fbUsers,
                uidList: action.payload.uidList
            };
        default:
            return state;
    }
}