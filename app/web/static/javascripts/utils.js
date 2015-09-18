"use strict";
(function() {
  angular.module('utils', ['ngCookies'])
  .factory('utils', ['$window', '$location', '$http', '$cookies', '$timeout', function ($window, $location, $http, $cookies, $timeout) {

    $http.defaults.headers.common.Authorization = 'Bearer ' + $cookies.token;

    var transformApiUrl = function(url) {return '/api' + url};

    function storeArticleContent(content) {
      var storage = window.localStorage;
      var url = $window.location.href;
      var today = new Date();
      if(content != ""){
        var info = {
          "content": content,
          "time": today.getFullYear() + "" + today.getMonth() + "" + today.getDate()
        }
        if (!storage.getItem(url)) {
          storage.setItem(url, JSON.stringify(info));
        }
        else {
          var oldInfo = JSON.parse(storage.getItem(url));
          if(oldInfo['content'] != content) {
            storage.setItem(url, JSON.stringify(info));
          }
        }
      }
    }

    function generateApi(transformUrlFunction) {
      return {
        getJson: function (url) {
          return ajax({
            method: 'GET',
            url: transformUrlFunction(url)
          });
        },
        postJson: function (url, data) {
          return ajax({
            method: 'POST',
            url: transformUrlFunction(url),
            data: data
          });
        },
        patchJson: function (url, data) {
          return ajax({
            method: 'PATCH',
            url: transformUrlFunction(url),
            data: data
          });
        },
        del: function (url) {
          return ajax({
            method: 'DELETE',
            url: transformUrlFunction(url)
          });
        },
        put: function (url) {
          return ajax({
            method: 'PUT',
            url: transformUrlFunction(url)
          });
        },
      };
    }

		var ajax = (function () {
			var ajaxCount = 0;
			return function (req) {
				if (++ajaxCount == 1) {
          utils.isLoading = true;
				}
				var promise = $http(req);
        promise['finally'](function (data) {
          if (!--ajaxCount) {
            utils.isLoading = false;
          }
        });
				return promise.success(function (data) {
					if (data && data.code === 401) {
						$window.location.href = '/';
					}
				});
			};
		}());

    var utils = {
      isLoading: false,
			monthDays: [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],
      localStore: function(content) {
        return storeArticleContent(content);
      },
      deleteStore: function(key) {
        var storage = window.localStorage;
        storage.removeItem(key);
      },
      getStore: function() {
        var storage = window.localStorage;
        var url = $window.location.href;
        if (!storage.getItem(url) || !JSON.parse(storage.getItem(url))) {
          return "";
        }
        else {
          return JSON.parse(storage.getItem(url)).content;
        }
      },
      msgAlert: function (message, type) {
        swal({   
          title: message,
          text: "",
          timer: 700,
          animation: false,
          type: type,
          showConfirmButton: false 
        });
      },
			mergeParams: function (obj) {
				var params = [];
				angular.forEach(obj, function (value, key) {
					if (value !== undefined)
						params.push(encodeURIComponent(key) + '=' + encodeURIComponent(value));
				});
				return params.join('&');
			},
      removeObjFromArray: function (array, obj) {
        for (var i=0; i<array.length; i++) {
          if (obj.id && obj.id === array[i].id) {
            array.splice(i, 1);
            break;
          }
        }
      },
      removeObjFromOptions: function (array, obj) {
        for (var i=0; i<array.length; i++) {
          if (obj.text === array[i].text) {
            array.splice(i, 1);
            break;
          }
        }
      },
      convertRFCTime: function(timestamp) {
        var str = (new Date(timestamp + 8 * 60 * 60 * 1000)).toISOString();
        str = str.substring(0, str.length - 1);
        str += "+08:00";
        return str;
      },
      log: function(x) {
        console.log(x);
      },
      // only save id,title,name here
      convertListData: function(list) {
        var res = [];
        for (var i in list) {
          var item = {};
          if (list[i]._id && list[i]._id.$oid) {
            item.id = list[i]._id.$oid;
          }
          if (list[i].name) {
            item.name = list[i].name;
          }
          if (list[i].title) {
            item.title = list[i].title;
          }
          if (list[i].place) {
            item.place = list[i].place;
          }
          if (list[i].type) {
            item.type = list[i].type;
          }
          if (list[i].startTime && list[i].startTime.$date) {
            item.startTimeDate = list[i].startTime.$date;
          }
          res.push(item);
        }
        return res;
      },
      uploadFile: function(fileSelector, url, success) {
        var data = new FormData();
        data.append('fileToUpload', $(fileSelector)[0].files[0]);
        utils.isLoading = true;
        $.ajax ({
          url: url,
          type: 'POST',
          data: data,
          cache: false,
          dataType: 'json',
          processData: false,
          contentType: false,
          success: function (data) {
            success(data);
            $timeout(function(){utils.isLoading=false}, 0);
          },
          error: function (msg) {
            alert("文件上传失败");    
            $timeout(function(){utils.isLoading=false}, 0);
          }
        });
      },
      api: generateApi(transformApiUrl),
			dateFromISOString: function (iso) {
				var date = new Date();
				date.setFullYear(parseInt(iso.slice(0, 4)));
				date.setMonth(parseInt(iso.slice(5, 7)) - 1);
				date.setDate(parseInt(iso.slice(8, 10)));
				date.setHours(parseInt(iso.slice(11, 13)));
				date.setMinutes(parseInt(iso.slice(14, 16)));
				date.setSeconds(parseInt(iso.slice(17, 19)));
				return date;
			},
			numberToDay: {
				'1': '一',
				'2': '二',
				'3': '三',
				'4': '四',
				'5': '五',
				'6': '六',
				'7': '日'
			},
			numberToDayEn: {
				'1': 'MONDAY',
				'2': 'TUESDAY',
				'3': 'WEDNESDAY',
				'4': 'THURSDAY',
				'5': 'FRIDAY',
				'6': 'SATURDAY',
				'7': 'SUNDAY',
			},
      formatImage: function (url, format) {
        return url.substring(0, url.length-4) + '_' + format + '.jpg';
      },
      cutStr: function(str, len) {
        return str.length > len ? str.substr(0, len-1) + '…' : str;
      },
      genUrl: function(baseUrl, params) {
        var param = [];
        angular.forEach(params, function (value, key) {
          if (value !== undefined)
            param.push(encodeURIComponent(key) + '=' + encodeURIComponent(value));
        });
        return param.length > 0 ? baseUrl + '?' + param.join('&') : baseUrl; 
      }
    };
    return utils;
  }]);
})();