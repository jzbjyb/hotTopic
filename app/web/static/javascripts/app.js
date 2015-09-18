var consoleModule = angular.module('rootapp', ['ngRoute', 'ngCookies', 'utils'], function(){})
	.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
		return $routeProvider
      .when('/hottopic', {
        templateUrl: '/static/templates/hot-topic.html',
        controllerUrl: '/static/javascripts/controller/hot-topic.js'
      })
	}]); 