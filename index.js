var note_data_list = [];

document.getElementById("addNote").addEventListener("click", function(){
    var title = getValue("create_textbox_input");
    if (title != ""){
    	createTextField(title,"",-1);
    }
});

// document.getElementById("popup_submit").addEventListener("click", function(){
// 	overlay.style.display = "none";
//     popup_upper.style.display = "none";
//   	popup.style.display = "none";
// });

function getValue(id){
	var value = document.getElementById(id).value;
	document.getElementById(id).value = "";
	return value;
}

function add (input_value){

	var li = document.createElement("li");
	var ul = document.getElementById("myUL");
	var tmp = document.createTextNode(input_value);
	var div = document.createElement("div");
	var popup = document.getElementsByClassName("popup");
	div.className = "popup";
	div.innerHTML = input_value;
	// div.style.marginRight =  "5%";
	// div.style.height =  "100%";

	// add inputvalue into li
	li.appendChild(div);
	// add li to popup.html
	ul.appendChild(li);


	var close = document.getElementsByClassName("close");
	var span = document.createElement("SPAN");
	var txt = document.createTextNode("\u00D7");
	span.className = "close";
	span.appendChild(txt);
	li.appendChild(span);


	// this is the list to store the score information of close

	for (i = 0; i < close.length; i++) {

		// pass parameter i as index
		(function(index){
			//alert(ul.children[i].getElementsByTagName("div"));

			popup[i].onclick = function(){
				var title = note_data_list[index].title;
				var content = note_data_list[index].content;
				createTextField(title,content,index);
			}

		})(i);

		(function(index){
		  close[i].onclick = function(){


		    // hide the cancel thing 

		    var div = this.parentElement;
		    div.style.display = "none";
		 
		  }    
		})(i);
	}

}


//Use the onload event so that we can make sure the DOM is at 
//least mostly loaded before trying to get elements

function createTextField(title,content,pos){
	document.getElementById("popup_title").innerHTML = title;
	document.getElementById("content_input").value   = content;

	var overlay = document.getElementById("overlay");
	//Set a variable to contain the DOM element of the popup
	var popup_upper = document.getElementById("popup_upper");
	var popup = document.getElementById("popup");

	//Changing the display css style from none to block will make it visible



	overlay.style.display = "block";
	popup_upper.style.display = "block";
	//Same goes for the popup
	popup.style.display = "block";
    document.getElementById("popup_submit").onclick = function(){
    	var content = document.getElementById("content_input").value;
		if(pos == -1){
			add(title);
			addData(title, content);
		}else{
			note_data_list[pos].content = content;
		}		

		document.getElementById("content_input").value = "";
		overlay.style.display = "none";
		popup_upper.style.display = "none";
		popup.style.display = "none";		
	}
}

function addData(title, content){
	var data = Object();
	data.title = title;
	data.content = content;
	note_data_list.push(data);
}



