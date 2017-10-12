function atoi(s) {
    return parseInt(s, 10);
}

function getRadioValue(form, radio) {
    for (var i = 0; i < form[radio].length; i++) {
        var item = form[radio][i];
        if (item.checked) return item.value;
    }
    return false;
}

function getsByTag(p, tag) {
    tag = tag.toUpperCase();
    var result = [];
    for (var i = 0; i < p.childNodes.length; i++) {
        if (p.childNodes[i].nodeName.toUpperCase() == tag) {
            result.push(p.childNodes[i]);
        }
    }
    return result;
}
function getsByCls(p, cls) {
    cls = cls.toUpperCase();
    var result = [];
    for (var i = 0; i < p.childNodes.length; i++) {
        if (p.childNodes[i].getAttribute('class').toUpperCase() == cls) {
            result.push(p.childNodes[i]);
        }
    }
    return result;
}
function getsByTagCls(p, tag, cls) {
    tag = tag.toUpperCase();
    cls = cls.toUpperCase();
    var result = [];
    for (var i = 0; i < p.childNodes.length; i++) {
        if (p.childNodes[i].nodeName.toUpperCase() == tag && p.childNodes[i].getAttribute('class').toUpperCase() == cls) {
            result.push(p.childNodes[i]);
        }
    }
    return result;
}

