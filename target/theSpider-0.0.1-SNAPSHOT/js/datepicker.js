var holiday = ["2015-09-27","2015-10-01","2015-01-01","2016-02-08","2016-02-22","2016-04-04","2016-05-01","2016-06-09",
"2016-09-15","2016-10-01","2016-01-01","2017-01-28","2017-02-11","2017-04-04","2017-05-01","2017-05-30",
"2017-10-04","2017-10-01","2017-01-01","2018-02-16","2018-03-02","2018-04-05","2018-05-01","2018-06-18",
"2018-09-24","2018-10-01","2018-01-01","2019-02-05","2019-02-19","2019-04-05","2019-05-01","2019-06-07",
"2019-09-13","2019-10-01","2019-01-01","2020-01-25","2020-02-08","2020-04-04","2020-05-01","2020-06-25",
"2020-10-01","2020-10-01","2020-01-01"];
var Datepicker = function(b, a, c) {
    this.target = b || null;
    this.eachW = a.eachW || 177;
    this.eachH = a.eachH || 178;
    this.year = a.year || new Date().getFullYear();
    this.month = a.month || (new Date().getMonth() + 1);
    this.day = a.day || new Date().getDate();
    try {
        this.startDate = a.startDate.split('-') || [];
    } catch (e) {
        this.startDate = a.startDate || [];
    }
    this.endDate = a.endDate || [];
    this.getsDatefrom = a.getsDatefrom || null;
    this.ttMonth = a.ttMonth || 2;
    this.direct = a.direct || "";
    this.fromtarget = a.fromtarget || false;
    this.targetValue = null; this.selected = [];
    this.isclick = false;
    this.addDays = a.addDays || 0;
    this.cday = 0; this.isopen = false;
    this.inputs = [];
    this.callback = a.callback || null;
    this.mindayadd=a.mindayadd || 0;
    this._init();
    this.dttype = a.dttype || 'depturedt'; //日期类型 depturedt（起飞日期），arrivedt(到达日期)
    this.deptureinput = c || null; //如果日期类型为到达日期时，这里就可以传入起飞日期的input
};

