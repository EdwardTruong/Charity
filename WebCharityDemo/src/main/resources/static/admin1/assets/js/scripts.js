/*!
	* Start Bootstrap - SB Admin v7.0.2 (https://startbootstrap.com/template/sb-admin)
	* Copyright 2013-2021 Start Bootstrap
	* Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
	*/
// 
// Scripts
// 
'use strict';
window.addEventListener('DOMContentLoaded', event => {

	// Toggle the side navigation
	const sidebarToggle = document.body.querySelector('#sidebarToggle');
	if (sidebarToggle) {
		// Uncomment Below to persist sidebar toggle between refreshes
		// if (localStorage.getItem('sb|sidebar-toggle') === 'true') {
		//     document.body.classList.toggle('sb-sidenav-toggled');
		// }
		sidebarToggle.addEventListener('click', event => {
			event.preventDefault();
			document.body.classList.toggle('sb-sidenav-toggled');
			localStorage.setItem('sb|sidebar-toggle', document.body.classList.contains('sb-sidenav-toggled'));
		});
	}

});

const modal = document.querySelector('.item');
const overlay = document.querySelector('.overlay');
const btnCloseItem = document.querySelector('.btn-close');
const btnOpenItem = document.querySelector('.show-item');

// Kiểm tra xem DOM element có tồn tại trước khi gọi hàm addEventListener() hay không.
const myButton = document.querySelector('#my-button');
if (myButton) {
  // Gọi hàm addEventListener() sau khi DOM element được render.
  window.onload = function() {
    myButton.addEventListener('click', function() {
     modal.classList.remove('hidden');
	overlay.classList.remove('hidden');
    });
  };
}
const closeItem = function (){
	modal.classList.add('hidden');
	overlay.classList.add('hidden');
};


btnCloseItem.addEventListener('click',closeItem );

overlay.addEventListener('click', closeItem );