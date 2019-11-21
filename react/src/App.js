import React from 'react';
import logo from './logo.svg';
import rock from './Rock.png';
import paper from './Paper.png';
import scissors from './Scissors.png';
import axios from 'axios';
import './App.css';

function App() {
  const [text, setText] = React.useState(''); // creates state variable, retuns tuple
  const [User, setUser] = React.useState('');
  const [statusText, setStatusText] = React.useState('');
  
  const handleClick = () => {
    axios.get(`/api?key=${text}`) // promise
      .then((res) => {
        setUser(res.data);
        setStatusText('Waiting');
      })
      .catch(console.log);
  };

  return (
    <div className="App Container">
      <div className="App-div">
        <br></br>
        <div className="Cursive Username">
          Username:
          <pre> </pre>
          <input value={text} onChange={e => setText(e.target.value)} className="Idbox"/>
          <pre> </pre>
          <button onClick={handleClick} className="Cursive Button">Enter</button>
        </div>
        <br></br>
        <div className = "RPCContainer">
          <div className="RPC">
            <div>Rock</div>
            <div><img src={rock} className="Choice-logo" alt="Rock" /></div>
            <button className="Cursive Button">Select</button>
          </div>
          <div className="RPC">
            <div>Paper</div>
            <div><img src={paper} className="Choice-logo" alt="Paper" /></div>
            <button className="Cursive Button">Select</button>
          </div>
          <div className="RPC">
            <div>Scissors</div>
            <div><img src={scissors} className="Choice-logo" alt="Scissors" /></div>
            <button className="Cursive Button">Select</button>
          </div>
        </div>
        &nbsp;
        <div className="PlayerContainer">
          <div className="Cursive Player">
            Player:
          </div>
          <div className="Cursive Player">
            Gamertag:
          </div>
          <div className="Cursive Player">
            Status:
          </div>
        </div>
        <br></br>
        <div className="PlayerContainer">
          <div className="Cursive Player">
            1
          </div>
          <div className="Cursive Player">
            {User}
          </div>
          <div className="Cursive Player">
            {statusText}
          </div>
        </div>
        <br></br>
        <div className="PlayerContainer">
          <div className="Cursive Player">
            2
          </div>
          <div className="Cursive Player">
          
          </div>
          <div className="Cursive Player">
          
          </div>
        </div>
        <br></br>
      </div>

      <div className="Leaderboard-div Cursive">
        <img src={logo} className="App-logo" alt="logo" />
        Leaderboards:
        <div className="Databox">
          <div>
            
            
          </div> 
        </div>
      </div>
    </div>
  );
}

export default App;
