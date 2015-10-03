rootModule.controller('hotTopicCtrl', ['$scope','$routeParams', '$location', '$window', 'utils',	function($scope, $routeParams, $location, $window, utils) {
  var nowDate = new Date();
  
  $scope.scope = '24';
  $scope.date = {
    'year': nowDate.getFullYear(),
    'month': nowDate.getMonth(),
    'day': nowDate.getDate(),
    'hour': nowDate.getHours()
  }

  var dateParse = function(dateArr) {
    return dateArr.map(function(e) {return [e.getMonth() + 1, e.getDate(), e.getHours()].join('-')});
  }
  /*
  $scope.$watchGroup(['date.year', 'date.month', 'date.day', 'date.hour'], function(newVal, oldVal) {
    console.log(newVal);
    $scope.endDate = new Date(newVal[0], newVal[1], newVal[2], newVal[3]);
  }, true)*/
  
  $scope.$watchGroup(['endDate', 'scope'], function(newVal, oldVal) {
    if(!newVal || !newVal[0] || !newVal[1]) return;
    var endDate = newVal[0];
    var interval = newVal[1];
    var startDate = new Date(endDate);
    startDate.setHours(startDate.getHours() - interval);
    console.log('get clusters from ' + startDate + ' to ' + endDate);
    var xArray = (function(start, end) {
      var xArray = [];
      var it = new Date(start);
      while(it.getTime() <= end.getTime()) {
        xArray.push(new Date(it));
        it.setHours(it.getHours() + 1);
      }
      xArray.push(it);
      return xArray;
    })(startDate, endDate);
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
    utils.api.getJson(utils.genUrl('/clusters', {'date': endDate.toISOString(), 'scope': interval}))
    .success(function (data) {
      if(data['data'] !== undefined && data['data']['hotTopicClusters'].length > 0) {
        $scope.hotTopicClusters = data['data']['hotTopicClusters'][0];
      }
    }).error(function(msg) {
      utils.msgAlert(msg, 'error', 5000);
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
    return;
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

  $scope.change = function() {
    $scope.endDate = new Date($scope.date.year, $scope.date.month,
      $scope.date.day, $scope.date.hour);
  }

  $scope.changeDay = function(offset) {
    $scope.date['day'] += offset;
    $scope.change();
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