function funcProxy(func, obj) {
    return function() {
        func.apply(obj, arguments);
    }
}
function createDelegate(func, obj) {
    return function() {
        func.apply(obj, arguments);
    }
}
function trim(s) {
    return s.replace(/^\s+/g, "").replace(/\s+$/g, "");
}
function str_pad(s, length, fill) {
    s = s.toString();
    if (typeof (fill) == 'undefined') fill = '0';
    var d = length - s.length;
    if (d > 0) {
        for (var i = 0; i < d; i++) {
            s = fill + s;
        }
    }
    return s;
}
function addEvent(ele, type, handler) {
    if (ele.addEventListener) {
        ele.addEventListener(type, handler, false);
    }
    else {
        ele.attachEvent("on" + type, handler);
    }
}
function stopBubble(evt) {
    evt = evt || window.event;
    if (evt.preventDefault) {
        evt.preventDefault();
        evt.stopPropagation();
    }
    else {
        evt.cancelBubble = true;
        evt.returnValue = false;
    }
}
function addFavorite(title, url, desc) {
    if ((typeof window.sidebar == 'object') && (typeof window.sidebar.addPanel == 'function')) {
        window.sidebar.addPanel(title, url, desc);
    }
    else {
        window.external.AddFavorite(url, title);
    }
}
function cookies(name, value, options) {
    if (typeof value != 'undefined') {
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString();
        }
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var items = document.cookie.split(';');
            for (var i = 0; i < items.length; i++) {
                var cookie = (items[i] || "").replace(/^\s+|\s+$/g, "");
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
}
function copytextToClipboard(txt) {
    if (window.clipboardData) {
        window.clipboardData.clearData();
        window.clipboardData.setData('Text', txt);
    }
    else {
        alert('对不起，您使用的浏览器不支持该功能');
    }
}
function ajaxRequest(url, callback, data, charset) {
    if (typeof data == 'undefined')
        data = null;
    if (typeof charset == 'undefined')
        charset = 'UTF-8';

    var request = null;
    if (window.XMLHttpRequest) {
        request = new XMLHttpRequest();
    }
    else {
        var MSXMLs = ['MSXML2.XMLHTTP.6.0', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP.5.0', 'MSXML2.XMLHTTP.4.0', 'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP'];
        for (var n = 0; n < MSXMLs.length; n++) {
            try {
                request = new ActiveXObject(MSXMLs[n]);
                break;
            } catch (e) { };
        }
    }
    if (request != null) {
        request.onreadystatechange = function() {
            if (request.readyState == 4 && request.responseText) {
                callback(request.responseText);
                request = null;
            }
        };
        var method = data == null ? 'GET' : 'POST';
        request.open(method, url, true);
        if (method == 'POST') {
            request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=' + charset);
        }
        else {
            if (charset != 'UTF-8')
                request.setRequestHeader('charset', charset);
        }
        request.send(data);
    }
}
function loadjs(url, fCallback) {
    var script = document.createElement('script');
    script.setAttribute('type', 'text/javascript');
    script.setAttribute('src', url);
    var head = document.getElementsByTagName('head')[0];
    script.onload = script.onreadystatechange = function() {
        if (!this.readyState || this.readyState == "loaded" || this.readyState == "complete") {
            fCallback();
            head.removeChild(script);
            script = null;
        }
    };
    head.appendChild(script);
}
function pause(numberMillis) {
    var now = new Date();
    var exitTime = now.getTime() + numberMillis;
    while (true) {
        now = new Date();
        if (now.getTime() > exitTime) return;
    }
}
var g_statImg = new Image();
function stat(id) {
    var url = '/stat/stat.php?id=' + id;
    g_statImg.src = url;
}
function adjustImageSize(img, maxWidth, maxHeight) {
    var i = new Image();
    i.src = img.src;
    if (i.width > maxWidth || i.height > maxHeight) {
        var w1 = i.width / maxWidth;
        var h1 = i.height / maxHeight;
        if (w1 > h1) {
            img.width = maxWidth;
        }
        else {
            img.height = maxHeight;
        }
    }
    i = null;
}

function getStyle(elem, name) {
    var ret = null;
    if (elem.style[name])
        ret = elem.style[name];
    else if (elem.currentStyle) {
        ret = elem.currentStyle[name];
    }
    else if (document.defaultView && document.defaultView.getComputedStyle) {
        name = name.replace(/([A-Z])/g, "-$1");
        name = name.toLowerCase();
        var s = document.defaultView.getComputedStyle(elem, "");
        ret = s && s.getPropertyValue(name);
    }
    if (ret == 'auto') {
        if (name == 'width')
            ret = elem.offsetWidth;
        else if (name == 'height')
            ret = elem.offsetHeight;
    }
    return ret;
}
function showTopElement(ele, type) {
    if (typeof type == 'undefined') type = '';
    ele = $(ele)[0];
    ele.style.display = type;

    var iframe = $(ele.id + '_iframe')[0];
    if (iframe == null) return;
    iframe.style.zIndex = parseInt(getStyle(ele, 'zIndex')) - 1;
    var w = parseInt(getStyle(ele, 'width')) +
		parseInt(getStyle(ele, 'paddingLeft')) + parseInt(getStyle(ele, 'paddingRight')) + 2;
    var h = iframe.style.height = parseInt(getStyle(ele, 'height')) +
		parseInt(getStyle(ele, 'paddingTop')) + parseInt(getStyle(ele, 'paddingBottom')) + 5;
    var data = new UserData(ele.getAttribute('dd'));
    w = w + parseInt(data.get('widthDelta', 0));
    h = h + parseInt(data.get('heightDelta', 0));
    iframe.style.width = w + 'px';
    iframe.style.height = h + 'px';
    iframe.style.display = 'block';
}
function hideTopElement(ele) {
    ele = $(ele)[0];
    ele.style.display = 'none';

    var iframe = $(ele.id + '_iframe')[0];
    if (iframe == null) return;
    iframe.style.display = 'none';
}
function toggleTopElement(ele, type) {
    if (getStyle(ele, 'display') == 'none')
        showTopElement(ele, type);
    else
        hideTopElement(ele);
}
function hasClass(ele, cls) {
    return ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}
function addClass(ele, cls) {
    if (!this.hasClass(ele, cls)) ele.className += " " + cls;
}
function removeClass(ele, cls) {
    if (hasClass(ele, cls)) {
        var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
        ele.className = ele.className.replace(reg, ' ');
    }
}


function jsTemplate(tpl, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9) {
    var re = /{&([^}]+)}/;
    var m = re.exec(tpl);
    while (m != null && m.length > 0) {
        for (var i = 1; i < arguments.length; i++) {
            var val = eval('obj' + i + '.' + m[1]);
            if (typeof val != 'undefined') break;
        }
        if (typeof val == 'undefined') alert(m[1] + ' not defined!');
        tpl = tpl.replace(m[0], val);
        m = re.exec(tpl);
    }
    return tpl;
}
/*
Object.prototype.Clone = function(){
var objClone;
if(this.constructor == Object)
objClone = new this.constructor();
else
objClone = new this.constructor(this.valueOf());
for(var key in this){
if(objClone[key] != this[key]){
if(typeof(this[key]) == 'object'){
objClone[key] = this[key].Clone();
}
else{
objClone[key] = this[key];
}
}
}
objClone.toString = this.toString;
objClone.valueOf = this.valueOf;
return objClone;
}
*/
function formSubmitData(action, params, datas, method, target) {
    if (typeof method == 'undefined') method = 'post';
    var form = document.createElement('form');
    form.method = method;
    form.action = action;
    if (typeof target == 'undefined') target = '_self';
    form.target = target;
    if (typeof target == '_blank') target = '_blank';
    form.target = target;

    for (var i = 0; i < params.length; i++) {
        var input = document.createElement('input');
        input.type = 'hidden';
        input.name = params[i];
        input.value = datas[i];
        form.appendChild(input);
    }
    document.body.appendChild(form);
    form.submit();
    form = null;
}
function showMask() {
    var mask = $('#pageMask')[0];
    var maskDiv = $('#hezc_bg')[0];
    maskDiv.style.height = mask.style.height = cssHelper.getMaxHeight();
    $('#pageMask,#hezc_bg').show();
}
function hideMask() {
    $('#pageMask,#hezc_bg').hide();
}
function showDialog(dlg) {
    showMask();
    var el = $('#' + dlg);
    el.show();
    el.css("top", ((parseInt($(window).height()) / 2) - (el.height() / 2) + $(document).scrollTop()) + "px");
    el.css("left", ((parseInt($(window).width()) / 2) - (el.width() / 2)) + "px");
}
function hideDialog(dlg) {
    hideMask();
    $('#' + dlg).hide();
}
function showDlgTpl(title, tplId) {
    showDlg(title, $('#' + tplId)[0].innerHTML);
}
function showDlg(title, content) {
    var el = $('#dlg')[0];
    if (el) {
        showMask();
        $('#dlgTitle')[0].innerHTML = title;
        $('#dlgContent')[0].innerHTML = content;
        el.style.display = 'block';
        cssHelper.centerElement(el);
    }
}
function hideParentDlg(el) {
    while ((el = el.parentNode) != null) {
        if (el.nodeName == 'FORM' || el.nodeName == 'DIV') {
            hideDialog(el);
            return;
        }
    }
}
/*
function showBlock(id, type){
if(typeof type == 'undefined') type = 'block';
var el = $(id)[0];
el.style.display = type;
var iframe = $(id + '_iframe')[0];
if(iframe != null){
iframe.style.zIndex = parseInt(cssHelper.getStyle(el, 'zIndex'))-1;
var h = cssHelper.getFullHeight(el);
var w = cssHelper.getFullWidth(el);
var x = cssHelper.getPageX(el);
var y = cssHelper.getPageY(el);
iframe.style.height = h + 'px';
iframe.style.width = w + 'px';
iframe.style.left = x + 'px';
iframe.style.top = y + 'px';
iframe.style.display = 'block';
}
}
function hideBlock(id){
$(id)[0].style.display = 'none';
var iframe = $(id + '_iframe')[0];
if(iframe != null) iframe.style.display = 'none';
}
*/

function showToolTip(s, x, y) {
    document.getElementById('bubble_tooltip_content').innerHTML = s;
    var obj = document.getElementById('bubble_tooltip');
    obj.style.display = 'block';
    obj.style.left = x - $('#bubble_tooltip_content').outerWidth() - 40 + 'px';
    obj.style.top = y + 'px';
}
function hideToolTip() {
    var tip = document.getElementById('bubble_tooltip');
    if (tip) tip.style.display = 'none';
}

//addEvent(document, 'click', function() {
//    hideToolTip();
//});


var cssHelper = {
    getStyle: function(elem, name) { // 获取最终属性值
        var ret = null;
        if (elem.style[name])
            ret = elem.style[name];
        else if (elem.currentStyle) {
            ret = elem.currentStyle[name];
        }
        else if (document.defaultView && document.defaultView.getComputedStyle) {
            name = name.replace(/([A-Z])/g, "-$1");
            name = name.toLowerCase();
            var s = document.defaultView.getComputedStyle(elem, "");
            ret = s && s.getPropertyValue(name);
        }
        if (ret == 'auto') {
            if (name == 'width')
                ret = elem.offsetWidth;
            else if (name == 'height')
                ret = elem.offsetHeight;
        }
        return ret;
    },
    getPageWidth: function() { // 获取页面宽度
        return document.body.scrollWidth;
    },
    getPageHeight: function() { // 获取页面高度
        return document.body.scrollHeight;
    },
    getWindowWidth: function() { // 获取视口宽度
        var de = document.documentElement;
        return self.innerWidth ||
			(de && de.clientWidth) ||
			document.body.clientWidth;
    },
    getWindowHeight: function() { // 获取视口高度
        var de = document.documentElement;
        return self.innerHeight ||
			(de && de.clientHeight) ||
			document.body.clientHeight;
    },

    getPageX: function(elem) { // 获取相对于整个文档的X位置
        return elem.offsetParent ? elem.offsetLeft + this.getPageX(elem.offsetParent) : elem.offsetLeft;
    },
    getPageY: function(elem) { // 获取相对于整个文档的Y位置
        return elem.offsetParent ? elem.offsetTop + this.getPageY(elem.offsetParent) : elem.offsetTop;
    },
    getScrollX: function() { // 获取相对视口的X
        var de = document.documentElement;
        return self.pageXOffset ||
			(de && de.scrollLeft) ||
			document.body.scrollLeft;
    },
    getScrollY: function() { // 获取相对视口的Y
        var de = document.documentElement;
        return self.pageYOffset ||
			(de && de.scrollTop) ||
			document.body.scrollTop;
    },
    getX: function(e) { // 获取鼠标相对于整个页面的位置X
        e = e || window.event;
        return e.pageX || e.clientX + document.body.scrollLeft;
    },
    getY: function(e) { // 获取鼠标相对于整个页面的位置Y
        e = e || window.event;
        return e.pageY || e.clientY + document.body.scrollTop;
    },
    getElementX: function(e) { // 获取鼠标相对于当前元素的位置X
        return (e && e.layerX) || window.event.offsetX;
    },
    getElementY: function(e) { // 获取鼠标相对于当前元素的位置Y
        return (e && e.layerY) || window.event.offsetY;
    },
    setOpacity: function(elem, level) { // 设置透明度
        if (elem.filters)
            elem.style.filters = 'alpha(opacity=' + level + ')';
        else
            elem.style.opacity = level / 100;
    },
    resetCSS: function(elem, prop) {
        var old = {};
        for (var i in prop) {
            old[i] = elem.style[i];
            elem.style[i] = prop[i];
        }
        return old;
    },
    restoreCSS: function(elem, prop) {
        for (var i in prop) {
            elem.style[i] = prop[i];
        }
    },
    show: function(elem) { // 显示元素
        elem.style.display = elem.$oldDisplay || '';
    },
    getMaxHeight: function() {
        return Math.max(this.getPageHeight(), this.getWindowHeight());
    },

    // 扩展方法

    hide: function(elem) { // 隐藏元素
        var curDisplay = this.getStyle(elem, 'display');
        if (curDisplay != 'none')
            elem.$oldDisplay = curDisplay;
        elem.style.display = 'none';
    },
    getWidth: function(ele) { // 获取元素宽度
        return parseInt(this.getStyle(ele, 'width'));
    },
    getHeight: function(ele) { // 获取元素高度
        return parseInt(this.getStyle(ele, 'height'));
    },
    getLeft: function(ele) { // 获取元素宽度
        return parseInt(this.getStyle(ele, 'left'));
    },
    getTop: function(ele) { // 获取元素宽度
        return parseInt(this.getStyle(ele, 'top'));
    },
    getParentX: function(elem) { // 获取相对于父元素的X位置
        return elem.parentNode == elem.offsetParent ? elem.offsetLeft : this.getPageX(elem) - this.getPageX(elem.parentNode);
    },
    getParentY: function(elem) { // 获取相对于父元素的Y位置
        return elem.parentNode == elem.offsetParent ? elem.offsetTop : this.getPageY(elem) - this.getPageY(elem.parentNode);
    },
    getFullHeight: function(elem) { // 获取元素完整高度
        if (this.getStyle(elem, "display") != 'none')
            return elem.offsetHeight || this.getHeight(elem);
        var old = this.resetCSS(elem, {
            display: '',
            visibility: 'hidden',
            position: 'absolute'
        });
        var h = elem.clientHeight || this.getHeight(elem);
        this.restoreCSS(elem, old);
        return h;
    },
    getFullWidth: function(elem) { // 获取元素完整宽度
        if (this.getStyle(elem, "display") != 'none')
            return elem.offsetWidth || this.getWidth(elem);
        var old = this.resetCSS(elem, {
            display: '',
            visibility: 'hidden',
            position: 'absolute'
        });
        var w = elem.clientWidth || this.getWidth(elem);
        this.resetCSS(elem, old);
        return w;
    },

    // 高级扩展

    centerElement: function(ele, heightOffset) { // 元素居中
        if (typeof heightOffset == 'undefined')
            heightOffset = 0;
        var viewWidth = this.getWindowWidth();
        var eleWidth = this.getWidth(ele);
        var viewHeight = this.getWindowHeight();
        var eleHeight = this.getHeight(ele);
        ele.style.left = eleWidth < viewWidth ? Math.round((viewWidth - eleWidth) / 2) + 'px' : '0px';
        if (eleHeight < viewHeight) {
            var h = Math.round((viewHeight - eleHeight) / 2) + heightOffset;
            h += parseInt(this.getScrollY());
            if (h < 0) h = 0;
            ele.style.top = h + 'px';
        }
        else {
            ele.style.top = '0px';
        }
    },

    slideDown: function(elem) { // 滑动
        var h = this.getFullHeight(elem);
        elem.style.height = '0px';
        this.show(elem);
        for (var i = 0; i <= 100; i += 5) {
            (function() {
                var pos = i;
                setTimeout(function() {
                    elem.style.height = (pos / 100 * h) + 'px';
                }, (pos + 1) * 10);
            })();
        }
    },

    fadeIn: function(elem) { // 渐显
        this.setOpacity(elem, 0);
        this.show(elem);
        for (var i = 0; i <= 100; i += 5) {
            (function() {
                var pos = i;
                setTimeout(function() {
                    this.setOpacity(elem, pos);
                }, (pos + 1) * 10);
            })();
        }
    },

    // opt说明
    // opt.obj 用自定义对象
    // opt.mask 遮罩iframe对象，注意：遮罩的iframe需用户自己定义
    showDropDown: function(target, dropdown, opt) {
        if (typeof opt == 'undefined') {
            opt = { obj: null, evt: null, close: true };
        }
        else {
            opt.obj = opt.obj || null;
            opt.close = opt.close || true;
            if (opt.evt) stopBubble(opt.evt);
        }
        var x = this.getPageX(target) + 'px';
        var y = this.getPageY(target) + this.getFullHeight(target) + 'px';
        dropdown.style.left = x;
        dropdown.style.top = y;
        dropdown.style.display = 'block';
        dropdown.target = target;
        dropdown.obj = opt.obj;
        var mask = $(dropdown.id + '_mask')[0];
        dropdown.mask = mask;
        if (mask) {
            var h = cssHelper.getFullHeight(dropdown) + 'px';
            var w = cssHelper.getFullWidth(dropdown) + 'px';
            mask.style.zIndex = parseInt(cssHelper.getStyle(dropdown, 'zIndex')) - 1;
            mask.style.width = w;
            mask.style.height = h;
            mask.style.left = x;
            mask.style.top = y;
            mask.style.display = 'block';
        }
        if (opt.close) {
            addEvent(document, 'click', function() {
                cssHelper.hideDropDown(dropdown);
            });
        }
    },

    hideDropDown: function(dropdown) {
        if (dropdown.mask) {
            dropdown.mask.style.display = 'none';
            dropdown.mask = null;
        }
        dropdown.style.display = 'none';
    },

    toggleDropDown: function(target, dropdown, opt) {
        if (this.getStyle(dropdown, 'display') == 'none') {
            this.showDropDown(target, dropdown, opt);
        }
        else {
            this.hideDropDown(dropdown);
        }
    }
};

function showCanvas(el, ref) {
    ref = $(ref);
    var offset = ref.offset();
    var x = offset.left + 'px';
    var y = offset.top + ref.outerHeight() + 'px';
    showWithMask(el, x, y);
}

function hideCanvas(el) {
    hideWithMask(el);
}

function showWithMask(el, x, y) {
    el = $(el);
    var mask = $('#' + el.attr('id') + '_mask');
    mask.width(el.outerWidth()).height(el.outerHeight()).css('zIndex', el.css('zIndex') - 1);
    mask.css('left', x).css('top', y).show();
    el.css('left', x).css('top', y).show();
}
function hideWithMask(el) {
    el = $(el);
    var mask = $('#' + el.attr('id') + '_mask');
    mask.hide();
    el.hide();
}

function emptyHint(input, hint) {
    input = $(input);
    input.data('hint', hint);
    if ($.trim(input.val()) == '') {
        input.addClass('gray');
        input.val(hint);
    }
    input.blur(function() {
        var input = $(this);
        if ($.trim(input.val()) == '') {
            input.addClass('gray');
            input.val(input.data('hint'));
        }
    });
    input.focus(function(evt) {
        var input = $(this);
        if ($.trim(input.val()) == input.data('hint')) {
            input.removeClass('gray');
            input.val('');
        }
    });
}

function wordLimit(s, len) {
    if (s.length > len) {
        s = s.substr(0, len - 1) + '...';
    }
    return s;
}

// 新的js库函数

var app = {};

// 对话框

app.showPageMask = function() {
    var h = Math.max($(document).height(), $(window).height());
    $('#PageMask').css('height', h + 'px').show();
}
app.hidePageMask = function() {
    $('#PageMask').hide();
}
app.centerEl = function(el) {
    var top = ($(window).height() - el.outerHeight()) / 2 + $(document).scrollTop();
    var left = ($(window).width() - el.width()) / 2;
    if (top > 50) top -= 50;
    if (top < 0) top = 0;
    if (left < 0) left = 0;
    el.css('top', top + 'px');
    el.css('left', left + 'px');
}
app.showDialog = function(el) {
    if (this.currDlg != undefined) {
        this.hideDialog(this.currDlg);
    }
    this.showPageMask();
    el.show();
    this.currDlg = el;
    this.centerEl(el);
}
app.hideDialog = function(el) {
    if (el == undefined) {
        el = this.currDlg;
    }
    if (el != undefined) {
        this.hidePageMask();
        el.hide();
        this.currDlg = undefined;
    }
}
app.showCanvas = function() {
}
app.hideCanvas = function() {
}

// 字符串处理

app.strPad = function(s, length, fill) {
    s = s.toString();
    if (typeof (fill) == 'undefined') fill = '0';
    var d = length - s.length;
    if (d > 0) {
        for (var i = 0; i < d; i++) {
            s = fill + s;
        }
    }
    return s;
}

app.startsWith = function(s, needle) {
    return s.indexOf(needle) == 0;
}
app.contains = function(s, needle) {
    return s.indexOf('/') != -1;
}

// 数据格式

app.isDateStr = function(s) {
    var r = s.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
    if (r == null) return false;
    var d = new Date(r[1], r[3] - 1, r[4]);
    var num = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]);
    return (num != 0);
}

