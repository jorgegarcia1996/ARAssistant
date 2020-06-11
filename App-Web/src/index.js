import React from "react";
import ReactDOM from "react-dom";
import "./assets/styles/index.css";
import App from "./controllers/App/App";
import * as serviceWorker from "./services/serviceWorker";
import { BrowserRouter } from "react-router-dom";
//Redux
import { createStore, applyMiddleware, compose } from "redux";
import { Provider } from "react-redux";
import rootReducer from './redux/reducer/rootReducer';
import thunk from 'redux-thunk';

const composeEnhancers =
  typeof window === 'object' &&
  window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ?   
    window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({
    }) : compose;

const enhancer = composeEnhancers(
  applyMiddleware(thunk),
);
const store = createStore(rootReducer, enhancer);

ReactDOM.render(
  <Provider store={store}>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </Provider>,
  document.getElementById("root")
);

serviceWorker.register();
