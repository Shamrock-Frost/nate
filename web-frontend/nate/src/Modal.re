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
let make = (~onSave, ~show, ~body, ~title,  _children) => {
  ...component,
  reducer: (action, state) => {
    switch(action) {
      /* lookup how to send actions to parent */
      | Open => ReasonReact.Update({...state, showing: true})
      | Save => {onSave(state.title, state.body); ReasonReact.Update({showing: false, body: "", title: ""}); }
      | Change(updatedbody) => ReasonReact.UpdateWithSideEffects({...state, body: updatedbody}, (_s) => ());
      | ChangeTitle(updatedtitle) => ReasonReact.Update({...state, title: updatedtitle})
    }
  },
  initialState: () => {
    showing: false,
    body: body,
    title: title
  },
  render: ({state: {showing}, send}) => {
      <div className="modal">
      (if (show) {
        <div className="modalMom">
          <input placeholder="title"
          defaultValue=title
          onChange=(
            event=>
              send(ChangeTitle(ReactDOMRe.domElementToObj(ReactEventRe.Form.target(event))##value)))></input>
          <br></br>
            <textarea placeholder="new task"
          defaultValue=body
            onInput=(
                event=> {
                send(Change(ReactDOMRe.domElementToObj(ReactEventRe.Form.target(event))##value));}
            )></textarea>
            <button onClick=((_e)=> send(Save))>(str("done"))</button>
        </div>
      } else {
        <div></div>
      })
    </div>
  }
};