app.isIntStr = function(s) {
}

app.formatDate = function(s) {
    
    var re = /(\d+)-(\d+)-(\d+)/;
    var m = s.match(re);
    var year = m[1];
    if (year.length == 2) {
        year = '20' + year;
    }
    var month = this.strPad(m[2], 2, 0);
    var day = this.strPad(m[3], 2, 0);
    return year + '-' + month + '-' + day;
}

app.dateToInt = function(s) {
    s = this.formatDate(s);
    s = s.replace(/-/g, '');
    return parseInt(s, 10);
}

app.dateInput = function(el) {
    el.keypress(function(evt) {
        if ((evt.which >= 48 && evt.which <= 57 && evt.shiftKey == false)
			|| evt.which == 0 || evt.which == 8 || evt.which == 45) {
            return true;
        }
        return false;
    });
}

app.intInput = function(el) {
    el.keypress(function(evt) {
        if ((evt.which >= 48 && evt.which <= 57 && evt.shiftKey == false)
			|| evt.which == 0 || evt.which == 8) {
            return true;
        }
        return false;
    });
}

app.isEmail = function(s) {
    //return /\w+@\w+\.[a-zA-Z]+/.test(s);
    return /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(s);
}

//手机号验证
app.isMobile = function(s) {
if (s.length == 11) {
    var reg0 = /^1\d{10}$/;
    if (reg0.test(s)) {
        return true;
    }
    else {
        return false;
    }
}
else {
    return false;
}
}

