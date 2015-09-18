(function($){
  'use strict';

  function tagNameLowerCase(ele){
    return ele.prop("tagName").toLowerCase();
  };

  function attrs(ele){
    if(ele.length == 0) return null;
    var nodes={};
    for (var i = 0, atts = ele[0].attributes, n = atts.length; i < n; i++){
      nodes[atts[i].nodeName] = atts[i].value;
    }
    return nodes;
  }

  function arrayToDict(array){
    if(!array) return null;
    var result = {};
    for(var i in array){
      result[array[i].toString()] = array[i].toString();
    }
    return result;
  };

  addXhrProgressEvent();
  $.extend(true, $.trumbowyg, {
    langs: {
      zh_cn: {
        viewHTML:"源代码",
        formatting:"格式",
        p:"段落",
        blockquote:"引用",
        code:"代码",
        header:"标题",
        bold:"加粗",
        italic:"斜体",
        strikethrough:"删除线",
        underline:"下划线",
        strong:"加粗",
        em:"斜体",
        del:"删除线",
        unorderedList:"无序列表",
        orderedList:"有序列表",
        insertImage:"插入图片",
        insertVideo:"插入视频",
        link:"超链接",
        createLink:"插入链接",
        unlink:"取消链接",
        justifyLeft:"居左对齐",
        justifyCenter:"居中对齐",
        justifyRight:"居右对齐",
        justifyFull:"两端对齐",
        horizontalRule:"插入分隔线",
        fullscreen:"全屏",
        close:"关闭",
        submit:"确定",
        reset:"取消",
        invalidUrl:"无效的 URL",
        required:"必需的",
        description:"描述",
        title:"标题",
        text:"文字",
        htmlFilter:"过滤标签",
        brFilter:"过滤换行",
        upload:"上传图片(可多选)"
      }
    },

    upload: {
      serverPath: CONSOLECONFIG.PIC_URL
    },

    htmlFilterRule: {
      tagMap: {
        div: 'p'
      },
      delete: {
        tag: [],
        attr: ['style', 'class', 'align', 'lang', 'id', 'name']
      },
      removeEmpty: {
        tag: ['p']
      },
      keep: {
        tag: ['p', 'i', 'b', 'strong', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'img', 'blockquote', 'a', 'br', 'ul', 'li', 'code', 'em'],
        attr: ['src', 'href']
      },
      unwrap: {
        tag: ['section', 'span']
      },
      mustInsideParagraph: {
        tag: ['img', 'i', 'b', 'a']
      }
    },

    opts: {
      btnsDef: {
        upload: {
          func: function(params, tbw){
            var files,
            pfx = tbw.o.prefix;

            var $modal = tbw.openModalInsert(
              // Title
              tbw.lang.upload,

              // Fields
              {
                file: {
                  type: 'file',
                  required: true
                }
              },

              // Callback
              function(){
                var uploadFile = function (i) {
                  if (i >= files.length) {
                    setTimeout(function(){
                      tbw.closeModal();
                    }, 250);
                    return;
                  }
                  var data = new FormData();
                  data.append('fileToUpload', files[i]);

                  if($('.' + pfx +'progress', $modal).length === 0)
                    $('.' + pfx + 'modal-title', $modal)
                    .after(
                    $('<div/>', {
                      'class': pfx +'progress'
                    })
                    .append(
                      $('<div/>', {
                        'class': pfx +'progress-bar'
                      })
                    ));

                  $.ajax({
                    url:            $.trumbowyg.upload.serverPath,
                    type:           'POST',
                    data:           data,
                    cache:          false,
                    dataType:       'json',
                    processData:    false,
                    contentType:    false,

                    progressUpload: function(e){
                      $('.' + pfx + 'progress-bar').stop().animate({
                        width: Math.round(e.loaded * 100 / e.total / file.length + i * 100 / file.length) + '%'
                      }, 200);
                    },

                    success: function(data){
                      if(data.data.files[0].downloadUrl) {
                        tbw.execCmd('insertImage', data.data.files[0].downloadUrl);
                        uploadFile(i+1);
                      } else {
                        tbw.addErrorOnModalField(
                          $('input[type=file]', $modal),
                          tbw.lang[data.message]
                          );
                      }
                    },
                    error: function(){
                      console.log('error');
                      tbw.addErrorOnModalField(
                        $('input[type=file]', $modal),
                        tbw.lang.uploadError
                        );
                    }
                  });
                };
                uploadFile(0);
              }
            );

            $('input[type=file]').attr('multiple', true);

            $('input[type=file]').on('change', function(e){
              files = e.target.files;
            });
          }
        },

        brFilter: {
          func: function(params, tbw) {
            var htmlString = tbw.html();
            var $root = $('<div></div>');
            $root.html(htmlString);
            $root.find('br').remove();
            if($root.html() == '') $root.html(' ');
            tbw.html($root.html());
          }
        },

        htmlFilter: {
          func: function(params, tbw){
            var htmlString = tbw.html();
            var $root = $('<div></div>');
            $root.html(htmlString);
            if($.trumbowyg.htmlFilterRule){
              // tagMap
              if($.trumbowyg.htmlFilterRule.tagMap){
                var map = $.trumbowyg.htmlFilterRule.tagMap
                for(var from in map){
                  $.each($root.find(from), function(i, ele){
                    $(ele).replaceWith('<' + map[from] + '>' + $(ele).html() + '</' + map[from] + '>');
                  })
                }
              }
              // delete
              if($.trumbowyg.htmlFilterRule.delete){
                for(var target in $.trumbowyg.htmlFilterRule.delete){
                  var list = $.trumbowyg.htmlFilterRule.delete[target];
                  switch(target){
                    case 'tag': 
                    $.each(list, function(i, tag){$root.find(tag).remove();});
                    break;
                    case 'attr': 
                    $.each($root.find('*'), function(i, ele){
                      $.each(list, function(i, attr){$(ele).removeAttr(attr);});
                    });
                    break;
                  }
                }
              }
              // unwrap
              if($.trumbowyg.htmlFilterRule.unwrap){
                for(var target in $.trumbowyg.htmlFilterRule.unwrap) {
                  var list = $.trumbowyg.htmlFilterRule.unwrap[target];
                  switch(target){
                    case 'tag': 
                    $.each(list, function(i, tag){$root.find(tag).contents().unwrap();});
                    break;
                  }
                }
              }
              // keep
              if($.trumbowyg.htmlFilterRule.keep){
                for(var target in $.trumbowyg.htmlFilterRule.keep){
                  var list = $.trumbowyg.htmlFilterRule.keep[target];
                  var dict = arrayToDict(list);
                  switch(target){
                    case 'tag': 
                    $.each($root.find('*'), function(i, ele){
                      if(!(tagNameLowerCase($(ele)) in dict)){
                        $(ele).remove();
                      }
                    });
                    break;
                    case 'attr': 
                    $.each($root.find('*'), function(i, ele){
                      for(var attr in attrs($(ele))){
                        if(!(attr in dict)){
                          $(ele).removeAttr(attr);
                        }
                      }
                    });
                    break;
                  }
                }
              }
              // removeEmpty
              if($.trumbowyg.htmlFilterRule.removeEmpty){
                for(var target in $.trumbowyg.htmlFilterRule.removeEmpty){
                  var list = $.trumbowyg.htmlFilterRule.removeEmpty[target];
                  switch(target){
                    case 'tag': 
                    $.each(list, function(i, tag) {
                      var $eleList = $root.find(tag);
                      $.each($eleList, function(i, ele) {
                        if(!$(ele).html() || $(ele).html().trim() == '') {
                          $(ele).remove();
                        }
                      });
                    });
                    break;
                  }
                }
              }
              // mustInsideParagraph
              if($.trumbowyg.htmlFilterRule.mustInsideParagraph){
                $root.contents().filter(function(){
                  return this.nodeType == 3; 
                }).wrap("<p></p>");
                var list = $.trumbowyg.htmlFilterRule.mustInsideParagraph['tag'];
                $.each(list, function(i, tag) {
                  var $eleList = $root.find(tag);
                  $.each($eleList, function(i, ele) {
                    if($(ele).parent()[0] === $root[0]) {
                      $(ele).wrap("<p></p>");
                    }
                  });
                });
              }
            }
            if($root.html() == '') $root.html(' ');
            tbw.html($root.html());
          }
        }
      }
    }
  });

  function addXhrProgressEvent(){
    if (!$.trumbowyg && !$.trumbowyg.addedXhrProgressEvent) {   // Avoid adding progress event multiple times
      var originalXhr = $.ajaxSettings.xhr;
      $.ajaxSetup({
        xhr: function() {
          var req  = originalXhr(),
          that = this;
          if(req && typeof req.upload == "object" && that.progressUpload !== undefined)
            req.upload.addEventListener("progress", function(e){
              that.progressUpload(e);
            }, false);
          return req;
        }
      });
      $.trumbowyg.addedXhrProgressEvent = true;
    }
  }
})(jQuery);