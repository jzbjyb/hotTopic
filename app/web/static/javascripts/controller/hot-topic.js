consoleModule.controller('hotTopicCtrl', ['$scope','$routeParams', '$location', '$window', 'utils',	function($scope, $routeParams, $location, $window, utils) {
	$scope.startDate = new Date();
  $scope.endDate = new Date($scope.startDate);
  $scope.startDate.setDate($scope.startDate.getDate() - 1);
  $scope.hotTopicCluster;

  utils.api.getJson(utils.genUrl('/clusters', {'start': $scope.startDate.toISOString(), 'end': $scope.endDate.toISOString()}))
  .success(function (data) {
    if(data['data'] != undefined) {
      $scope.hotTopicCluster = data['data']['topicClusterExts'];
    }
  }).error(function(msg) {
    utils.msgAlert(msg, 'error');
  });
}]);