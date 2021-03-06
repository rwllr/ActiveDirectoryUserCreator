<%--
MIT License

Copyright (c) 2017 Raphael Waller

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Creation Tool</title>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script>
        $(document).on("click", "#createbutton", function (e) {
            $("#statusMessage").empty().append("Please wait. Creating user can take up to 30 seconds.");
            var groupdata = $("#grouptable input:checked").map(function (i, el) {
                return el.value;
            }).get();

            var fName = $("#fNameField").val();
            var sName = $("#sNameField").val();
            var descr = $("#descrField").val();
            var exchAcc = $("#exchAccField").prop('checked');


            $.get("createUser", {data: JSON.stringify({groupID: groupdata, fName: fName, sName: sName, descr: descr, exchAcc: exchAcc})},
                function (responseData) {
                    $("#statusMessage").empty().append("Username: "+responseData.samAccountName +
                        "<br/>Password :" + responseData.password +
                        "<br/>A ticket has been created and you will receive confirmation once this account has been enabled")

                })
                .fail( function (xhr) {
                    $("#statusMessage").empty().append(xhr.responseText);
            });
        });



        $(document).on("click", "#somebutton", function () { // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
                fieldname = $("#nameField").val();
                $.get("userSearch", {fieldname: fieldname}, function (responseJson) {   // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
                    var $select = $("#searchSelect");                           // Locate HTML DOM element with ID "someselect".
                    $select.find("option").remove();                          // Find all child elements with tag fieldname "option" and remove them (just to prevent duplicate options when button is pressed again).
                    $.each(responseJson, function (key, value) {               // Iterate over the JSON object.
                        $("<option>").val(key).text(value).appendTo($select); // Create HTML <option> element, set its value with currently iterated key and its text content with currently iterated item and finally append it to the <select>.
                    });
                });
            });

        $(document).on("dblclick", "#searchSelect", function () { // When HTML DOM "dblclick" event is invoked on element with ID "somebutton", execute the following function...
            //alert("Hello world!")
            var selectedValue = $(this).val();
            var selectedText = $(this).find('option:selected').text();
            var $select = $("#selectedUsers");
            $("<option>").val(selectedValue).text(selectedText).appendTo($select); // Create HTML <option> element, set its value with currently iterated key and its text content with currently iterated item and finally append it to the <select>.
        });


            $(document).on("click", "#tablebutton", function() {        // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
                var samnames = new Array();
                $('#selectedUsers > option').each(
                    function(i){
                        samnames[i] = $(this).val();
                    });
                $.get("groupList2", {grouplist: samnames}, function(responseJson) {          // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
                    var $table = $('<table id="GroupList" border="1">').appendTo($("#grouptable").empty()); // Create HTML <table> element and append it to HTML DOM element with ID "somediv".
                    $.each(responseJson, function(index, ADGroup) {    // Iterate over the JSON array.
                        $("<tr><td><input type=checkbox name=groupID value='"+ADGroup.dn+"'>").appendTo($table)                     // Create HTML <tr> element, set its text content with currently iterated item and append it to the <table>.
                            .append($('<td  style="display:none;>').text(ADGroup.dn))        // Create HTML <td> element, set its text content with id of currently iterated product and append it to the <tr>.
                            .append($("<td>").text(ADGroup.name))      // Create HTML <td> element, set its text content with name of currently iterated group and append it to the <tr>.
                            .append($("<td>").text(ADGroup.description));    // Create HTML <td> element, set its text content with group description and append it to the <tr>.
                    });
                });
            });
    </script>
</head>
<body>
    <input id="fNameField" type="text"> <- First Name <br/>
    <input id="sNameField" type="text"> <- Surname <br/>
    <input id="descrField" type="text"> <- Description <br/>


    <input id="nameField" type="text"> <- Similar User <br/>
    <input id="exchAccField" type="checkbox"> <- Exchange Account <br/>
    <button id="somebutton">Search for Similar</button><br/>



    <div id="somediv"></div>
    <select id="searchSelect" size="7" style="width: 330px;"></select>
    <select id="selectedUsers" size="7" style="width: 330px;"></select><br/>
<!--    <select id="adgroups" size="25" style="width: 662px;"></select><br/> -->
    <button id="tablebutton">Create Group Table</button><br/>
    <div id="grouptable"></div><br/>

    <button id="createbutton">Create New User</button><br/>
    <div id="statusMessage"></div><br/>

<%

%>

</body>
</html>