app.isDate = function(s) {
    var r = s.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
    if (r == null) return false;
    var d = new Date(r[1], r[3] - 1, r[4]);
    var num = (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]);
    return (num != 0);
}
app.hasHanzi = function(s) {
    return /[\u4e00-\u9fa5]/.test(s);
}

app.getToday = function() {
    var now = new Date();
    var ret = '' + now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();
    ret = app.formatDate(ret);
    return ret;
}

//为空验证
app.isNull = function(obj) {
    var vObj = $.trim(obj);
    if (vObj == '') {
        return true;
    }
    else {
        return false;
    }
}
//两个值是否相等
app.isEqual = function(objOne, objTwo) {
    var vOne = $.trim(objOne);
    var vTwo = $.trim(objTwo);
    if (vOne == vTwo) {
        return false;
    }
    else {
        return true;
    }
}
//长度的限制(需要判断的长度、长度1、长度2)
//如果iOne为空，则比较的是obj长度小于iTwo
//如果iTwo为空，则比较的是obj长度大于iOne
//如果iOne与iTwo都不为空，则比较的是obj长度大于iOne小于iTWO
app.isLimitLength = function(obj, iOne, iTwo) {
    var vObj = $.trim(obj).length;
    if ((iOne == '') && (iTwo != '')) {
        if (vObj < iTwo) {
            return false;
        }
        else {
            return true;
        }
    }
    else if ((iTwo == '') && (iOne != '')) {
        if (vObj > iOne) {
            return false;
        }
        else {
            return true;
        }
    }
    else if ((iOne != '') && (iTwo != '')) {
        if ((vObj) >= iOne && (vObj <= iTwo)) {
            return false;
        }
        else {
            return true;
        }
    }
}

