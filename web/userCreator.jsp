<%--
  Created by IntelliJ IDEA.
  User: Raphael
  Date: 07/03/2017
  Time: 18:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Creation Tool</title>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
    <script>
        $(document).on("click", "#createbutton", function (e) {
            var groupdata = $("#grouptable input:checked").map(function (i, el) {
                return el.value;
            }).get();
            /*
             //var groupdata = $("#groupID").val();
             var groupdata = new Array();
             $('#groupID > option').each(
             function(i){
             alert($(this).val());
             groupdata[i] = $(this).val();
             }); */
            //var groupdata = ["Var 1", "Var 2"];
            var fName = $("#fNameField").val();
            var sName = $("#sNameField").val();
            var descr = $("#descrField").val();

            $.get("createUser", {data: JSON.stringify({groupID: groupdata, fName: fName, sName: sName, descr: descr})},
                function (responseData) {
                    $("#statusMessage").empty().append("Username: "+responseData.samAccountName +
                        "<br/>Password :" + responseData.password)

                })
                .fail( function (xhr) {
                    $("#statusMessage").empty().append(xhr.responseText);
            });
            /* ,
             success: function (responseJson) {          // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
             //var $table = $('<table id="StatusTable" border="1">').appendTo($("#grouptable").empty()); // Create HTML <table> element and append it to HTML DOM element with ID "somediv".
             $.each(responseJson, function(index, StatusResponse) {    // Iterate over the JSON array.
             $("<tr><td>").text(StatusResponse.samAccountName).appendTo('#statusMessage')                     // Create HTML <tr> element, set its text content with currently iterated item and append it to the <table>.
             .append($("<br/>").text(StatusResponse.password))      // Create HTML <td> element, set its text content with name of currently iterated group and append it to the <tr>.
             }); //TODO fix the response handling here and see how to handle the different types of response
             }
             } */
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

/*            $(document).on("change", "#selectedUsers", function () { // When HTML DOM "dblclick" event is invoked on element with ID "somebutton", execute the following function...
                var samnames = new Array();
                $('#selectedUsers > option').each(
                    function(i){
                        samnames[i] = $(this).val();
                    });
                $.get("groupList", {grouplist: samnames}, function (responseJson) {   // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
                    var $select = $("#adgroups");                           // Locate HTML DOM element with ID "someselect".
                    $select.find("option").remove();                          // Find all child elements with tag fieldname "option" and remove them (just to prevent duplicate options when button is pressed again).
                    $.each(responseJson, function (key, value) {               // Iterate over the JSON object.
                        $("<option>").val(key).text(value).appendTo($select); // Create HTML <option> element, set its value with currently iterated key and its text content with currently iterated item and finally append it to the <select>.
                    });
                });

            });
*/
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
    <button id="somebutton">press here</button><br/>



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