Datepicker.prototype = {
    _init: function() {
        this._startEvent()
    },
    _createPanel: function() {
        var b = MGTOOL.$id("dp_id");
        if (b) { return } else {
            var a = document.createElement("div");
            a.className = "datepicker";
            a.id = "dp_id";
            a.innerHTML = '<div class="pickerwrap"><em class="prevMonth"></em><em class="nextMonth"></em><div class="pickwidth" id="pickwidth"> </div> </div><iframe frameborder="0" marginheight="0" marginwidth="0" id="dateframe" style="position:absolute; left:-8px; top:-8px; overflow:hidden; background:#fff; border:0; z-index:-1; opacity:0; filter:alpha(opacity = 0);"></iframe></div>';
            document.body.appendChild(a);
        }
    },
    _getCalendar: function(d) {
        this.year = d.getFullYear(), this.month = d.getMonth(), this.day = d.getDate();
        var f = this.ttMonth, b = [];
        var c = new Date(this.year, this.month, this.day);
        for (var e = 0; e < f; e++) {
            b[e] = this._getDayList(c, e);
            var a = new Date(c.getFullYear(), c.getMonth() + e + 1, 0).getDate();
            c.setFullYear(c.getFullYear(), c.getMonth(), 1 + a)
        } MGTOOL.$id("pickwidth").innerHTML = b.join("");
        MGTOOL.$id("dp_id").style.width = f * this.eachW + "px"; MGTOOL.$id("dateframe").style.width = (f * this.eachW + 8) + "px"; MGTOOL.$id("dateframe").style.height = 188 + "px"
    },
    _getDayList: function(v, c) {
        var d = ["日", "一", "二", "三", "四", "五", "六"];
        var t = (this.fromtarget) ? this.targetValue[2] : this.startDate[2] || new Date().getDate();
        var e = (this.fromtarget) ? this.targetValue[1] - 1 : this.startDate[1] || new Date().getMonth();
        var b = (this.fromtarget) ? this.targetValue[0] : this.startDate[0] || new Date().getFullYear();
        var z = v.getMonth(); var m = v.getFullYear();
        var l = this.cday;
        var h = (this.fromtarget) ? this._operaAddDay(this.addDays, new Date(this.targetValue[0], (this.targetValue[1] - 1), this.targetValue[2])) : this._operaAddDay(this.addDays);
        var q = this.endDate[0] || h[0];
        var g = this.endDate[1] || h[1];
        var r = this.endDate[2] || h[2];
        if (this.endDate.length && (new Date(h[0], h[1], h[2]) < new Date(this.endDate[0], this.endDate[1], this.endDate[2]))) {
            q = h[0]; g = h[1]; r = h[2];
        };
        var a = [], f, u, s = [], w;
        f = new Date(m, z, 1).getDay();
        u = new Date(m, z + 1, 0).getDate();
        for (var p = 1; p <= f; p++) { a.push(0) };
        for (var p = 1; p <= u; p++) { a.push(p) };
        if (!Array.prototype.isIn) {
            Array.prototype.isIn = function(j) {
                var i = this.length;
                while (i--) {
                    if (this[i] == j) { return true }
                };
                return false;
            };
        };
        var deptureY = parseInt(this.deptureinput.value.split('-')[0]);
        var deptureM = parseInt(this.deptureinput.value.split('-')[1]);
        var deptureD = parseInt(this.deptureinput.value.split('-')[2]);
        for (var o = 0, n = a.length; o < n; o++) {
            if ((b > m && a[o]) || ((b == m) && (e > z) && a[o]) || ((b == m) && (e == z) && a[o] && (a[o] < t)) || (q < m && a[o]) || ((q == m) && (g < z + 1) && a[o]) || ((q == m) && (g == z + 1) && a[o] && (r < a[o])) || ((this.dttype == 'arrivedt') && deptureY > m && deptureD) || ((this.dttype == 'arrivedt') && (deptureY == m) && (deptureM > z + 1) && deptureD) || ((this.dttype == 'arrivedt') && (deptureY == m) && (deptureM == z + 1) && deptureD && (a[o] < deptureD+this.mindayadd))) {
                var temp_ = a[o];
                if (temp_ == '0') {
                    temp_ = "&nbsp;";
                }
                s.push("<li>" + temp_ + "</li>")
            } else {
                if (holiday && holiday.isIn((m + "-" + ((z + 1) < 10 ? ("0" + (z + 1)) : (z + 1)) + "-" + (a[o] < 10 ? ("0" + a[o]) : a[o])))) {
                    s.push('<li><a href="javascript:void(0);" class="hld hl' + m + "" + ((z + 1) < 10 ? ("0" + (z + 1)) : (z + 1)) + "" + (a[o] < 10 ? ("0" + a[o]) : a[o]) + '" title="' + m + "-" + ((z + 1) < 10 ? ("0" + (z + 1)) : (z + 1)) + "-" + (a[o] < 10 ? ("0" + a[o]) : a[o]) + '">&nbsp;</a></li>')
                } else {
                    if ((b == m) && (e == z) && a[o] && a[o] == t) {
                        s.push('<li><a href="javascript:void(0);" class="now" title="' + m + "-" + ((z + 1) < 10 ? ("0" + (z + 1)) : (z + 1)) + "-" + (a[o] < 10 ? ("0" + a[o]) : a[o]) + '">' + a[o] + "</a></li>")
                    } else {
                        if ((m == this.selected[0]) && (z == this.selected[1]) && a[o] && a[o] == l) {
                            s.push('<li><a href="javascript:void(0);" class="choice" title="' + m + "-" + ((z + 1) < 10 ? ("0" + (z + 1)) : (z + 1)) + "-" + (a[o] < 10 ? ("0" + a[o]) : a[o]) + '">' + a[o] + "</a></li>")
                        } else {
                            if (a[o]) {
                                s.push('<li><a href="javascript:void(0);" title="' + m + "-" + ((z + 1) < 10 ? ("0" + (z + 1)) : (z + 1)) + "-" + (a[o] < 10 ? ("0" + a[o]) : a[o]) + '">' + a[o] + "</a></li>")
                            } else {
                                s.push("<li>&nbsp;</li>")
                            }
                        }
                    }
                }
            }
        } if (c != 0) {
            w = '<div class="pickerbody"><div class="pickhead"><strong>' + m + "年" + (z + 1) + '月</strong></div><div class="pickweek wbleft"><span class="wkfont">' + d[0] + "</span><span>" + d[1] + "</span><span>" + d[2] + "</span><span>" + d[3] + "</span><span>" + d[4] + "</span><span>" + d[5] + '</span><span class="wkfont">' + d[6] + '</span></div><div class="dateswrap bleft"><ul>' + s.slice(0).join("") + "</ul></div></div>";
        } else {
            w = '<div class="pickerbody"><div class="pickhead"><strong>' + m + "年" + (z + 1) + '月</strong></div><div class="pickweek"><span class="wkfont">' + d[0] + "</span><span>" + d[1] + "</span><span>" + d[2] + "</span><span>" + d[3] + "</span><span>" + d[4] + "</span><span>" + d[5] + '</span><span class="wkfont">' + d[6] + '</span></div><div class="dateswrap"><ul>' + s.slice(0).join("") + "</ul></div></div>";
        };
        return w;
    },
    _operaAddDay: function(c, b) {
        var a = b ? new Date(b.getFullYear(), b.getMonth(), b.getDate()) : new Date();
        var d = []; if (!!c) {
            a.setFullYear(a.getFullYear(), a.getMonth(), a.getDate() + c);
            d.push(a.getFullYear());
            d.push((a.getMonth() + 1) < 10 ? "0" + (a.getMonth() + 1) : (a.getMonth() + 1)); d.push(a.getDate() < 10 ? "0" + a.getDate() : a.getDate())
        };
        return d;
    },
    _onclick: function(h, i, j) {
        var g = MGTOOL.getTarget(h), a = g.nodeName, b = MGTOOL.$id("dp_id"), f, c = new Date(this.year, this.month, this.day);
        if (!this.isopen) { return false };
        var d = this.callback;
        if (MGTOOL.contains(b, g)) {
            switch (a) {
                case "EM":
                    if (/prevMonth/.test(g.className)) { this._preMonth(c) } if (/nextMonth/.test(g.className)) { this._nextMonth(c) }; break;
                case "A":
                    if (d) { d(j, g); this.valuechanged = g.title; this._resetPosition(i, b) }; break;
                default:
                    break;
            } this.isclick = true;
        } else {
            this._resetPosition(i, b); this.isclick = false
        };
    },
    _preMonth: function(a) {
        a.setMonth(a.getMonth() - 1);
        this._getCalendar(a)
    },
    _nextMonth: function(a) {
        a.setMonth(a.getMonth() + 1);
        this._getCalendar(a)
    },
    _setposition: function(k, c, e) {
        c.style.display = "block";
        var h = MGTOOL.getoffset(k), j = h.top, l = h.left, n = document.documentElement ? document.documentElement.scrollTop : document.body.scrollTop, f = document.documentElement ? document.documentElement.scrollLeft : document.body.scrollLeft, b = k.offsetHeight, g = k.offsetWidth, m = c.offsetHeight, d = c.offsetWidth, i = document.documentElement.clientHeight, a = document.documentElement.clientWidth;
        if ((j - n + m > i && j - n > m) || e == "up") {
            y = j - m; c.style.top = y + "px"
        } else {
            y = j + b; c.style.top = y + "px";
        }
        if (l - f + d > a && l - f > d) {
            x = l - d + g; c.style.left = x + "px"
        } else {
            x = l; c.style.left = x + "px"
        }
        c.style.display = "block";
        this.isopen = true;
    },
    _resetPosition: function(a, b) {
        b.style.display = "none";
        this.isopen = false
    },
    _startEvent: function() {
        var f = [];
        if (this.getsDatefrom) {
            f = this.getsDatefrom.value.split("-")
        }
        function d(h) {
            var g = /(?:[19|20]\d{2})-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])/;
            return g.test(h) && h.length == 10
        };

        var e = this;
        if (e.target) {
            e.target.onfocus = e.target.onclick = function(h) {
                e._createPanel();
                e.targetValue = this.value.split("-");
                var g = this.value && d(this.value) ? new Date(this.value.split("-")[0], this.value.split("-")[1] - 1, this.value.split("-")[2]) : (e.startDate.length && (e.startDate[0] !== "NaN" || !isNaN(e.startDate[0]))) ? new Date(e.startDate[0], e.startDate[1], e.startDate[2]) : new Date(); if (this.value && !d(this.value)) { e.target.value = "" } e.selected = [g.getFullYear(), g.getMonth(), g.getDate()];
                e.cday = g.getDate();
                e._getCalendar(g);
                e._setposition(this, MGTOOL.$id("dp_id"), e.direct);
                document.onmousedown = function(i) {
                    e._onclick(i, this, e.target)
                };
            };
            e.target.onblur = function() {
                if (!e.isclick) {
                    e._resetPosition(e.target, MGTOOL.$id("dp_id")); e.isclick = false;
                };
            };
        } else {
            var b = MGTOOL.getbyClass(document, "input", "mg_q_datepikcer");
            if (b.length) {
                for (var c = 0, a = b.length; c < a; c++) {
                    b[c].onfocus = b[c].onclick = function() {
                        e.target = this;
                        e._createPanel();
                        e.targetValue = this.value.split("-");
                        var g = this.value && d(this.value) ? new Date(this.value.split("-")[0], this.value.split("-")[1] - 1, this.value.split("-")[2]) : (e.startDate.length && (e.startDate[0] !== "NaN" || !isNaN(e.startDate[0]))) ? new Date(e.startDate[0], e.startDate[1], e.startDate[2]) : new Date(); e.selected = [g.getFullYear(), g.getMonth(), g.getDate()];
                        e.cday = g.getDate(); e._getCalendar(g);
                        e._setposition(this, MGTOOL.$id("dp_id"), e.direct);
                        document.onmousedown = function(h) {
                            e._onclick(h, this, e.target);
                        }
                    }
                }
            }
        }
    }
};