//固定电话的验证
app.isTelephone = function(obj) {
    var vObj = $.trim(obj);
    //var reg=/(\d{2,5}-\d{7,8})/; //固定电话例如：010-12345678
    var reg = /^[0-9]/;  //只能是数字
    if (reg.test(vObj)) {
        return true;
    }
    else {
        return false;
    }
}
// 表示电话
app.isPhoneShow = function(obj) {
    var vObj = $.trim(obj);
    var reg = /\(?[046]\d{2}\)?[- ]?\d{8}|\d{7}|0\d{2}[- ]?\d{8}/;
    // var re = new RegExp(reg);
    if (reg.test(vObj)) {
        return true;
    }
    else {
        return false;
    }
}

// 表示区号的验证
app.isQuNo = function(obj) {
    var vObj = $.trim(obj);
    var reg = /^[0]([1-9]){1,1}([0-9]){1,2}/;  //表示匹配区号
    if (reg.test(vObj)) {
        return true;
    }
    else {
        return false;
    }
}

// 表示区号后面的电话 7或8位
app.isPhoneNo = function(obj) {
    var vObj = $.trim(obj);
    var reg = /^[1-9]([0-9]){6,7}/;  //只能是数字
    if (reg.test(vObj)) {
        return true;
    }
    else {
        return false;
    }
}


