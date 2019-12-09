import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  IndexRoute,
  Link
} from "react-router-dom";
import logo from './logo.svg';
import rock from './Rock.png';
import paper from './Paper.png';
import scissors from './Scissors.png';
import axios from 'axios';
import './App.css';


function Home() {
  const [text, setText] = React.useState(''); // creates state variable, retuns tuple
  const [User, setUser] = React.useState('');
  const [statusText, setStatusText] = React.useState('');
  const [listLeader, setListLeader] = React.useState([]);
  const ws = React.useRef(new WebSocket('ws://localhost:1234/ws'));

  ws.current.onopen = () => {
    console.log('Connection open!')
  };

  ws.current.onmessage = (message) => {
    console.log('message received')
    console.log(message);
    // setClickCount(Number(message.data));
  };

  ws.current.onclose = () => {
    console.log('connection closed');
  };

  ws.current.onerror = () => {
    console.log('ws error');
  };

  const handleClick = () => {
    window.location = '/waiting';
  };

  return (
    <div className="App-div Cursive">

      <pre> </pre>

      <div className="textcenter">
        Team Keyboard Presents:
      </div>

      <pre> </pre>

      <div className="textcenter">
        <div><img src={rock} className="Choice-logo" alt="Rock" /></div>
      </div>

      <pre> </pre>

      <div className="Container">

        <div className="textright">
          <div><img src={paper} className="Choice-logo" alt="Paper" /></div>
        </div>

        <div className="textcenter">
          <div>Rock,</div>
          <div>Paper,</div>
          <div>Scissors</div>
        </div>

        <div className="textleft">
          <div><img src={scissors} className="Choice-logo" alt="Scissors" /></div>
        </div>

      </div>

      <pre> </pre>

      <div className="textcenter">
        Please Enter a Username:
      </div>

      <div className="space"></div>

      <div className="textcenter">
        <input className="Idbox" />
      </div>

      <div className="space"></div>

      <div className="textcenter">
        <button onClick={handleClick} className="Cursive Button">Enter</button>
      </div>

      <pre> </pre>

      <div className="textcenter">
        <img src={logo} className="App-logo" alt="logo" />
        <div>Leaderboards:</div>
      </div>

      <div className="Leaderboard textcenter">
        {listLeader.map(noteObject =>
          <div className="App-text">
            <div> Gamertag: {noteObject.name}
              <div>
                Wins: {noteObject.score}
              </div>
            </div>
          </div>
        )}
      </div>

      <pre> </pre>

    </div>

  )
}
function Waiting() {
  const ws = React.useRef(new WebSocket('ws://localhost:1234/ws'));

  ws.current.onopen = () => {
    console.log('Connection open!')
  };

  ws.current.onmessage = (message) => {
    console.log('message received')
    console.log(message);
    // setClickCount(Number(message.data));
  };

  ws.current.onclose = () => {
    console.log('connection closed');
  };

  ws.current.onerror = () => {
    console.log('ws error');
  };
  const [listLeader, setListLeader] = React.useState([]);
  const handleClick = () => {
    window.location = '/game';
  };


  return (
    <div className="App-div Cursive">
      <div className="bigspace"></div>
      <div className="textcenter">Waiting for another player...</div>
      <div className="textcenter">
        {listLeader.map(noteObject =>
          <div> Available Players {noteObject.numPlayer}
          </div>
        )}
      </div>
      <pre> </pre>
      <div className="textcenter">
        <button onClick={handleClick} className="Cursive Button">Continue</button>
      </div>

    </div>
  )
}

function Game() {
  const [text, setText] = React.useState(''); // creates state variable, retuns tuple
  const [User, setUser] = React.useState('');
  const [statusText, setStatusText] = React.useState('');
  const ws = React.useRef(new WebSocket('ws://localhost:1234/ws'));

  ws.current.onopen = () => {
    console.log('Connection open!')
  };

  ws.current.onmessage = (message) => {
    console.log('message received')
    console.log(message);
    // setClickCount(Number(message.data));
  };

  ws.current.onclose = () => {
    console.log('connection closed');
  };

  ws.current.onerror = () => {
    console.log('ws error');
  };
  const [listLeader, setListLeader] = React.useState([]);
  const handleClick = () => {
    window.location = '/';
  };
  return (
    <div className="App-div Cursive">

      <pre> </pre>
      <pre> </pre>

      <div className="RPCContainer textcenter">
        <div>
          <div>Rock</div>
          <div><img src={rock} className="Choice-logo" alt="Rock" /></div>
          <button className="Cursive Button">Select</button>
        </div>
        <div>
          <div>Paper</div>
          <div><img src={paper} className="Choice-logo" alt="Paper" /></div>
          <button className="Cursive Button">Select</button>
        </div>
        <div>
          <div>Scissors</div>
          <div><img src={scissors} className="Choice-logo" alt="Scissors" /></div>
          <button className="Cursive Button">Select</button>
        </div>
      </div>

      <pre> </pre>

      <div className="textcenter">
        <div>
          <div className="PlayerContainer textcenter">
            <div>
              Player:
          </div>
            <div>
              Gamertag:
          </div>
            <div>
              Status:
          </div>
          </div>
          <div className="space"></div>
          <div className="PlayerContainer textcenter">
            <div className="Player">
              <div>
                1
            </div>
            </div>
            <div className="Player">
              <div>
                {text}
              </div>

            </div>
            <div className="Player">
              <div>
                {statusText}
              </div>

            </div>
          </div>
          <div className="space"></div>
          <div className="PlayerContainer textcenter">
            <div className="Player">
              <div>
                2
            </div>
            </div>
            <div className="Player">
              <div>
                {text}
              </div>

            </div>
            <div className="Player">
              <div>
                {statusText}
              </div>
            </div>
          </div>
        </div>
      </div>

      <pre> </pre>

      <div className="textcenter">
        <button onClick={handleClick} className="Cursive Button2">Return to Menu</button>
      </div>



    </div>
  )
}

function App() {
  return (
    <Router>
      <div>
        <Switch>
          <Route path="/waiting">
            <Waiting />
          </Route>
          <Route path="/game">
            <Game />
          </Route>
          <Route path="/">
            <Home />
          </Route>
        </Switch>
      </div>
    </Router>

  );
}

export default App;