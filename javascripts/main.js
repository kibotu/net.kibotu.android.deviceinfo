$(document).ready(function () {

    String.prototype.escape = function () {
        var tagsToReplace = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;'
        };
        return this.replace(/[&<>]/g, function (tag) {
            return tagsToReplace[tag] || tag;
        });
    };

    $(function () {
        $('#myTab a:last').tab('show');
    });

    var createTimespan = function (minutes) {
        var timespan = new Date();
        timespan.setTime(timespan.getTime() - (1000 * 60 * minutes));  // milliseconds * seconds * minutes * hours * days
        return timespan;
    };

    var spanColor = function (text, color) {
        return '<span style="color:' + color + ';">' + text + '</span>';
    };

    var createToolTip = function (tooltip, text, style) {
        return '<span style="' + style + '" data-toggle="tooltip" data-placement="top" title="' + tooltip + '">' + text + '</span>';
    };

    var toType = function (obj) {
        return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase()
    };

    var parseGetThrowableById = function (objectId, cb) {
        var throwable = Parse.Object.extend("Throwable");
        var query = new Parse.Query(throwable);
        query.get(objectId, {
            success: function (result) {
                cb(result);
            },
            error: function (object, error) {
            }
        });
    };

    var fillFields = function (exceptionJson, metaData) {

        $("#metaData").html("");
        $("#stacktrace").html("");

        for (var k = 0; k < exceptionJson.length; ++k) {

            var currentException = exceptionJson[k];
            var stacktrace = currentException['stacktrace'];
            var curErrorType = currentException['errorType'];
            var curMessage = currentException['message'];
            curMessage = curMessage ? curMessage.escape() : "[no error message]";

            $("#stacktrace").append('<br />' + spanColor(curErrorType, "#b52222") + '<br /><br />' + curMessage + "<br /><br />");

            for (var j = 0; j < stacktrace.length; ++j) {

                var element = stacktrace[j];
                var inProject = element['inProject'];

                var cls = createToolTip(element['file'], element['class'], 'color:#222;');
                var lineNumber = element['lineNumber'] != -2 ? element['lineNumber'] : element['file'].escape();
                var line = cls + ' # ' + spanColor(element['method'], '#aa2222') + '<b> : </b>' + spanColor(lineNumber, "#668bda");

                $("#stacktrace").append('<div ' + (inProject ? 'class="alert alert-danger" role="alert"' : '' ) + ' style="text-indent:1em;">' + line + '</div>');
            }

            // add metaData
            $("#metaData").html(JSON.stringify(metaData) + "<br />");
        }
    };

    var addExceptions = function (throwables) {

        /**
         * throwable:
         *  - exceptionJson
         *      - exceptions
         *          - errorType
         *          - message (nullable)
         *          - stacktrace
         *              - file
         *              - class
         *              - method
         *              - linenumber
         *              - inProject (optional)
         *  - metaData
         *  - createdAt
         *  - updatedAt
         *  - objectId
         */

        // 1) run through all throwables
        for (var i = 0; i < throwables.length; ++i) {

            var exceptionJson = JSON.parse(throwables[i].get('exceptionJson'));
            var createdAt = throwables[i].createdAt;
            var objectId = throwables[i].id;

            var dmy = createdAt.getDate() + "-" + (createdAt.getMonth() + 1) + "-" + createdAt.getFullYear() + ' ' + createdAt.getHours() + ':' + createdAt.getMinutes() + ":" + createdAt.getSeconds();

            var dateTooltip = createToolTip(createdAt, dmy, "color:#565656;");

            var firstException = exceptionJson[0];
            var message = firstException['message'] ? firstException['message'].escape() : "[no error message]";
//            message = spanColor(message, "#000");

            $("#throwables").append('<li class="list-group-item">' + dateTooltip + ' <a style="color:#b52222;" href="' + objectId + '">' + firstException['errorType'] + '</a><br/>' + message + '</li>');

            // fillFields(exceptionJson,throwables[i].get('metaData'));
        }

        $("[data-toggle=tooltip]").tooltip();

        $(".list-group-item a").click(function (event) {
            event.preventDefault();
            parseGetThrowableById($(this).attr('href'), function (result) {
                fillFields(JSON.parse(result.get('exceptionJson')), result.get('metaData'));
            });
        });

        $(".dropdown-menu a").click(function (event) {
            event.preventDefault();
            parseLoadThrowables(createTimespan($(this).attr('href')));
        });
    };

    Parse.initialize("ydr6IjoEfkcmgfqBFxeTujnVHGZ8Gtvqw8XVJtde", "TjR59xOlQlTfFRHXMaSgtxRV7pYPuFj8OAMRY8qX");

    var parseLoadThrowables = function (timespan) {

//        console.log(timespan);
//        console.log(new Date())

        var Throwable = Parse.Object.extend("Throwable");
        var query = new Parse.Query(Throwable);

        query.greaterThanOrEqualTo("createdAt", timespan);
        query.descending("createdAt");

        query.find({
            success: function (results) {

                $("#throwables").html("");

                addExceptions(results);
            },
            error: function (throwable, error) {
                console.log("Failed " + JSON.stringify(throwable) + " " + error);
            }
        });
    };

    parseLoadThrowables(createTimespan(60 * 24 * 2));
});