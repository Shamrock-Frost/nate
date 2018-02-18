type state = {
  showing: bool,
  body: string,
  title: string
};

let str = ReasonReact.stringToElement;

type action =
  | Change(string)
  | ChangeTitle(string)
  | Open
  | Save;

let component = ReasonReact.reducerComponent("Modal");
let make = (~onSave, ~body, ~title, _children) => {
  ...component,
  reducer: (action, state) => {
    switch(action) {
      /* lookup how to send actions to parent */
      | Open => ReasonReact.Update({...state, showing: true})
      | Save => {onSave("title", state.body); ReasonReact.Update({showing: false, body: "", title: ""}); }
      | Change(updatedbody) => ReasonReact.UpdateWithSideEffects({...state, body: updatedbody}, (_s) => ());
      | ChangeTitle(updatedtitle) => ReasonReact.Update({...state, title: updatedtitle})
    }
  },
  initialState: () => {
    showing: true,
    body: "emptybody",
    title: "emptytitle"
  },
  render: ({state: {showing, body}, send}) => {
      <div className="modal">
      (if (showing) {
        <div className="modalMom">
          <input placeholder="title"
          onChange=(
            event=>
              send(ChangeTitle(ReactDOMRe.domElementToObj(ReactEventRe.Form.target(event))##value)))></input>
          <br></br>
            <textarea placeholder="new task"
            onInput=(
                event=> {
                send(Change(ReactDOMRe.domElementToObj(ReactEventRe.Form.target(event))##value));}
            )> </textarea>
            <button onClick=((_e)=> send(Save))>(str("done"))</button>
        </div>
      } else {
        str(body);
        <button onClick=((_e)=> send(Open))>(str("add new item"))</button>
      })
    </div>
  }
};
