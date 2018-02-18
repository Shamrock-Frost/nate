type item = {
  id: int,
  title: string,
  body: string,
};


type action =
  | AddItem(string, string)
  | UpdateItem(int, string, string)
  | StartEditItem(int);

type state = {
  showGuy: bool,
  guyAction: action,
  /*onSave: (string, string) =>*/
  items: list(item)
};

let lastId = ref(0);

let newItem = (title, body) => {
  lastId := lastId^ + 1;
  {id: lastId^, title: title, body: body}
};


let str = ReasonReact.stringToElement;

module DisplayItem = {
  let component = ReasonReact.statelessComponent("DisplayItem");
  let make = (~item, ~onClick, children) => {
    ...component,
    render: (self) =>
      <div className="item" onClick=onClick>
      (str(item.title ++ "-----" ++ item.body))
      </div>
  };
};

/* This is the basic component. */
let component = ReasonReact.reducerComponent("Page");

/* Your familiar handleClick from ReactJS. This mandatorily takes the payload,
   then the `self` record, which contains state (none here), `handle`, `reduce`
   and other utilities */


let handleClick = (_event, _self) => Js.log("clicked!");

/* `make` is the function that mandatorily takes `children` (if you want to use
   `JSX). `message` is a named argument, which simulates ReactJS props. Usage:

   `<Page message="hello" />`

   Which desugars to

   `ReasonReact.element(Page.make(~message="hello", [||]))` */
let thing = (_) => Js.log("thing");


let make = (~message, _children) => {
  ...component,
  initialState: () => {
    guyAction: AddItem("",""),
    showGuy: false,
    /*onSave: ((title, text) => send(AddItem(title, text))),*/
    items: [ {id: 0, title: "first note", body: "this is the body for the first note"} ]
  },
  reducer: (action, state) => {
    Js.log(state.guyAction);
    switch action {
      | AddItem(title, body) => ReasonReact.Update({...state, guyAction: action, showGuy: false, items: [newItem(title, body), ...state.items]})
      | StartEditItem(id) => ReasonReact.Update({...state, guyAction: action, showGuy: true})
      | UpdateItem(id, title, body) => ReasonReact.Update({...state, guyAction: action})
    }
  },
  render: ({state: {items, showGuy, guyAction}, send, handle}) =>
    <div className="top">
        <div onClick=(handle(handleClick))> (str(message)) </div>
        <div className="items">
    (List.map((item) => <DisplayItem
                         onClick=((e_)=>send(StartEditItem(item.id)))
                         key=(string_of_int(item.id)) item />, items)
     |> Array.of_list |> ReasonReact.arrayToElement)
        </div>
    <button onClick=((_)=> {send(StartEditItem(0));
                            Js.log("sssssssss");
    })>(str("add new task"))</button>
    (switch guyAction {
      | AddItem(_, _) => <Modal body="" title="" show=showGuy onSave=((title, text) => send(AddItem(title, text)))/>
      | StartEditItem(id) => <Modal body="">

    })

    /* idea: have the current function passed in to modal as a reference to a function in page state
     then change that functino depending on new or edit.
   */
    </div>

};

