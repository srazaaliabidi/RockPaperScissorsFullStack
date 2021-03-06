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


let num = 0;
function App() {
  const [text, setText] = React.useState(''); // creates state variable, retuns tuple
  const [listLeader, setListLeader] = React.useState([]);
  const ws = React.useRef(new WebSocket('ws://localhost:1234/ws'));
  const alertText = 'Please enter a valid Username (Single word, no spcaes)';

  ws.current.onopen = () => {
    console.log('Connection open!')
  };

  ws.current.onmessage = (message) => {
    console.log('message received')
    console.log(message);
    if (message.data == "WAITSCREEN") {
      console.log("REACH WAIT");
      // window.location = '/waiting';
    }
    if (message.data == "REMOVE_WAITSCREEN_PLAY_GAME") {
      gameStart();
    }
    if (message.data == "WINNER") {
      alert("You win, press enter to play again")
    }
    if (message.data == "LOSER") {
      alert("You lose, press enter to play again")
    }
  };

  ws.current.onclose = () => {
    console.log('connection closed');
  };

  ws.current.onerror = () => {
    console.log('ws error');
  };

  const handleClick = () => {//if a blank userame is inputed an alert will popup, if it's not then move to waiting page
    if (text != '') {
      //ws.current.send(text);
      ws.current.send(`{"name":"${text}","choice":""}`);
      displayWaiting();


      axios.get('/getall')
        .then((res) => {
          setListLeader(res.data.response);
        })
        listLeader.map(noteObject =>
          num = noteObject.numPlayer
        )

    }
    else {
      alert(alertText);
    }
  };
  const handleRock = () => {
    ws.current.send(`{"name":"${text}","choice":"rock"}`);
  };
  const handlePaper = () => {
    ws.current.send(`{"name":"${text}","choice":"paper"}`);
  };
  const handleScissors = () => {
    ws.current.send(`{"name":"${text}","choice":"scissors"}`);
  };
  const leaderboardRefresh = () => {
    axios.get('/getall')
      .then((res) => {
        setListLeader(res.data.response);
      })
  }

  function off() {
    document.getElementById("overlay").style.display = "none";
  }

  function displayWaiting() {
    (document.getElementById("waitingDiv")).style.display = "block";
    (document.getElementById("totalDiv")).style.display = "none";
  }
  function hideWaiting() {
    (document.getElementById("totalDiv")).style.display = "block";
    (document.getElementById("waitingDiv")).style.display = "none";
  }
  function gameStart() {
    (document.getElementById("totalDiv")).style.display = "none";
    (document.getElementById("waitingDiv")).style.display = "none";
    (document.getElementById("startDiv")).style.display = "block";
  }
  return (
    <div onLoad={leaderboardRefresh}>

      <div id="Home" className="App-div Cursive">
        <div className="textcenter">
          <button onClick={leaderboardRefresh} className="Cursive Button">Refresh Leaderboard</button>
        </div>
        <pre></pre>
        <div className="textcenter">
          Team Keyboard Presents:
        </div>
        <pre> </pre>
        <div className="RPCContainer textcenter">
          <div>
            <div>Rock</div>
            <div><img src={rock} className="Choice-logo" alt="Rock" /></div>
            <button onClick={handleRock} className="Cursive Button">Select</button>
          </div>
          <div>
            <div>Paper</div>
            <div><img src={paper} className="Choice-logo" alt="Paper" /></div>
            <button onClick={handlePaper} className="Cursive Button">Select</button>
          </div>
          <div>
            <div>Scissors</div>
            <div><img src={scissors} className="Choice-logo" alt="Scissors" /></div>
            <button onClick={handleScissors} className="Cursive Button">Select</button>
          </div>
        </div>

        <pre> </pre>

        <div className="border1 centered">
          <div className="textcenter">
            Please Enter a Username:
          </div>

          <div className="space"></div>

          <div className="textcenter">
            <input value={text} onChange={e => setText(e.target.value)} className="Idbox" />
          </div>

          <div className="space"></div>

          <div className="textcenter">
            <button onClick={handleClick} className="Cursive Button">Enter</button>
          </div>
          <div className="space"></div>
        </div>

        <div className="mediumspace2"></div>

        <div id="startDiv" className="textcenter border1 centered hideInitial">Game has started...</div>
        <div id="waitingDiv" className="textcenter border1 centered hideInitial">Waiting for another player...</div>
        <div id="totalDiv" className="textcenter border1 centered showInitial">Available Players: {num}</div>

        <div className="textcenter">
        <pre> </pre>
          <img src={logo} className="App-logo" alt="logo" />
          <div>Leaderboards:</div>
        </div>
        <div className="Leaderboard textcenter">
          {listLeader.map(noteObject =>
            <div className="border2">
              <div>
                <div>
                  Gamertag: {noteObject.name}
                </div>
                <div>
                  Wins: {noteObject.score}
                </div>
              </div>
            </div>
          )}
        </div>
        <pre> </pre>
      </div>

    </div>
  )
}


