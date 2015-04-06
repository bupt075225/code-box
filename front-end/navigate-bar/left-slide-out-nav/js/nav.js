/* Make the icons appear whenever we hover over one of the list items.
 * the following function that get’s executed whenever we hover over a
 * li
 */
$(function()
{
	/* we defined that it should take 1 second to give a left margin of
	 * -85 pixels to all the link elements in the list. Through the 
	 * margin that we set we will show the navigation to the user and 
	 * with the JavaScript we will then hide it. */
	$('#nav a').stop().animate({'marginLeft':'-85px'},1000);
	
	/* when hovering, we want the specific link element to get a left 
	 * margin of -2 pixels, and that nicely animated, and not too slow 
	 * (200 milliseconds). Moving the mouse out shall put the link 
	 * element back to it’s old position (-85 pixels).
	 */
    $('#nav > li').hover(
	    function()
		{
		    $('a',$(this)).stop().animate({'marginLeft':'-2px'},200);
		},
		function()
		{
		    $('a',$(this)).stop().animate({'marginLeft':'-85px'},200);
		}
	);
});