//判断是否汉字
function isChina(c) {
    if ((c >= 0x0001 && c <= 0x007e) || (0xff60 <= c && c <= 0xff9f)) {
        return false;
    }
    else {
        return true;
    }

}


//乘机人姓名校验
app.isFlighter = function(Pname) {
    ////debugger;
    Pname = $.trim(Pname);
    if (Pname.indexOf('/', 0) > -1) {
        fristandsecdName = Pname.split("/");
        if (fristandsecdName.length > 2) return false;
        if (!CheckEnStr(fristandsecdName[0])) return false;
        if (!CheckEnStr(fristandsecdName[1])) return false;
    } else {
        if (CheckAllCnStr(Pname))//如果全部是汉字
        {
            return true;
        }
        else {
            var n = 0;
            for (var i = 0; i < Pname.length; i++) {
                if (isChina(Pname.charCodeAt(i))) {
                    continue;
                }
                n = i;
                break;
            }

            if (n == 0)//说明没有汉字也没有'/' 返回False
            {
                return false;
            }

            //如果是在以后还有汉字的时候返回false;
            for (var i = n; i < Pname.length; i++) {
                if (isChina(Pname.charCodeAt(i))) {
                    return false;
                    break;
                }
            }

        }
    }
    return true;
}

function CheckEnStr(str) {
    var filter = /^[a-zA-Z ]+$/;
    if (str == '') return false;
    if (filter.test(str)) return true;
    return false;
}

