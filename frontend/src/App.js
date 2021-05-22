import React, { Component } from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import GatewayList from './GatewayList';
import GatewayEdit from "./GatewayEdit";

class App extends Component {
  render() {
    return (
        <Router>
          <Switch>
            <Route path='/' exact={true} component={GatewayList}/>
            <Route path='/gateways/:id' component={GatewayEdit}/>
          </Switch>
        </Router>
    )
  }
}

export default App;
