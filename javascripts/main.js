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

    var spanColor = function (text, color) {
        return '<span style="color:' + color + ';">' + text + '</span>';
    };

    var createToolTip = function (tooltip, text, style) {
        return '<span style="' + style + '" data-toggle="tooltip" data-placement="top" title="' + tooltip + '">' + text + '</span>';
    };

    var toType = function (obj) {
        return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase()
    };

    var parseGetThrowableById = function(objectId, cb) {
        var throwable = Parse.Object.extend("Throwable");
        var query = new Parse.Query(throwable);
        query.get(objectId, {
            success: function(result) {
                cb(result);
            },
            error: function(object, error) {
            }
        });
    };

    var fillFields = function(exceptionJson, metaData) {

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

            var dmy = createdAt.getDate() + "-" + createdAt.getMonth() + 1 + "-" + createdAt.getYear();

            var dateTooltip = createToolTip(createdAt, dmy);

            var firstException = exceptionJson[0];
            var message = firstException['message'] ? firstException['message'].escape() : "[no error message]";
            message = spanColor(message, "#000000");
            $("#throwables").append('<li class="list-group-item">' + dateTooltip + ' <a href="#' + objectId + '" class="exceptionObjectId">' + firstException['errorType'] + '</a><br/><div class="well" style="word-wrap:break-word;margin-top: 1em;">' + message + '</div></li>');

            // fillFields(exceptionJson,throwables[i].get('metaData'));
        }

        $("[data-toggle=tooltip]").tooltip();

        $("a.exceptionObjectId").click(function(event) {
            event.preventDefault();

            var objectId = $(this).attr('href').substring(1);

            console.log("click " + objectId);

            parseGetThrowableById(objectId, function(result) {
                fillFields(JSON.parse(result.get('exceptionJson')),result.get('metaData'));
            });
        });
    };

    Parse.initialize("ydr6IjoEfkcmgfqBFxeTujnVHGZ8Gtvqw8XVJtde", "TjR59xOlQlTfFRHXMaSgtxRV7pYPuFj8OAMRY8qX");

    var Throwable = Parse.Object.extend("Throwable");
    var query = new Parse.Query(Throwable);

    var last2days = 60 * 60 * 24 * 2; // 30601000
    var now = new Date();
    now.setTime(now.setTime()-30601000);
//    query.lessThanOrEqualTo("createdAt",now);

    query.find({
        success: function (results) {
            addExceptions(results);
        },
        error: function (throwable, error) {
            console.log("Failed " + error);
        }
    });
});