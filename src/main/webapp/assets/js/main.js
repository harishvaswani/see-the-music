(function() {

	// Vars
	var $form = document.querySelectorAll('#main-form')[0],
		$submit = document.querySelectorAll('#main-form input[type="submit"]')[0],
		$artist = document.querySelectorAll('#main-form input[type="artist"]')[0],
		$song = document.querySelectorAll('#main-form input[type="song"]')[0],
		$message;

		// Message
		$message = document.createElement('span');
		$message.classList.add('message');
		$form.appendChild($message);

		$message._show = function(type, text) {
			$message.innerHTML = text;
			$message.classList.add(type);
			$message.classList.add('visible');
			window.setTimeout(function() {
				$message._hide();
			}, 5000);
		};

		$message._hide = function() {
			$message.classList.remove('visible');
		};

		// Events.
		// Note: If you're *not* using AJAX, get rid of this event listener.
		$form.addEventListener('submit', function(event) {

			event.stopPropagation();
			event.preventDefault();

			// Hide message.
			$message._hide();

			// Disable submit.
			$submit.disabled = true;

			// Process form.
			window.setTimeout(function() {
				// Enable submit.
				$submit.disabled = false;
				// Show message.
				$message._show('success',  'Playing '+$song.value+' by '+$artist.value+'...');
				//$message._show('failure', 'Something went wrong. Please try again.');
				// Reset form.
				$form.reset();
			}, 750);

		// Search the song using Spotify API and play a 30 sec preview
		$.ajax({
        	url: 'https://api.spotify.com/v1/search',
            	data: {
                	q: $song.value + ' artist:'+$artist.value,
                    type: 'track'
               	},
                success: function (response) {
					var audio = new Audio();
				 	audio.src = response.tracks.items[0].preview_url;
                 	audio.play();
                }
		});

        $('#slideshow').bjqs({
    		'height' : 320,
            'width' : 620,
            'animtype' : 'fade', // accepts 'fade' or 'slide'
            'animduration' : 450, // how fast the animation are
            'animspeed' : 4000, // the delay between each slide
            'automatic' : true,
          	'showcontrols' : false, // show next and prev controls
          	'showmarkers' : false, // Show individual slide markers
          	'keyboardnav' : true, // enable keyboard navigation
	        'hoverpause' : true // pause the slider on hover

		});


	});

})();