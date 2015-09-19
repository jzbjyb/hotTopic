var rootModule = angular.module('rootapp', ['ngRoute', 'ngCookies', 'utils'], function(){})
	.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
		return $routeProvider
      .when('/hottopic', {
        templateUrl: '/static/templates/hot-topic.html',
        controllerUrl: '/static/javascripts/controller/hot-topic.js'
      })
	}]);

(function() {
	$(window).scroll(function(event) {
		$('.ab-fixed').css('top', $(window).scrollTop() + 'px');
	});
})();