/*
//Really basic Home page right now
//Refresh leaderboard button at the top for easy checking
//Enter a username and click next, takes you to waiting page
function Home() {
  const [text, setText] = React.useState(''); // creates state variable, retuns tuple
  const [listLeader, setListLeader] = React.useState([]);
  const ws = React.useRef(new WebSocket('ws://localhost:1234/ws'));
  const alertText = 'Please enter a valid Username (Single word, no spcaes)';
  ws.current.onopen = () => {
    console.log('Connection open!')
  };
  ws.current.onmessage = (message) => {
    console.log('message received')
    console.log(message);
    if (message.data == "WAITSCREEN") {
      window.location = '/waiting';
    }
    else if (message.data == "REMOVE_WAITSCREEN_PLAY_GAME") {
      window.location = '/game'
    }
    // setClickCount(Number(message.data));
  };
  ws.current.onclose = () => {
    console.log('connection closed');
  };
  ws.current.onerror = () => {
    console.log('ws error');
  };
  const handleClick = () => {//if a blank userame is inputed an alert will popup, if it's not then move to waiting page
    if (text != '') {
      //window.location = '/waiting';
      ws.current.send(`{"name":"${text}","choice":""}`);
    }
    else {
      alert(alertText);
    }
  };
  const leaderboardRefresh = () => {
    ws.current.send(text);
    axios.get('/getall')
      .then((res) => {
        setListLeader(res.data.response);
      })
    console.log('message send')
  }
  return (
    <div className="App-div Cursive">
      <div className="mediumspace textright">
        <button onClick={leaderboardRefresh} className="Cursive Button2">Manual Refresh</button>
      </div>
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
      <div className="border1 centered">
        <div className="textcenter">
          Please Enter a Username:
        </div>
        <div className="space"></div>
        <div className="textcenter">
          <input value={text} onChange={e => setText(e.target.value)} className="Idbox" />
        </div>
        <div className="space"></div>
        <div className="textcenter">
          <button onClick={handleClick} className="Cursive Button">Enter</button>
        </div>
        <div className="space"></div>
      </div>
      <pre> </pre>
      <div className="textcenter">
        <img src={logo} className="App-logo" alt="logo" />
        <div>Leaderboards:</div>
      </div>
      <div className="Leaderboard textcenter">
        {listLeader.map(noteObject =>
          <div className="border2">
            <div>
              <div>
                Gamertag: {noteObject.name}
              </div>
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
//Rn theres not much but I plan to fix and put active players count here
//Also plan to have a notification of some sort pop up what another player has been match made and then the continue button will pop up
//Continue button takes you to game page
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
  const playerRefresh = () => {
    axios.get('/getall')
      .then((res) => {
        setListLeader(res.data.response);
      })
    console.log('message send')
  }
  return (
    <div className="App-div Cursive">
      <div className="mediumspace textright">
        <button onClick={playerRefresh} className="Cursive Button2">Manual Refresh</button>
      </div>
      <div className="bigspace"></div>
      <div className="textcenter">Waiting for another player...</div>
      <div className="textcenter">
        {listLeader.map(noteObject =>
          <div> Available Players: {noteObject.numPlayer}
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
//Game page, this is more react interacting with spark so thats that
//Idea is to have a popup notification pop up when a winner has been decided
//Then the return to home button would pop up and then everything starts over.
//This to be added based on what is needed and what I think looks good :3
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
  const handleClick3 = () => {
    window.location = '/';
  };
  const handleRock = () => {
    ws.current.send(`{"name":"${text}","choice":"Rock"}`);
  };
  const handlePaper = () => {
    ws.current.send(`{"name":"${text}","choice":"Paper"}`);
  };
  const handleScissors = () => {
    ws.current.send(`{"name":"${text}","choice":"Scissors"}`);
  };
  return (
    <div className="App-div Cursive">
      <pre> </pre>
      <pre> </pre>
      <div className="RPCContainer textcenter">
        <div>
          <div>Rock</div>
          <div><img src={rock} className="Choice-logo" alt="Rock" /></div>
          <button onClick={handleRock} className="Cursive Button">Select</button>
        </div>
        <div>
          <div>Paper</div>
          <div><img src={paper} className="Choice-logo" alt="Paper" /></div>
          <button onClick={handlePaper} className="Cursive Button">Select</button>
        </div>
        <div>
          <div>Scissors</div>
          <div><img src={scissors} className="Choice-logo" alt="Scissors" /></div>
          <button onClick={handleScissors} className="Cursive Button">Select</button>
        </div>
      </div>
      <pre> </pre>
      <div className="textcenter centered">
        <div>
          <div className="PlayerContainer">
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
          <div className="PlayerContainer">
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
          <div className="PlayerContainer">
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
        <button onClick={handleClick3} className="Cursive Button2">Return to Menu</button>
      </div>
    </div>
  )
}
*/

export default App;