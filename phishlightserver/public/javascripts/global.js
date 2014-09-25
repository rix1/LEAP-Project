
// Userlist data array for filling in info box
var userListData = [];

// DOM Ready =========
$(document).ready(function(){

    // Populate the user table on initial page load
    populateTable();

    // Username link click
    $('#userList table tbody').on('click', 'td a.linkshowuser', showUserInfo);

    // Add user button click
    $('#btnAddUser').on('click', addUser);

    // Delete User link click
    $('#userList table tbody').on('click', 'td a.linkdeleteuser', deleteUser);

});

// Functions ==========

// Fill table with data
function populateTable(){

    var tableContent = '';

    // jQuery AJAX call for JSON
    $.getJSON('/users/userlist', function(data){

        // Stick our user data array into a userlist variable in the global object
        userListData = data;

        // For each item in our JSON, add a table row and cells to the content string
        $.each(data, function(){
            tableContent += '<tr>';
            tableContent += '<td><a href="#" class="linkshowuser" rel="' + this.userInfoName + '" title="Show Details">' + this.userInfoName + '</a></td>';
            tableContent += '<td>' + this.bankName + '</td>';
            tableContent += '<td><a href="#" class="linkdeleteuser" rel="' + this._id + '">X</a></td>';
            tableContent += '</tr>';
        });

        // Inject the whole content string into our exiting HTML table
        $('#userList table tbody').html(tableContent);
    });
};

// Show user info
function showUserInfo(event){

// Prevent Link from Firing
event.preventDefault();

// Retrive username from link rel attribute
var thisUserName = $(this).attr('rel');

// Get index of object based on id value
var arrayPosition = userListData.map(function (arrayItem){
    return arrayItem.userInfoName;
}).indexOf(thisUserName);

// Get our user object
var thisUserObject = userListData[arrayPosition];

// Populate info box
    //Populate Info Box
    $('#userInfoName').text(thisUserObject.userInfoName);
    $('#bankName').text(thisUserObject.bankName);
    $('#transTime').text(thisUserObject.transTime);
    $('#transDate').text(thisUserObject.transDate);
    $('#transPlace').text(thisUserObject.transPlace);
    $('#transType').text(thisUserObject.transType);
    $('#transAmount').text(thisUserObject.transAmount);
    $('#accBalance').text(thisUserObject.accBalance);
    $('#smsID').text(thisUserObject.smsID);
    $('#smsTime').text(thisUserObject.smsTime);
    $('#smsNum').text(thisUserObject.smsNum);
    $('#smsContent').text(thisUserObject.smsContent);
};

// Add User
function addUser(event) {
    event.preventDefault();

    // Super basic validation - increase errorCount variable if any fields are blank
    var errorCount = 0;
    $('#addUser input').each(function(index, val) {
        if($(this).val() === '') { errorCount++; }
    });

    // Check and make sure errorCount's still at zero
    if(errorCount === 0) {

        // If it is, compile all user info into one object
        var newUser = {
            'userInfoName': $('#addUser fieldset input#inputUserFullName').val(),
            'bankName': $('#addUser fieldset input#inputBankName').val(),
            'transTime': $('#addUser fieldset input#inputTransTime').val(),
            'transDate': $('#addUser fieldset input#inputTransDate').val(),
            'transPlace': $('#addUser fieldset input#inputTransPlace').val(),
            'transType': $('#addUser fieldset input#inputTransType').val(),
            'transAmount': $('#addUser fieldset input#inputTransAmount').val(),
            'accBalance': $('#addUser fieldset input#inputAccBalance').val(),
            'smsID': $('#addUser fieldset input#inputSmsID').val(),
            'smsTime': $('#addUser fieldset input#inputSmsTime').val(),
            'smsNum': $('#addUser fieldset input#inputSmsNum').val(),
            'smsContent': $('#addUser fieldset input#inputSmsContent').val()
        }

        // Use AJAX to post the object to our adduser service
        $.ajax({
            type: 'POST',
            data: newUser,
            url: '/users/adduser',
            dataType: 'JSON'
        }).done(function( response ) {

            // Check for successful (blank) response
            if (response.msg === '') {

                // Clear the form inputs
                $('#addUser fieldset input').val('');

                // Update the table
                populateTable();

            }
            else {

                // If something goes wrong, alert the error message that our service returned
                alert('Error: ' + response.msg);

            }
        });
    }
    else {
        // If errorCount is more than 0, error out
        alert('Please fill in all fields');
        return false;
    }
};


// Delete User
function deleteUser(event) {

    event.preventDefault();

    // Pop up a confirmation dialog
    var confirmation = confirm('Are you sure you want to delete this user?');


    // Check and make sure the user confirmed
    if (confirmation === true) {

        // If they did, do our delete
        $.ajax({
            type: 'DELETE',
            url: '/users/deleteuser/' + $(this).attr('rel')
        }).done(function( response ) {

            // Check for a successful (blank) response
            if (response.msg === '') {
            }
            else {
                alert('Error: ' + response.msg);
            }

            // Update the table
            populateTable();

        });

    }
    else {
        // If they said no to the confirm, do nothing
        return false;
    }
};