function CheckAllCnStr(str) {
    var filter = /[^\u4e00-\u9fa5]/;
    if (str == '') return false;
    if (!filter.test(str)) return true;
    return false;
}

//邮政编码
app.isZip = function(obj) {
    var regZip = /^\d{6}$/g;
    var _obj = $.trim(obj);
    if (regZip.test(_obj)) {
        return true;
    }
    else {
        return false;
    }
}

//大于0的正整数
app.isZhenShu = function(obj) {
    var reg = /^[1-9]([0-9])*$/;
    if (reg.test(obj)) {
        return true;
    }
    else {
        return false;
    }
}
// 包括0的正整数
app.isZhenShuL = function(obj) {
    var reg = /^[0-9]([0-9])*$/;
    if (reg.test(obj)) {
        return true;
    }
    else {
        return false;
    }
}

//登录名称判断
app.isLogName = function(obj) {
    var reg = /^[A-Za-z0-9]{1,20}$/;
    if (reg.test(obj)) {
        return true;
    }
    else {
        return false;
    }
}

// 证件有效日期比较
app.CompareDate = function(objDate) {
    var nowDate = new Date();
    var year = nowDate.getFullYear();
    var month = nowDate.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    var nowDay = nowDate.getDate();
    if (nowDay < 10) {
        nowDay = "0" + nowDay;
    }
    var DateNow = year + "-" + month + "-" + nowDay;
    var LDate = DateNow.replace(/-/g, "/"); //表示现在
    var CDate = objDate.replace(/-/g, "/"); ;
    var vNow = Date.parse(LDate);
    var vYx = Date.parse(CDate);
    if (vNow > vYx) {
        return true;
    }
    else {
        return false;
    }
}

