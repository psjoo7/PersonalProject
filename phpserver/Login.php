<?php
    $con = mysqli_connect("localhost", "psjoo7pproject", "xorhdakd!2", "psjoo7pproject");
    mysqli_query($con,'SET NAMES utf8');

    $UserID = isset($_POST["UserID"]) ? $_POST["UserID"] : "";
    $UserPwd = isset($_POST["UserPwd"]) ? $_POST["UserPwd"] : "";
    
    $statement = mysqli_prepare($con, "SELECT * FROM USERLIST WHERE UserID = ? AND UserPwd = ?");
    mysqli_stmt_bind_param($statement, "ss", $UserID, $UserPwd);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $UserID, $UserPwd, $UserName, $UserPhone, $UserAdd);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["UserID"] = $UserID;
        $response["UserPwd"] = $UserPwd;
        $response["UserName"] = $UserName;
        $response["UserPhone"] = $UserPhone;
        $response["UserAdd"] = $UserAdd;       
    }

    echo json_encode($response);
?>