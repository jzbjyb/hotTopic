rootModule.controller('hotTopicCtrl', ['$scope','$routeParams', '$location', '$window', 'utils',	function($scope, $routeParams, $location, $window, utils) {
  var nowDate = new Date();
  $scope.startDate = new Date(nowDate.getFullYear(), nowDate.getMonth(), nowDate.getDate());
  $scope.endDate = new Date($scope.startDate);
  $scope.endDate.setDate($scope.endDate.getDate() + 1);

  var dateParse = function(dateArr) {
    return dateArr.map(function(e) {return [e.getMonth() + 1, e.getDate(), e.getHours()].join('-')});
  }  
  $scope.$watchGroup(['startDate', 'endDate'], function(newVal, oldVal) {
    console.log('get clusters');
    console.log(newVal);
    if(!newVal || !newVal[0] || !newVal[1] || newVal[0].getTime() >= newVal[1].getTime()) return;
    var xArray = (function(start, end) {
      var xArray = [];
      var it = new Date(start);
      while(it.getTime() <= end.getTime()) {
        xArray.push(new Date(it));
        it.setHours(it.getHours() + 1);
      }
      xArray.push(it);
      return xArray;
    })(newVal[0], newVal[1]);
    renderManager.clear();
    // init chart
    $('.graph__panel').highcharts({
      title: {
          text: 'hot topic trends',
          x: -20 //center
      },
      xAxis: {
          categories: dateParse(xArray)
      },
      yAxis: {
          min: 1,
          title: {
              text: 'count'
          },
          plotLines: [{
              value: 0,
              width: 1,
              color: '#808080'
          }]
      },
      tooltip: {
          valueSuffix: '#'
      },
      legend: {
          layout: 'vertical',
          align: 'right',
          verticalAlign: 'middle',
          borderWidth: 0
      },
      series: {
        data:[null,null]
      }
    });
    utils.api.getJson(utils.genUrl('/clusters', {'start': newVal[0].toISOString(), 'end': newVal[1].toISOString()}))
    .success(function (data) {
      if(data['data'] != undefined) {
        $scope.hotTopicCluster = data['data']['topicClusterExts'];
      }
    }).error(function(msg) {
      utils.msgAlert(msg, 'error');
    });
  });
  
  Array.prototype.spliceAll = function(num) {
    if(num <= 0) return [];
    if(this.length == 0) return [];
    var next = this.splice(num);
    var result = [this];
    if(next.length > 0) {
      Array.prototype.push.apply(result, next.spliceAll(num));
    }
    return result;
  }

  var hotTopicsCache = [];
  $scope.clusterClick = function(cluster) {
    if(cluster['added']) {
      renderManager.releaseData(cluster['title']);
    } else {
      utils.api.getJson(Array.apply(null, cluster['items']).spliceAll(5).map(function(e, i) {
      return utils.genUrl('/hottopics', {'ids': e.join(',')})
      })).then(function(data) {
        hotTopicsCache = [];
        data.map(function(e, i) {
          hotTopicsCache.push.apply(hotTopicsCache, e['data']['data']['hotTopics']);
        });
        renderManager.renderData($scope.startDate, $('.graph__panel').highcharts().xAxis[0].categories, cluster['title'], hotTopicsCache, '.graph__panel', cluster);
      });  
    }      
  };

  $scope.changeDay = function(offset) {
    $scope.startDate = new Date($scope.startDate.setDate($scope.startDate.getDate() + offset));
    $scope.endDate = new Date($scope.endDate.setDate($scope.endDate.getDate() + offset));
    console.log('change Day');
  }
  
  var renderManager = (function() {
    var maxNum = 10;
    var renderNum = 0;
    var clusterMap = {};

    var clear = function() {
      renderNum = 0;
      clusterMap = {};
    }

    var releaseData = function(title) {
      if(clusterMap[title] !== undefined) {
        var chart = $('.graph__panel').highcharts();
        delete clusterMap[title]['added'];
        delete clusterMap[title];
        chart.series[chart.series.findIndex(function(e){return e['name'] === title})].remove();
        renderNum--;
      }
    };

    var renderData = function(start, xArray, title, data, panelSelector, cluster) {
      // pre check
      if(clusterMap[title] !== undefined) return;
      //pre delete
      var chart = $('.graph__panel').highcharts();
      if(renderNum >= maxNum) {
        delete clusterMap[chart.series[0]['name']]['added'];
        delete clusterMap[chart.series[0]['name']];
        chart.series[0].remove();
        renderNum--;
      }
      var yData = Array.apply(null, {length: xArray.length}).map(function(){return 0});
      data.map(function(e, i) {
        var time = new Date(e['time']);
        var index = parseInt((time.getTime() - start.getTime()) / (60 * 60 * 1000));
        if(index >= 0 && index < xArray.length) {
          yData[index] += 1;
        }
      });
      cluster['added'] = true;
      clusterMap[title] = cluster;
      chart.addSeries({
        name: title,
        data: yData
      });
      renderNum++;
    };

    return {
      renderData: renderData,
      releaseData: releaseData,
      clear: clear
    };
  })();
}]);