// JavaScript Document
/*The js finds the navigation by using the document.getElementById and the loops
 *though all the child li tags (perhaps child tags is not the best analogy here, 
 *as the loop goes through all the li tags under the current ul node, wich in 
 *fact means all decendants of the parent). For every li tag it searches for a 
 *child ul tag, wich means that the list element has a sublist (or in our case 
 *a submenu). So when and if we find it, all we need to do is append a javascript
 * function to the onmouseover and onmouseout events, wich just sets the inline 
 *style of the sublist to block when hovered over and none (thus hidding it from
 * plain sight) when the mouse leaves the hover area.
 */

function addLoadEvent(func) {
    var oldonload = window.onload;
    if (typeof window.onload != 'function') {
        window.onload = func;
    } else {
        window.onload = function() {
            oldonload();
            func();
        }
    }
}

function prepareMenu() {
    // first lets make sure the browser understands the DOM methods we will be using
  	if (!document.getElementsByTagName) return false;
  	if (!document.getElementById) return false;
  	
  	// lets make sure the element exists
  	if (!document.getElementById("nav")) return false;
  	var nav = document.getElementById("nav");
  	
  	// for each of the li on the root level check if the element has any children
  	// if so append a function that makes the element appear when hovered over
  	var root_li = nav.getElementsByTagName("li");
  	for (var i = 0; i < root_li.length; i++) {
  	    var li = root_li[i];
  	    // search for children
  	    var child_ul = li.getElementsByTagName("ul");
  	    if (child_ul.length >= 1) {
  	        // we have children - append hover function to the parent
  	        li.onmouseover = function () {
  	            if (!this.getElementsByTagName("ul")) return false;
  	            var ul = this.getElementsByTagName("ul");
  	            ul[0].style.display = "block";
  	            return true;
  	        }
  	        li.onmouseout = function () {
  	            if (!this.getElementsByTagName("ul")) return false;
  	            var ul = this.getElementsByTagName("ul");
  	            ul[0].style.display = "none";
  	            return true;
  	        }
  	    }
  	}
  	
  	return true;
}

addLoadEvent(prepareMenu);