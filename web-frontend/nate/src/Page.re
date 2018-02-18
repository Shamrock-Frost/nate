type item = {
  id: int,
  title: string,
  body: string,
};

type state = {
  items: list(item)
};

type action =
  | AddItem(string, string)
  | EditItem(int);

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
    items: [ {id: 0, title: "first note", body: "this is the body for the first note"} ]
  },
  reducer: (action, state) => {
    switch action {
      | AddItem(title, body) => ReasonReact.Update({items: [newItem(title, body), ...state.items]})
      | EditItem(id) => ReasonReact.UpdateWithSideEffects(state, (e_)=>Js.log("stst"));
    }
  },
  render: ({state: {items}, send, handle}) =>
    <div className="top">
        <div onClick=(handle(handleClick))> (str(message)) </div>
        <div className="items">
            (List.map((item) => <DisplayItem onClick=((e_)=>send(EditItem(item.id))) key=(string_of_int(item.id)) item />, items) |> Array.of_list |> ReasonReact.arrayToElement)
        </div>
    <Modal body="" title="" onSave=((title, text) => send(AddItem(title, text)))/>
    </div>
};