// 表示OFFICE号
app.Office = function(objValue) {
    objValue = $.trim(objValue);
    if (objValue.length == 6) {
        var reg = /^[A-Za-z]{3}\d{3}$/;
        if (reg.test(objValue)) {
            return true;
        }
        else {
            return false;
        }
    }
    else {
        return false;
    }
}

// 表示QQ号码
app.isQQ = function(objValue) {
    if ($.trim(objValue).length < 5) {
        return false;
    }
    else {
        var reg = /^[1-9]([0-9])*$/;
        if (reg.test($.trim(objValue))) {
            return true;
        }
        else {
            return false;
        }
    }
}
// 表示网站域名
app.IsURL = function(str_url) {

    var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
         + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@  
         + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184  
         + "|" // 允许IP和DOMAIN（域名） 
         + "([0-9a-z_!~*'()-]+\.)*" // 域名- www.  
         + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名  
         + "[a-z]{2,6})" // first level domain- .com or .museum  
         + "(:[0-9]{1,4})?" // 端口- :80  
         + "((/?)|" // a slash isn't required if there is no file name  
         + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
    var re = new RegExp(strRegex);
    if (re.test(str_url)) {
        return (true);
    } else {
        return (false);
    }
}

// 表示端口号
app.IsPort = function(obj) {
    var strRegex = "/^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/";
    var re = new RegExp(strRegex);
    if (re.test(obj)) {
        return (true);
    } else {
        return (false);
    }
}


//取得浏览器视窗高度
function getBrowserHeight() {
    if ($.browser.msie) {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight :
	                 document.body.clientHeight;
    } else {
        return self.innerHeight;
    }
}

//取得浏览器视窗宽度
function getBrowserWidth() {
    if ($.browser.msie) {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth :
	                 document.body.clientWidth;
    } else {
        return self.innerWidth;
    }
}

function getPageScroll() {
    var xScroll, yScroll;
    if (self.pageYOffset) {
        yScroll = self.pageYOffset;
        xScroll = self.pageXOffset;
    } else if (document.documentElement && document.documentElement.scrollTop) {     // Explorer 6 Strict  
        yScroll = document.documentElement.scrollTop;
        xScroll = document.documentElement.scrollLeft;
    } else if (document.body) {// all other Explorers  
        yScroll = document.body.scrollTop;
        xScroll = document.body.scrollLeft;
    }
    arrayPageScroll = new Array(xScroll, yScroll);
    return arrayPageScroll;
};
 