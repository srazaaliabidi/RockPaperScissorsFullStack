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

  // const handleClick = () => {
  //   axios.get(`/api?key=${text}`) // promise
  //     .then((res) => {
  //       setUser(res.data);
  //     })
  //     .catch(console.log);
  // };

  return (
    <div className="container App-div">

      <div className="nine columns">
        <br></br>
        <div className="Cursive Username">
          Username:
          <pre> </pre>
          <input value={text} onChange={e => setText(e.target.value)} className="Idbox" />
          <pre> </pre>
          <button onClick={()=> setStatusText("Waiting")} className="Cursive Button">Enter</button>
        </div>
        <br></br>
        <div className="RPCContainer">
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
          <div className="Cursive">
            Player:
          </div>
          <div className="Cursive">
            Gamertag:
          </div>
          <div className="Cursive">
            Status:
          </div>
        </div>
        <br></br>

        <div className="PlayerContainer">
          <div className="Cursive Player">
            <div>
              1
            </div>
          </div>
          <div className="Cursive Player">
          <div>
              {text}
            </div>
            
          </div>
          <div className="Cursive Player">
          <div>
             {statusText}
            </div>
           
          </div>
        </div>

        <br></br>
        <div className="PlayerContainer">
          <div className="Cursive Player">
            <div>
              2
            </div>
          </div>
          <div className="Cursive Player">
            <div>
              Name is here {User}
            </div>
          </div>
          <div className="Cursive Player">
            <div>
              {statusText}
            </div>
          </div>
        </div>
        <br></br>
      </div>

      <div className="three columns">
        <div className="Leaderboard-div Cursive">
          <img src={logo} className="App-logo" alt="logo" />
          Leaderboards:

          <div className="Databox">

          </div>

        </div>
      </div>
    </div>

  );
}

export default App;
